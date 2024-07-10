package pro.sunspace.nba.dto.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pro.sunspace.nba.dto.team.TeamCreateDto;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlayerUpdateDto extends TeamCreateDto {
}