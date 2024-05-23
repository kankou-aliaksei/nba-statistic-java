package pro.sunspace.nba.repository.impl;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import pro.sunspace.nba.dto.statistics.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistics.TeamSeasonAverage;
import pro.sunspace.nba.model.GameStatistic;
import pro.sunspace.nba.repository.api.GameStatisticRepository;
import pro.sunspace.nba.repository.SqlConstant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DefaultGameStatisticRepository implements GameStatisticRepository {

    private final DatabaseClient databaseClient;

    public DefaultGameStatisticRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<PlayerSeasonAverage> findPlayerSeasonAverage(long playerId, int seasonStartYear) {
        String query = SqlConstant.FIND_PLAYER_SEASON_AVERAGE;

        return databaseClient.sql(query)
                .bind("playerId", playerId)
                .bind("seasonStartYear", seasonStartYear)
                .map(this::mapPlayerSeasonAverage)
                .one();
    }

    @Override
    public Mono<TeamSeasonAverage> findTeamSeasonAverage(long teamId, int seasonStartYear) {
        String query = SqlConstant.FIND_TEAM_SEASON_AVERAGE;

        return databaseClient.sql(query)
                .bind("teamId", teamId)
                .bind("seasonStartYear", seasonStartYear)
                .map(this::mapTeamSeasonAverage)
                .one();
    }

    @Override
    public Flux<GameStatistic> saveAll(List<GameStatistic> gameStatistics) {
        String sql = buildBatchInsertSql(gameStatistics);

        // Note: This implementation constructs the SQL query using string concatenation for performance reasons.
        // Given that all data being inserted is numerical, there is no risk of SQL injection.
        return databaseClient.sql(sql)
                .map(row -> row.get("id", Long.class))
                .all()
                .zipWithIterable(gameStatistics)
                .map(tuple -> {
                    GameStatistic stat = tuple.getT2();
                    stat.setId(tuple.getT1());
                    return stat;
                });
    }

    private String buildBatchInsertSql(List<GameStatistic> gameStatistics) {
        StringBuilder baseSql = new StringBuilder(SqlConstant.INSERT_GAME_STATISTICS_BASE_SQL);

        String values = gameStatistics.stream()
                .map(stat -> String.format("(%d, %d, %d, %d, %d, %d, %d, %d, %d, %.2f, %d)",
                        stat.getPlayerId(),
                        stat.getTeamId(),
                        stat.getPoints(),
                        stat.getRebounds(),
                        stat.getAssists(),
                        stat.getSteals(),
                        stat.getBlocks(),
                        stat.getFouls(),
                        stat.getTurnovers(),
                        stat.getMinutesPlayed(),
                        stat.getSeasonStartYear()))
                .collect(Collectors.joining(", "));

        // Adding RETURNING clause to get the generated IDs
        baseSql.append(values).append(" RETURNING id");

        return baseSql.toString();
    }

    private PlayerSeasonAverage mapPlayerSeasonAverage(Row row, RowMetadata rowMetadata) {
        return PlayerSeasonAverage.builder()
                .playerId(row.get("player_id", Long.class))
                .avgPoints(row.get("avg_points", Double.class))
                .avgRebounds(row.get("avg_rebounds", Double.class))
                .avgAssists(row.get("avg_assists", Double.class))
                .avgSteals(row.get("avg_steals", Double.class))
                .avgBlocks(row.get("avg_blocks", Double.class))
                .avgTurnovers(row.get("avg_turnovers", Double.class))
                .avgFouls(row.get("avg_fouls", Double.class))
                .avgMinutesPlayed(row.get("avg_minutes_played", Double.class))
                .build();
    }

    private TeamSeasonAverage mapTeamSeasonAverage(Row row, RowMetadata rowMetadata) {
        return TeamSeasonAverage.builder()
                .teamId(row.get("team_id", Long.class))
                .avgPoints(row.get("avg_points", Double.class))
                .avgRebounds(row.get("avg_rebounds", Double.class))
                .avgAssists(row.get("avg_assists", Double.class))
                .avgSteals(row.get("avg_steals", Double.class))
                .avgBlocks(row.get("avg_blocks", Double.class))
                .avgTurnovers(row.get("avg_turnovers", Double.class))
                .avgFouls(row.get("avg_fouls", Double.class))
                .avgMinutesPlayed(row.get("avg_minutes_played", Double.class))
                .build();
    }
}
