-- Connect to the 'nba_stats' database
\connect nba_stats

-- Create the player table
CREATE TABLE IF NOT EXISTS player (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL
);

-- Create the team table
CREATE TABLE IF NOT EXISTS team (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL
);

-- Create the game_statistic table
CREATE TABLE IF NOT EXISTS game_statistic (
                                              id SERIAL PRIMARY KEY,
                                              player_id BIGINT NOT NULL,
                                              team_id BIGINT NOT NULL,
                                              points INT NOT NULL CHECK (points >= 0),
                                              rebounds INT NOT NULL CHECK (rebounds >= 0),
                                              assists INT NOT NULL CHECK (assists >= 0),
                                              steals INT NOT NULL CHECK (steals >= 0),
                                              blocks INT NOT NULL CHECK (blocks >= 0),
                                              turnovers INT NOT NULL CHECK (turnovers >= 0),
                                              fouls INT NOT NULL CHECK (fouls >= 0 AND fouls <= 6),
                                              minutes_played FLOAT NOT NULL CHECK (minutes_played >= 0.0 AND minutes_played <= 48.0),
                                              season_start_year INT NOT NULL,
                                              FOREIGN KEY (player_id) REFERENCES player(id),
                                              FOREIGN KEY (team_id) REFERENCES team(id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_player_season ON game_statistic(player_id, season_start_year);
CREATE INDEX IF NOT EXISTS idx_team_season ON game_statistic(team_id, season_start_year);

-- Insert sample data into team table
INSERT INTO team (name) VALUES ('Lakers') ON CONFLICT DO NOTHING;
INSERT INTO team (name) VALUES ('Warriors') ON CONFLICT DO NOTHING;
INSERT INTO team (name) VALUES ('Celtics') ON CONFLICT DO NOTHING;
INSERT INTO team (name) VALUES ('Bulls') ON CONFLICT DO NOTHING;

-- Insert sample data into player table
INSERT INTO player (name) VALUES ('LeBron James') ON CONFLICT DO NOTHING;
INSERT INTO player (name) VALUES ('Stephen Curry') ON CONFLICT DO NOTHING;
INSERT INTO player (name) VALUES ('Jayson Tatum') ON CONFLICT DO NOTHING;
INSERT INTO player (name) VALUES ('Zach LaVine') ON CONFLICT DO NOTHING;

-- Insert sample game statistics data
INSERT INTO game_statistic (player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year)
VALUES (1, 1, 28, 8, 8, 2, 1, 3, 2, 36.5, 2023);

INSERT INTO game_statistic (player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year)
VALUES (2, 2, 30, 5, 9, 3, 0, 2, 3, 38.2, 2023);

INSERT INTO game_statistic (player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year)
VALUES (3, 3, 26, 7, 4, 1, 1, 2, 4, 35.0, 2023);

INSERT INTO game_statistic (player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year)
VALUES (4, 4, 24, 5, 5, 2, 1, 3, 2, 34.0, 2023);
