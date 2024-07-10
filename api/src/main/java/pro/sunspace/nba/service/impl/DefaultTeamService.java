package pro.sunspace.nba.service.impl;

import org.springframework.stereotype.Service;
import pro.sunspace.nba.dto.team.TeamCreateDto;
import pro.sunspace.nba.dto.team.TeamDto;
import pro.sunspace.nba.dto.team.TeamUpdateDto;
import pro.sunspace.nba.repository.api.TeamRepository;
import pro.sunspace.nba.service.api.TeamService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import pro.sunspace.nba.converter.TeamConverter;

import java.util.UUID;

@Service
public class DefaultTeamService implements TeamService {

    private final TeamRepository teamRepository;

    public DefaultTeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Mono<TeamDto> create(TeamCreateDto teamDto) {
        return teamRepository.save(TeamConverter.toModel(teamDto)).map(TeamConverter::toDto);
    }

    @Override
    public Mono<TeamDto> getById(UUID id) {
        return teamRepository.findById(id).map(TeamConverter::toDto);
    }

    @Override
    public Flux<TeamDto> getAll() {
        return teamRepository.findAll().map(TeamConverter::toDto);
    }

    @Override
    public Mono<TeamDto> update(UUID id, TeamUpdateDto teamDto) {
        return teamRepository.findById(id)
                .flatMap(team -> teamRepository.save(TeamConverter.toModel(id, teamDto)))
                .map(TeamConverter::toDto);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return teamRepository.deleteById(id);
    }
}
