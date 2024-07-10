package pro.sunspace.nba.converter;

import pro.sunspace.nba.dto.statistic.GameStatisticDto;
import pro.sunspace.nba.model.GameStatistic;

public class GameStatisticConverter {

    public static GameStatistic toModel(GameStatisticDto gameStatisticDto, String traceId) {
        return GameStatistic.builder()
                .id(gameStatisticDto.getId())
                .playerId(gameStatisticDto.getPlayerId())
                .teamId(gameStatisticDto.getTeamId())
                .points(gameStatisticDto.getPoints())
                .rebounds(gameStatisticDto.getRebounds())
                .assists(gameStatisticDto.getAssists())
                .steals(gameStatisticDto.getSteals())
                .blocks(gameStatisticDto.getBlocks())
                .fouls(gameStatisticDto.getFouls())
                .turnovers(gameStatisticDto.getTurnovers())
                .minutesPlayed(gameStatisticDto.getMinutesPlayed())
                .seasonStartYear(gameStatisticDto.getSeasonStartYear())
                .traceId(traceId)
                .build();
    }
}
