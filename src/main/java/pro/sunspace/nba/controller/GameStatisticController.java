package pro.sunspace.nba.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import pro.sunspace.nba.dto.statistics.GameStatisticDto;
import pro.sunspace.nba.dto.statistics.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistics.TeamSeasonAverage;
import pro.sunspace.nba.dto.ValidList;
import pro.sunspace.nba.service.api.GameStatisticService;

@RestController
@RequestMapping("/api/statistics")
public class GameStatisticController {

    private final GameStatisticService gameStatisticService;

    public GameStatisticController(GameStatisticService gameStatisticService) {
        this.gameStatisticService = gameStatisticService;
    }

    @PostMapping("/log")
    public Mono<GameStatisticDto> logGameStatistics(@RequestBody @Valid GameStatisticDto gameStatistic) {
        return gameStatisticService.logStatistics(gameStatistic);
    }

    @PostMapping("/log/batch")
    public Flux<GameStatisticDto> logGameStatisticsBatch(@RequestBody @Valid ValidList<GameStatisticDto> gameStatistics) {
        return gameStatisticService.logStatisticsBatch(gameStatistics);
    }

    @GetMapping("/players/{playerId}/seasons/{seasonStartYear}/average")
    public Mono<PlayerSeasonAverage> getPlayerSeasonStatistics(@PathVariable long playerId, @PathVariable int seasonStartYear) {
        return gameStatisticService.getPlayerSeasonAverage(playerId, seasonStartYear);
    }

    @GetMapping("/teams/{teamId}/seasons/{seasonStartYear}/average")
    public Mono<TeamSeasonAverage> getTeamSeasonStatistics(@PathVariable long teamId, @PathVariable int seasonStartYear) {
        return gameStatisticService.getTeamSeasonAverage(teamId, seasonStartYear);
    }
}
