package pro.sunspace.nba.converter;

import pro.sunspace.nba.dto.statistics.GameStatisticDto;
import pro.sunspace.nba.model.GameStatistic;

public class GameStatisticConverter {

    public static GameStatisticDto toDto(GameStatistic gameStatistic) {
        return GameStatisticDto.builder()
                .id(gameStatistic.getId())
                .playerId(gameStatistic.getPlayerId())
                .teamId(gameStatistic.getTeamId())
                .points(gameStatistic.getPoints())
                .rebounds(gameStatistic.getRebounds())
                .assists(gameStatistic.getAssists())
                .steals(gameStatistic.getSteals())
                .blocks(gameStatistic.getBlocks())
                .fouls(gameStatistic.getFouls())
                .turnovers(gameStatistic.getTurnovers())
                .minutesPlayed(gameStatistic.getMinutesPlayed())
                .seasonStartYear(gameStatistic.getSeasonStartYear())
                .build();
    }

    public static GameStatistic toModel(GameStatisticDto gameStatisticDto) {
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
                .build();
    }
}
