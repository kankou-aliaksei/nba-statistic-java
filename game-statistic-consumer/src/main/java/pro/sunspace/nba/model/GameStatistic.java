package pro.sunspace.nba.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStatistic {
    @Id
    private UUID id;
    private UUID playerId;
    private UUID teamId;
    private String traceId;
    private int points;
    private int rebounds;
    private int assists;
    private int steals;
    private int blocks;
    private int fouls;
    private int turnovers;
    private float minutesPlayed;
    private int seasonStartYear;
}
