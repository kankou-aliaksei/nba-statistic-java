package pro.sunspace.nba.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStatisticDto {
    private Long id;

    @NotNull(message = "Player ID cannot be null")
    @Positive(message = "Player ID must be a positive number")
    private long playerId;

    @NotNull(message = "Team ID cannot be null")
    @Positive(message = "Team ID must be a positive number")
    private long teamId;

    @NotNull(message = "Points cannot be null")
    @Positive(message = "Points must be a positive integer")
    private int points;

    @NotNull(message = "Rebounds cannot be null")
    @Positive(message = "Rebounds must be a positive integer")
    private int rebounds;

    @NotNull(message = "Assists cannot be null")
    @Positive(message = "Assists must be a positive integer")
    private int assists;

    @NotNull(message = "Steals cannot be null")
    @Positive(message = "Steals must be a positive integer")
    private int steals;

    @NotNull(message = "Blocks cannot be null")
    @Positive(message = "Blocks must be a positive integer")
    private int blocks;

    @NotNull(message = "Fouls cannot be null")
    @Min(value = 0, message = "Fouls must be at least 0")
    @Max(value = 6, message = "Fouls must be at most 6")
    private int fouls;

    @NotNull(message = "Turnovers cannot be null")
    @Positive(message = "Turnovers must be a positive integer")
    private int turnovers;

    @NotNull(message = "Minutes played cannot be null")
    @Min(value = 0, message = "Minutes played must be at least 0")
    @Max(value = 48, message = "Minutes played must be at most 48")
    private float minutesPlayed;

    @NotNull(message = "Season start year cannot be null")
    @Min(value = 1900, message = "Season start year must be a valid year")
    private int seasonStartYear;
}
