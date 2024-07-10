package pro.sunspace.nba.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlayerSeasonAverage {
    private UUID playerId;
    private Double avgPoints;
    private Double avgRebounds;
    private Double avgAssists;
    private Double avgSteals;
    private Double avgBlocks;
    private Double avgTurnovers;
    private Double avgFouls;
    private Double avgMinutesPlayed;
}
