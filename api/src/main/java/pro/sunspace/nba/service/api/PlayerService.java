package pro.sunspace.nba.service.api;

import pro.sunspace.nba.dto.player.PlayerCreateDto;
import pro.sunspace.nba.dto.player.PlayerDto;
import pro.sunspace.nba.dto.player.PlayerUpdateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerService {

    Mono<PlayerDto> create(PlayerCreateDto playerDto);

    Mono<PlayerDto> getById(UUID id);

    Flux<PlayerDto> getAll();

    Mono<PlayerDto> update(UUID id, PlayerUpdateDto playerDto);

    Mono<Void> delete(UUID id);
}
