package pro.sunspace.nba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

import pro.sunspace.nba.dto.team.TeamCreateDto;
import pro.sunspace.nba.dto.team.TeamDto;
import pro.sunspace.nba.dto.team.TeamUpdateDto;
import pro.sunspace.nba.service.api.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public Mono<TeamDto> create(@RequestBody @Valid TeamCreateDto teamDto) {
        return teamService.create(teamDto);
    }

    @GetMapping("/{id}")
    public Mono<TeamDto> getById(@PathVariable long id) {
        return teamService.getById(id);
    }

    @GetMapping
    public Flux<TeamDto> getAll() {
        return teamService.getAll();
    }

    @PutMapping("/{id}")
    public Mono<TeamDto> update(@PathVariable long id, @RequestBody @Valid TeamUpdateDto teamDto) {
        return teamService.update(id, teamDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable long id) {
        return teamService.delete(id);
    }
}
