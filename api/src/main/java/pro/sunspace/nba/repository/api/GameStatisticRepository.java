package pro.sunspace.nba.repository.api;

import pro.sunspace.nba.dto.statistic.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistic.TeamSeasonAverage;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GameStatisticRepository {
    Mono<PlayerSeasonAverage> findPlayerSeasonAverage(UUID playerId, int seasonStartYear);
    Mono<TeamSeasonAverage> findTeamSeasonAverage(UUID teamId, int seasonStartYear);
}
