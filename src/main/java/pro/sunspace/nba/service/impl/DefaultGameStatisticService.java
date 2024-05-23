package pro.sunspace.nba.service.impl;

import org.springframework.stereotype.Service;
import pro.sunspace.nba.converter.GameStatisticConverter;
import pro.sunspace.nba.dto.statistics.GameStatisticDto;
import pro.sunspace.nba.dto.statistics.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistics.TeamSeasonAverage;
import pro.sunspace.nba.repository.api.GameStatisticCrudRepository;
import pro.sunspace.nba.repository.api.GameStatisticRepository;
import pro.sunspace.nba.service.api.GameStatisticService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultGameStatisticService implements GameStatisticService {

    private final GameStatisticCrudRepository gameStatisticCrudRepository;
    private final GameStatisticRepository gameStatisticRepository;

    public DefaultGameStatisticService(
            GameStatisticCrudRepository gameStatisticCrudRepository,
            GameStatisticRepository gameStatisticRepository) {
        this.gameStatisticCrudRepository = gameStatisticCrudRepository;
        this.gameStatisticRepository = gameStatisticRepository;
    }

    @Override
    public Mono<GameStatisticDto> logStatistics(GameStatisticDto gameStatisticDto) {
        return gameStatisticCrudRepository
                .save(GameStatisticConverter.toModel(gameStatisticDto))
                .map(GameStatisticConverter::toDto);
    }

    @Override
    public Flux<GameStatisticDto> logStatisticsBatch(List<GameStatisticDto> gameStatistics) {
        return gameStatisticRepository
                .saveAll(gameStatistics.stream().map(GameStatisticConverter::toModel).collect(Collectors.toList()))
                .map(GameStatisticConverter::toDto);
    }

    @Override
    public Mono<PlayerSeasonAverage> getPlayerSeasonAverage(Long playerId, int seasonStartYear) {
        return gameStatisticRepository.findPlayerSeasonAverage(playerId, seasonStartYear);
    }

    @Override
    public Mono<TeamSeasonAverage> getTeamSeasonAverage(Long teamId, int seasonStartYear) {
        return gameStatisticRepository.findTeamSeasonAverage(teamId, seasonStartYear);
    }
}

