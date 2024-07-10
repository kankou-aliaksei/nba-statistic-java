package pro.sunspace.nba.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.isolation-level}")
    private String isolationLevel;

    @Value("${spring.kafka.topic.game-statistic}")
    private String gameStatisticTopic;

    @Value("${spring.kafka.producer.acks}")
    private String acksConfig;

    @Value("${spring.kafka.producer.enable-idempotence}")
    private boolean enableIdempotence;

    @Value("${spring.kafka.producer.request-timeout-ms}")
    private int requestTimeoutMs;

    @Value("${spring.kafka.producer.delivery-timeout-ms}")
    private int deliveryTimeoutMs;

    @Value("${spring.kafka.producer.retry.backoff.ms}")
    private int retryBackoffMsConfig;

    @Bean
    public KafkaReceiver<String, byte[]> kafkaReceiver() {
        var props = Map.<String, Object>of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
                ConsumerConfig.ISOLATION_LEVEL_CONFIG, isolationLevel
        );

        return KafkaReceiver.create(
                ReceiverOptions.<String, byte[]>create(props)
                        .subscription(Collections.singleton(gameStatisticTopic))
        );
    }

    @Bean
    public KafkaSender<String, byte[]> kafkaSender() {
        var props = Map.<String, Object>of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class,
                ProducerConfig.ACKS_CONFIG, acksConfig,
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence,
                ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs,
                ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs,
                ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMsConfig
        );

        return KafkaSender.create(SenderOptions.create(props));
    }
}
