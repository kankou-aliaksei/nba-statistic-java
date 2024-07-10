package pro.sunspace.nba.repository;

public class SqlConstant {
    public static final String SAVE_GAME_STATISTIC =
            "INSERT INTO game_statistic " +
                    "(id, player_id, team_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played, season_start_year, trace_id) " +
                    "VALUES (:id, :player_id, :team_id, :points, :rebounds, :assists, :steals, :blocks, :fouls, :turnovers, :minutes_played, :season_start_year, :trace_id) " +
                    "ON CONFLICT (id) DO NOTHING";
}
