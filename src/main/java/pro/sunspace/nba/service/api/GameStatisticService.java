package pro.sunspace.nba.service.api;

import pro.sunspace.nba.dto.statistics.GameStatisticDto;
import pro.sunspace.nba.dto.statistics.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistics.TeamSeasonAverage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GameStatisticService {

    Mono<GameStatisticDto> logStatistics(GameStatisticDto gameStatisticDto);

    Flux<GameStatisticDto> logStatisticsBatch(List<GameStatisticDto> gameStatistics);

    Mono<PlayerSeasonAverage> getPlayerSeasonAverage(Long playerId, int seasonStartYear);

    Mono<TeamSeasonAverage> getTeamSeasonAverage(Long teamId, int seasonStartYear);
}
