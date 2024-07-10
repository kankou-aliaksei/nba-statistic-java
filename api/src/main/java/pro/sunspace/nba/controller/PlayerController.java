package pro.sunspace.nba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import pro.sunspace.nba.dto.player.PlayerCreateDto;
import pro.sunspace.nba.dto.player.PlayerDto;
import pro.sunspace.nba.dto.player.PlayerUpdateDto;
import pro.sunspace.nba.service.api.PlayerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public Mono<PlayerDto> create(@RequestBody @Valid PlayerCreateDto playerDto) {
        return playerService.create(playerDto);
    }

    @GetMapping("/{id}")
    public Mono<PlayerDto> getById(@PathVariable UUID id) {
        return playerService.getById(id);
    }

    @GetMapping
    public Flux<PlayerDto> getAll() {
        return playerService.getAll();
    }

    @PutMapping("/{id}")
    public Mono<PlayerDto> update(@PathVariable UUID id, @RequestBody @Valid PlayerUpdateDto playerDto) {
        return playerService.update(id, playerDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable UUID id) {
        return playerService.delete(id);
    }
}
