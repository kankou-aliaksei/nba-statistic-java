package pro.sunspace.nba.repository;

public class SqlConstant {

    public static final String FIND_PLAYER_SEASON_AVERAGE = "SELECT player_id, AVG(points) AS avg_points, AVG(rebounds) AS avg_rebounds, " +
            "AVG(assists) AS avg_assists, AVG(steals) AS avg_steals, AVG(blocks) AS avg_blocks, " +
            "AVG(turnovers) AS avg_turnovers, AVG(fouls) AS avg_fouls, AVG(minutes_played) AS avg_minutes_played " +
            "FROM game_statistic WHERE player_id = :playerId AND season_start_year = :seasonStartYear GROUP BY player_id";

    public static final String FIND_TEAM_SEASON_AVERAGE = "SELECT team_id, AVG(points) AS avg_points, AVG(rebounds) AS avg_rebounds, " +
            "AVG(assists) AS avg_assists, AVG(steals) AS avg_steals, AVG(blocks) AS avg_blocks, " +
            "AVG(turnovers) AS avg_turnovers, AVG(fouls) AS avg_fouls, AVG(minutes_played) AS avg_minutes_played " +
            "FROM game_statistic WHERE team_id = :teamId AND season_start_year = :seasonStartYear GROUP BY team_id";
}
