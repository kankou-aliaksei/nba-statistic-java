package pro.sunspace.nba.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStatisticDto {
    private UUID id;

    @NotNull(message = "Player ID cannot be null")
    private UUID playerId;

    @NotNull(message = "Team ID cannot be null")
    private UUID teamId;

    @NotNull(message = "Points cannot be null")
    @Min(value = 0, message = "Points must be at least 0")
    private int points;

    @NotNull(message = "Rebounds cannot be null")
    @Min(value = 0, message = "Rebounds must be at least 0")
    private int rebounds;

    @NotNull(message = "Assists cannot be null")
    @Min(value = 0, message = "Assists must be at least 0")
    private int assists;

    @NotNull(message = "Steals cannot be null")
    @Min(value = 0, message = "Steals must be at least 0")
    private int steals;

    @NotNull(message = "Blocks cannot be null")
    @Min(value = 0, message = "Blocks must be at least 0")
    private int blocks;

    @NotNull(message = "Fouls cannot be null")
    @Min(value = 0, message = "Fouls must be at least 0")
    @Max(value = 6, message = "Fouls must be at most 6")
    private int fouls;

    @NotNull(message = "Turnovers cannot be null")
    @Min(value = 0, message = "Turnovers must be at least 0")
    private int turnovers;

    @NotNull(message = "Minutes played cannot be null")
    @Min(value = 0, message = "Minutes played must be at least 0")
    @Max(value = 48, message = "Minutes played must be at most 48")
    private float minutesPlayed;

    @NotNull(message = "Season start year cannot be null")
    @Min(value = 1900, message = "Season start year must be a valid year")
    private int seasonStartYear;
}
