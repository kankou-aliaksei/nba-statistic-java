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
import pro.sunspace.nba.dto.player.PlayerCreateDto;
import pro.sunspace.nba.dto.player.PlayerDto;
import pro.sunspace.nba.dto.player.PlayerUpdateDto;
import pro.sunspace.nba.service.api.PlayerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(PlayerController.class)
@Import(TestConfig.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    @Test
    void testCreatePlayer_Success() {
        // Arrange
        PlayerCreateDto playerCreateDto = new PlayerCreateDto("John Doe");
        PlayerDto playerDto = PlayerDto.builder().id(1L).name("John Doe").build();
        Mockito.when(playerService.create(Mockito.any(PlayerCreateDto.class))).thenReturn(Mono.just(playerDto));

        // Act & Assert
        webTestClient.post().uri("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playerCreateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class).isEqualTo(playerDto);
    }

    @Test
    void testGetPlayerById_Success() {
        // Arrange
        long playerId = 1L;
        PlayerDto playerDto = PlayerDto.builder().id(playerId).name("John Doe").build();
        Mockito.when(playerService.getById(playerId)).thenReturn(Mono.just(playerDto));

        // Act & Assert
        webTestClient.get().uri("/api/players/{id}", playerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class).isEqualTo(playerDto);
    }

    @Test
    void testGetAllPlayers_Success() {
        // Arrange
        PlayerDto player1 = PlayerDto.builder().id(1L).name("John Doe").build();
        PlayerDto player2 = PlayerDto.builder().id(2L).name("Jane Doe").build();
        Mockito.when(playerService.getAll()).thenReturn(Flux.just(player1, player2));

        // Act & Assert
        webTestClient.get().uri("/api/players")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlayerDto.class).hasSize(2).contains(player1, player2);
    }

    @Test
    void testUpdatePlayer_Success() {
        // Arrange
        long playerId = 1L;
        PlayerUpdateDto playerUpdateDto = PlayerUpdateDto.builder().name("John Doe").build();
        PlayerDto updatedPlayerDto = PlayerDto.builder().id(playerId).name("John Doe").build();
        Mockito.when(playerService.update(Mockito.eq(playerId), Mockito.any(PlayerUpdateDto.class))).thenReturn(Mono.just(updatedPlayerDto));

        // Act & Assert
        webTestClient.put().uri("/api/players/{id}", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playerUpdateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class).isEqualTo(updatedPlayerDto);
    }
}
