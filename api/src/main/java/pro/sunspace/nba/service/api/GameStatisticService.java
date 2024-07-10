package pro.sunspace.nba.service.api;

import pro.sunspace.nba.dto.statistic.*;
import pro.sunspace.nba.model.GameStatistic;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GameStatisticService {

    Mono<GameStatistic> logStatistic(GameStatistic gameStatistic);

    Mono<PlayerSeasonAverage> getPlayerSeasonAverage(UUID playerId, int seasonStartYear);

    Mono<TeamSeasonAverage> getTeamSeasonAverage(UUID teamId, int seasonStartYear);
}
