package pro.sunspace.nba.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapAdapter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sunspace.nba.model.GameStatistic;
import pro.sunspace.nba.repository.api.GameStatisticRepository;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameStatisticConsumer {

    private static final Logger logger = LoggerFactory.getLogger(GameStatisticConsumer.class);
    private static final String ERROR_TAG = "error";
    private static final String ERROR_MESSAGE_KEY = "message";
    private static final String ERROR_OBJECT_KEY = "error.object";
    private static final String ERROR_EVENT_KEY = "event";

    private final KafkaReceiver<String, byte[]> kafkaReceiver;
    private final KafkaSender<String, byte[]> kafkaByteSender;
    private final GameStatisticRepository gameStatisticRepository;
    private final Tracer tracer;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.consumer.max-retries}")
    private int maxRetries;

    @Value("${spring.kafka.consumer.dead-letter-topic}")
    private String deadLetterTopic;

    @Value("${spring.kafka.consumer.retry.backoff.ms}")
    private long retryBackoffMs;

    public GameStatisticConsumer(KafkaReceiver<String, byte[]> kafkaReceiver,
                                 KafkaSender<String, byte[]> kafkaByteSender,
                                 GameStatisticRepository gameStatisticRepository,
                                 Tracer tracer) {
        this.kafkaReceiver = kafkaReceiver;
        this.kafkaByteSender = kafkaByteSender;
        this.gameStatisticRepository = gameStatisticRepository;
        this.tracer = tracer;
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void consume() {
        kafkaReceiver.receive()
                .flatMap(this::processRecord)
                .subscribe();
    }

    private Mono<Void> processRecord(ReceiverRecord<String, byte[]> record) {
        var spanContextMap = new HashMap<String, String>();
        record.headers().forEach(header -> spanContextMap.put(header.key(), new String(header.value(), StandardCharsets.UTF_8)));

        var parentContext = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapAdapter(spanContextMap));
        var span = tracer.buildSpan("processRecord").asChildOf(parentContext).start();

        var data = record.value();
        GameStatistic gameStatistic;

        try {
            gameStatistic = objectMapper.readValue(data, GameStatistic.class);
        } catch (Exception e) {
            return handleDeserializationError(record, span, e);
        }

        logger.info("Received message: {}", gameStatistic);

        return gameStatisticRepository.save(gameStatistic)
                .doOnSuccess(signalType -> record.receiverOffset().commit())
                .retryWhen(createRetrySpec())
                .doOnError(e -> logError(span, e))
                .onErrorResume(e -> handleError(record, e))
                .doOnTerminate(span::finish);
    }

    private Mono<Void> handleDeserializationError(ReceiverRecord<String, byte[]> record, io.opentracing.Span span, Exception e) {
        logError(span, e);
        logger.error("Deserialization error occurred", e);
        return sendToDeadLetterTopic(record, "Deserialization error occurred")
                .then(record.receiverOffset().commit())
                .doOnTerminate(span::finish);
    }

    private void logError(io.opentracing.Span span, Throwable e) {
        span.setTag(ERROR_TAG, true);
        span.log(Map.of(ERROR_EVENT_KEY, ERROR_TAG, ERROR_OBJECT_KEY, e, ERROR_MESSAGE_KEY, e.getMessage()));
    }

    private Mono<Void> handleError(ReceiverRecord<String, byte[]> record, Throwable e) {
        if (isRecoverableError(e.getCause())) {
            logger.warn("Recoverable error occurred during DB saving, will retry: {}", e.getMessage());
            return kafkaReceiver.doOnConsumer(consumer -> {
                consumer.seek(new TopicPartition(
                                record.receiverOffset().topicPartition().topic(),
                                record.receiverOffset().topicPartition().partition()),
                        record.receiverOffset().offset());
                return Mono.empty();
            }).then();
        } else {
            logger.error("Non-recoverable error occurred during DB saving, sending message to Dead Letter Topic");
            return sendToDeadLetterTopic(record, "Non-recoverable error occurred during DB saving");
        }
    }

    private Mono<Void> sendToDeadLetterTopic(ReceiverRecord<String, byte[]> record, String errorMessage) {
        return sendToDeadLetterTopic(record.receiverOffset().topicPartition(),
                record.receiverOffset().offset(),
                record.value(),
                errorMessage);
    }

    private Mono<Void> sendToDeadLetterTopic(TopicPartition topicPartition, long offset, byte[] rawData, String errorMessage) {
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(deadLetterTopic, null, rawData);
        producerRecord.headers().add(new RecordHeader(ERROR_TAG, errorMessage.getBytes(StandardCharsets.UTF_8)));
        producerRecord.headers().add(new RecordHeader("topic", topicPartition.topic().getBytes(StandardCharsets.UTF_8)));
        producerRecord.headers().add(new RecordHeader("partition", String.valueOf(topicPartition.partition()).getBytes(StandardCharsets.UTF_8)));
        producerRecord.headers().add(new RecordHeader("offset", String.valueOf(offset).getBytes(StandardCharsets.UTF_8)));

        return kafkaByteSender.send(Mono.just(SenderRecord.create(producerRecord, null)))
                .doOnNext(result -> logger.info("Successfully sent non-recoverable error to Dead Letter Topic"))
                .doOnError(ex -> logger.error("Failed to send non-recoverable error message to Dead Letter Topic", ex))
                .then();
    }

    private RetryBackoffSpec createRetrySpec() {
        return Retry.backoff(maxRetries, Duration.ofMillis(retryBackoffMs))
                .maxBackoff(Duration.ofSeconds(30))
                .jitter(0.5)
                .doBeforeRetry(retrySignal -> logger.warn("Retrying operation, attempt {}/{}", retrySignal.totalRetries() + 1, maxRetries));
    }

    private boolean isRecoverableError(Throwable throwable) {
        return throwable instanceof org.springframework.dao.DataAccessResourceFailureException;
    }
}
