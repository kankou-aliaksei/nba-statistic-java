package pro.sunspace.nba.service.api;

import pro.sunspace.nba.dto.player.PlayerCreateDto;
import pro.sunspace.nba.dto.player.PlayerDto;
import pro.sunspace.nba.dto.player.PlayerUpdateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {

    Mono<PlayerDto> create(PlayerCreateDto playerDto);

    Mono<PlayerDto> getById(long id);

    Flux<PlayerDto> getAll();

    Mono<PlayerDto> update(long id, PlayerUpdateDto playerDto);

    Mono<Void> delete(Long id);
}
