package pro.sunspace.nba.repository.impl;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import pro.sunspace.nba.dto.statistic.PlayerSeasonAverage;
import pro.sunspace.nba.dto.statistic.TeamSeasonAverage;
import pro.sunspace.nba.repository.api.GameStatisticRepository;
import pro.sunspace.nba.repository.SqlConstant;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class DefaultGameStatisticRepository implements GameStatisticRepository {

    private final DatabaseClient databaseClient;

    public DefaultGameStatisticRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<PlayerSeasonAverage> findPlayerSeasonAverage(UUID playerId, int seasonStartYear) {
        var query = SqlConstant.FIND_PLAYER_SEASON_AVERAGE;

        return databaseClient.sql(query)
                .bind("playerId", playerId)
                .bind("seasonStartYear", seasonStartYear)
                .map(this::mapPlayerSeasonAverage)
                .one();
    }

    @Override
    public Mono<TeamSeasonAverage> findTeamSeasonAverage(UUID teamId, int seasonStartYear) {
        var query = SqlConstant.FIND_TEAM_SEASON_AVERAGE;

        return databaseClient.sql(query)
                .bind("teamId", teamId)
                .bind("seasonStartYear", seasonStartYear)
                .map(this::mapTeamSeasonAverage)
                .one();
    }

    private PlayerSeasonAverage mapPlayerSeasonAverage(Row row, RowMetadata rowMetadata) {
        return PlayerSeasonAverage.builder()
                .playerId(row.get("player_id", UUID.class))
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
                .teamId(row.get("team_id", UUID.class))
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
