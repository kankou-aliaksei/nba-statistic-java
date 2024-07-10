package pro.sunspace.nba.controller;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sunspace.nba.converter.GameStatisticConverter;
import pro.sunspace.nba.dto.statistic.*;
import pro.sunspace.nba.service.api.GameStatisticService;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/statistic")
public class GameStatisticController {

    private static final Logger logger = LoggerFactory.getLogger(GameStatisticController.class);

    private final GameStatisticService gameStatisticService;
    private final Tracer tracer;

    public GameStatisticController(
            GameStatisticService gameStatisticService,
            Tracer tracer) {
        this.gameStatisticService = gameStatisticService;
        this.tracer = tracer;
    }

    @PostMapping("/log")
    public Mono<ResponseEntity<GameStatisticResponse>> logGameStatistic(@RequestBody @Valid GameStatisticDto gameStatistic) {
        var span = tracer.buildSpan("GameStatisticController.logGameStatistic").start();
        var traceId = span.context().toTraceId();

        try (Scope ignored = tracer.scopeManager().activate(span)) {
            return gameStatisticService.logStatistic(GameStatisticConverter.toModel(gameStatistic, traceId))
                    .map(statistic -> ResponseEntity.ok(GameStatisticResponse.builder().traceId(traceId).build()))
                    .doFinally(signalType -> span.finish());
        }
    }

    @GetMapping("/players/{playerId}/seasons/{seasonStartYear}/average")
    public Mono<PlayerSeasonAverage> getPlayerSeasonStatistics(@PathVariable UUID playerId, @PathVariable int seasonStartYear) {
        return gameStatisticService.getPlayerSeasonAverage(playerId, seasonStartYear);
    }

    @GetMapping("/teams/{teamId}/seasons/{seasonStartYear}/average")
    public Mono<TeamSeasonAverage> getTeamSeasonStatistics(@PathVariable UUID teamId, @PathVariable int seasonStartYear) {
        return gameStatisticService.getTeamSeasonAverage(teamId, seasonStartYear);
    }
}
