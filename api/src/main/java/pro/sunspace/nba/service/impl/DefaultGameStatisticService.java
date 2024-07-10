package pro.sunspace.nba.service.impl;

import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapAdapter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sunspace.nba.dto.statistic.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistic.TeamSeasonAverage;
import pro.sunspace.nba.exception.ServerException;
import pro.sunspace.nba.model.GameStatistic;
import pro.sunspace.nba.repository.api.GameStatisticRepository;
import pro.sunspace.nba.service.api.GameStatisticService;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DefaultGameStatisticService implements GameStatisticService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultGameStatisticService.class);

    private final GameStatisticRepository gameStatisticRepository;
    private final KafkaSender<String, GameStatistic> kafkaSender;
    private final Tracer tracer;

    @Value("${spring.kafka.topic.game-statistic}")
    private String gameStatisticTopic;

    public DefaultGameStatisticService(
            GameStatisticRepository gameStatisticRepository,
            KafkaSender<String, GameStatistic> kafkaSender,
            Tracer tracer) {
        this.gameStatisticRepository = gameStatisticRepository;
        this.kafkaSender = kafkaSender;
        this.tracer = tracer;
    }

    @Override
    public Mono<GameStatistic> logStatistic(GameStatistic gameStatistic) {
        var activeSpan = tracer.activeSpan();
        var span = tracer.buildSpan("GameStatisticService.logStatistic")
                .asChildOf(activeSpan)
                .start();

        gameStatistic.setId(UUID.randomUUID());
        var producerRecord = new ProducerRecord<>(gameStatisticTopic, gameStatistic.getId().toString(), gameStatistic);

        var spanContextMap = new HashMap<String, String>();
        tracer.inject(span.context(), Format.Builtin.TEXT_MAP, new TextMapAdapter(spanContextMap));

        spanContextMap.forEach((key, value) ->
                producerRecord.headers().add(new RecordHeader(key, value.getBytes(StandardCharsets.UTF_8)))
        );

        return kafkaSender
                .send(Mono.just(SenderRecord.create(producerRecord, null)))
                .then(Mono.just(gameStatistic))
                .doOnError(e -> {
                    span.setTag("error", true);
                    span.log(Map.of("event", "error", "error.object", e, "message", e.getMessage()));
                })
                .onErrorMap(e -> new ServerException(e.getMessage(), gameStatistic.getTraceId()))
                .doOnTerminate(span::finish);
    }

    @Override
    public Mono<PlayerSeasonAverage> getPlayerSeasonAverage(UUID playerId, int seasonStartYear) {
        logger.info("Fetching player season average for playerId: {} and seasonStartYear: {}", playerId, seasonStartYear);
        return gameStatisticRepository.findPlayerSeasonAverage(playerId, seasonStartYear);
    }

    @Override
    public Mono<TeamSeasonAverage> getTeamSeasonAverage(UUID teamId, int seasonStartYear) {
        logger.info("Fetching team season average for teamId: {} and seasonStartYear: {}", teamId, seasonStartYear);
        return gameStatisticRepository.findTeamSeasonAverage(teamId, seasonStartYear);
    }
}
