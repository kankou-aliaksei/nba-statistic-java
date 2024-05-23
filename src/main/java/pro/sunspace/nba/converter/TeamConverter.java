package pro.sunspace.nba.converter;

import pro.sunspace.nba.dto.team.TeamCreateDto;
import pro.sunspace.nba.dto.team.TeamDto;
import pro.sunspace.nba.dto.team.TeamUpdateDto;
import pro.sunspace.nba.model.Team;

public class TeamConverter {

    public static TeamDto toDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }

    public static Team toModel(TeamCreateDto teamDto) {
        return Team.builder()
                .name(teamDto.getName())
                .build();
    }

    public static Team toModel(long id, TeamUpdateDto teamDto) {
        return Team.builder()
                .id(id)
                .name(teamDto.getName())
                .build();
    }
}
