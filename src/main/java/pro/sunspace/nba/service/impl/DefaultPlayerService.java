package pro.sunspace.nba.service.impl;

import org.springframework.stereotype.Service;
import pro.sunspace.nba.dto.player.PlayerCreateDto;
import pro.sunspace.nba.dto.player.PlayerDto;
import pro.sunspace.nba.dto.player.PlayerUpdateDto;
import pro.sunspace.nba.repository.api.PlayerRepository;
import pro.sunspace.nba.service.api.PlayerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import pro.sunspace.nba.converter.PlayerConverter;

@Service
public class DefaultPlayerService implements PlayerService {

    private final PlayerRepository playerRepository;

    public DefaultPlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Mono<PlayerDto> create(PlayerCreateDto playerDto) {
        return playerRepository.save(PlayerConverter.toModel(playerDto)).map(PlayerConverter::toDto);
    }

    @Override
    public Mono<PlayerDto> getById(long id) {
        return playerRepository.findById(id).map(PlayerConverter::toDto);
    }

    @Override
    public Flux<PlayerDto> getAll() {
        return playerRepository.findAll().map(PlayerConverter::toDto);
    }

    @Override
    public Mono<PlayerDto> update(long id, PlayerUpdateDto playerDto) {
        return playerRepository.findById(id)
                .flatMap(player -> playerRepository.save(PlayerConverter.toModel(id, playerDto)))
                .map(PlayerConverter::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return playerRepository.deleteById(id);
    }
}
