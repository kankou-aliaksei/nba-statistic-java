package pro.sunspace.nba.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pro.sunspace.nba.config.TestConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import pro.sunspace.nba.dto.statistics.GameStatisticDto;
import pro.sunspace.nba.dto.statistics.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistics.TeamSeasonAverage;
import pro.sunspace.nba.dto.ValidList;
import pro.sunspace.nba.service.api.GameStatisticService;

import java.util.Arrays;

@WebFluxTest(GameStatisticController.class)
@Import(TestConfig.class)
public class GameStatisticControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameStatisticService gameStatisticService;

    @Test
    void testLogGameStatistics() {
        // Arrange
        GameStatisticDto gameStatistic = GameStatisticDto.builder()
                .playerId(1L)
                .teamId(1L)
                .points(4)
                .rebounds(1)
                .assists(1)
                .steals(1)
                .blocks(1)
                .fouls(1)
                .turnovers(1)
                .minutesPlayed(15.0f)
                .seasonStartYear(2000)
                .build();
        Mockito.when(gameStatisticService.logStatistics(gameStatistic)).thenReturn(Mono.just(gameStatistic));

        // Act & Assert
        webTestClient.post().uri("/api/statistics/log")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(gameStatistic)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameStatisticDto.class).isEqualTo(gameStatistic);
    }

    @Test
    void testLogGameStatisticsBatch() {
        // Arrange
        GameStatisticDto stat1 = GameStatisticDto.builder()
                .playerId(1L)
                .teamId(1L)
                .points(4)
                .rebounds(1)
                .assists(1)
                .steals(1)
                .blocks(1)
                .fouls(1)
                .turnovers(1)
                .minutesPlayed(15.0f)
                .seasonStartYear(2000)
                .build();
        GameStatisticDto stat2 = GameStatisticDto.builder()
                .playerId(1L)
                .teamId(1L)
                .points(4)
                .rebounds(1)
                .assists(2)
                .steals(3)
                .blocks(4)
                .fouls(1)
                .turnovers(1)
                .minutesPlayed(15.0f)
                .seasonStartYear(2000)
                .build();
        ValidList<GameStatisticDto> stats = new ValidList<>();
        stats.setList(Arrays.asList(stat1, stat2));
        Mockito.when(gameStatisticService.logStatisticsBatch(stats)).thenReturn(Flux.just(stat1, stat2));

        // Act & Assert
        webTestClient.post().uri("/api/statistics/log/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stats)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameStatisticDto.class).hasSize(2).contains(stat1, stat2);
    }

    @Test
    void testGetPlayerSeasonStatistics() {
        // Arrange
        long playerId = 1L;
        int seasonStartYear = 2021;
        PlayerSeasonAverage average = new PlayerSeasonAverage(); // Assume necessary setters here
        Mockito.when(gameStatisticService.getPlayerSeasonAverage(playerId, seasonStartYear)).thenReturn(Mono.just(average));

        // Act & Assert
        webTestClient.get().uri("/api/statistics/players/{playerId}/seasons/{seasonStartYear}/average", playerId, seasonStartYear)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerSeasonAverage.class).isEqualTo(average);
    }

    @Test
    void testGetTeamSeasonStatistics() {
        // Arrange
        long teamId = 1L;
        int seasonStartYear = 2021;
        TeamSeasonAverage average = new TeamSeasonAverage(); // Assume necessary setters here
        Mockito.when(gameStatisticService.getTeamSeasonAverage(teamId, seasonStartYear)).thenReturn(Mono.just(average));

        // Act & Assert
        webTestClient.get().uri("/api/statistics/teams/{teamId}/seasons/{seasonStartYear}/average", teamId, seasonStartYear)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamSeasonAverage.class).isEqualTo(average);
    }
}
