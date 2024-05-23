package pro.sunspace.nba.service.api;

import pro.sunspace.nba.dto.team.TeamCreateDto;
import pro.sunspace.nba.dto.team.TeamDto;
import pro.sunspace.nba.dto.team.TeamUpdateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamService {

    Mono<TeamDto> create(TeamCreateDto teamDto);

    Mono<TeamDto> getById(long id);

    Flux<TeamDto> getAll();

    Mono<TeamDto> update(long id, TeamUpdateDto teamDto);

    Mono<Void> delete(Long id);
}
