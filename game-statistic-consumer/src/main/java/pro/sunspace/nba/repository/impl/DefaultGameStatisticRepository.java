package pro.sunspace.nba.repository.impl;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import pro.sunspace.nba.model.GameStatistic;
import pro.sunspace.nba.repository.SqlConstant;
import pro.sunspace.nba.repository.api.GameStatisticRepository;
import reactor.core.publisher.Mono;

@Repository
public class DefaultGameStatisticRepository implements GameStatisticRepository {

    private final DatabaseClient databaseClient;

    public DefaultGameStatisticRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Void> save(GameStatistic gameStatistic) {
        return databaseClient.sql(SqlConstant.SAVE_GAME_STATISTIC)
                .bind("id", gameStatistic.getId())
                .bind("player_id", gameStatistic.getPlayerId())
                .bind("team_id", gameStatistic.getTeamId())
                .bind("points", gameStatistic.getPoints())
                .bind("rebounds", gameStatistic.getRebounds())
                .bind("assists", gameStatistic.getAssists())
                .bind("steals", gameStatistic.getSteals())
                .bind("blocks", gameStatistic.getBlocks())
                .bind("fouls", gameStatistic.getFouls())
                .bind("turnovers", gameStatistic.getTurnovers())
                .bind("minutes_played", gameStatistic.getMinutesPlayed())
                .bind("season_start_year", gameStatistic.getSeasonStartYear())
                .bind("trace_id", gameStatistic.getTraceId())
                .then();
    }
}
