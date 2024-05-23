package pro.sunspace.nba.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlayerSeasonAverage {
    private Long playerId;
    private Double avgPoints;
    private Double avgRebounds;
    private Double avgAssists;
    private Double avgSteals;
    private Double avgBlocks;
    private Double avgTurnovers;
    private Double avgFouls;
    private Double avgMinutesPlayed;
}