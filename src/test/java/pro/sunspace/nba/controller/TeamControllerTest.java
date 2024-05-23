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
import pro.sunspace.nba.dto.team.TeamCreateDto;
import pro.sunspace.nba.dto.team.TeamDto;
import pro.sunspace.nba.dto.team.TeamUpdateDto;
import pro.sunspace.nba.service.api.TeamService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(TeamController.class)
@Import(TestConfig.class)
public class TeamControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TeamService teamService;

    @Test
    void testCreateTeam_Success() {
        // Arrange
        TeamCreateDto teamCreateDto = new TeamCreateDto("Los Angeles Lakers");
        TeamDto teamDto = TeamDto.builder().id(1L).name("Los Angeles Lakers").build();
        Mockito.when(teamService.create(Mockito.any(TeamCreateDto.class))).thenReturn(Mono.just(teamDto));

        // Act & Assert
        webTestClient.post().uri("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamCreateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class).isEqualTo(teamDto);
    }

    @Test
    void testGetTeamById_Success() {
        // Arrange
        long teamId = 1L;
        TeamDto teamDto = TeamDto.builder().id(teamId).name("Los Angeles Lakers").build();
        Mockito.when(teamService.getById(teamId)).thenReturn(Mono.just(teamDto));

        // Act & Assert
        webTestClient.get().uri("/api/teams/{id}", teamId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class).isEqualTo(teamDto);
    }

    @Test
    void testGetAllTeams_Success() {
        // Arrange
        TeamDto team1 = TeamDto.builder().id(1L).name("Los Angeles Lakers").build();
        TeamDto team2 = TeamDto.builder().id(2L).name("Chicago Bulls").build();
        Mockito.when(teamService.getAll()).thenReturn(Flux.just(team1, team2));

        // Act & Assert
        webTestClient.get().uri("/api/teams")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TeamDto.class).hasSize(2).contains(team1, team2);
    }

    @Test
    void testUpdateTeam_Success() {
        // Arrange
        long teamId = 1L;
        TeamUpdateDto teamUpdateDto = TeamUpdateDto.builder().name("Los Angeles Lakers").build();
        TeamDto updatedTeamDto = TeamDto.builder().id(teamId).name("Los Angeles Lakers").build();
        Mockito.when(teamService.update(Mockito.eq(teamId), Mockito.any(TeamUpdateDto.class))).thenReturn(Mono.just(updatedTeamDto));

        // Act & Assert
        webTestClient.put().uri("/api/teams/{id}", teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teamUpdateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeamDto.class).isEqualTo(updatedTeamDto);
    }
}
