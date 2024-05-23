package pro.sunspace.nba.repository.api;

import pro.sunspace.nba.dto.statistics.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistics.TeamSeasonAverage;
import pro.sunspace.nba.model.GameStatistic;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GameStatisticRepository {
    Mono<PlayerSeasonAverage> findPlayerSeasonAverage(long playerId, int seasonStartYear);
    Mono<TeamSeasonAverage> findTeamSeasonAverage(long teamId, int seasonStartYear);
    Flux<GameStatistic> saveAll(List<GameStatistic> gameStatistics);
}
