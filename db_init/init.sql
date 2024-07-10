-- Connect to the 'nba_stats' database
\connect nba_stats

-- Enable the extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the player table
CREATE TABLE IF NOT EXISTS player (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL
);

-- Create the team table
CREATE TABLE IF NOT EXISTS team (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL
);

-- Create the game_statistic table with trace_id as VARCHAR(16)
CREATE TABLE IF NOT EXISTS game_statistic (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    team_id UUID NOT NULL,
    points INT NOT NULL CHECK (points >= 0),
    rebounds INT NOT NULL CHECK (rebounds >= 0),
    assists INT NOT NULL CHECK (assists >= 0),
    steals INT NOT NULL CHECK (steals >= 0),
    blocks INT NOT NULL CHECK (blocks >= 0),
    turnovers INT NOT NULL CHECK (turnovers >= 0),
    fouls INT NOT NULL CHECK (fouls >= 0 AND fouls <= 6),
    minutes_played FLOAT NOT NULL CHECK (minutes_played >= 0.0 AND minutes_played <= 48.0),
    season_start_year INT NOT NULL,
    trace_id VARCHAR(16) NOT NULL,
    FOREIGN KEY (player_id) REFERENCES player(id),
    FOREIGN KEY (team_id) REFERENCES team(id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_player_season ON game_statistic(player_id, season_start_year);
CREATE INDEX IF NOT EXISTS idx_team_season ON game_statistic(team_id, season_start_year);

-- Insert sample data into team table
INSERT INTO team (id, name) VALUES (uuid_generate_v4(), 'Lakers') ON CONFLICT DO NOTHING;
INSERT INTO team (id, name) VALUES (uuid_generate_v4(), 'Warriors') ON CONFLICT DO NOTHING;
INSERT INTO team (id, name) VALUES (uuid_generate_v4(), 'Celtics') ON CONFLICT DO NOTHING;
INSERT INTO team (id, name) VALUES (uuid_generate_v4(), 'Bulls') ON CONFLICT DO NOTHING;

-- Insert sample data into player table
INSERT INTO player (id, name) VALUES (uuid_generate_v4(), 'LeBron James') ON CONFLICT DO NOTHING;
INSERT INTO player (id, name) VALUES (uuid_generate_v4(), 'Stephen Curry') ON CONFLICT DO NOTHING;
INSERT INTO player (id, name) VALUES (uuid_generate_v4(), 'Jayson Tatum') ON CONFLICT DO NOTHING;
INSERT INTO player (id, name) VALUES (uuid_generate_v4(), 'Zach LaVine') ON CONFLICT DO NOTHING;

-- Insert sample game statistics data with trace_id
INSERT INTO game_statistic (id, player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year, trace_id)
VALUES (uuid_generate_v4(), (SELECT id FROM player WHERE name='LeBron James'), (SELECT id FROM team WHERE name='Lakers'), 28, 8, 8, 2, 1, 3, 2, 36.5, 2023, 'fdf5f3538490b70c');

INSERT INTO game_statistic (id, player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year, trace_id)
VALUES (uuid_generate_v4(), (SELECT id FROM player WHERE name='Stephen Curry'), (SELECT id FROM team WHERE name='Warriors'), 30, 5, 9, 3, 0, 2, 3, 38.2, 2023, 'a1b2c3d4e5f67890');

INSERT INTO game_statistic (id, player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year, trace_id)
VALUES (uuid_generate_v4(), (SELECT id FROM player WHERE name='Jayson Tatum'), (SELECT id FROM team WHERE name='Celtics'), 26, 7, 4, 1, 1, 2, 4, 35.0, 2023, 'b2c3d4e5f67890ab');

INSERT INTO game_statistic (id, player_id, team_id, points, rebounds, assists, steals, blocks, turnovers, fouls, minutes_played, season_start_year, trace_id)
VALUES (uuid_generate_v4(), (SELECT id FROM player WHERE name='Zach LaVine'), (SELECT id FROM team WHERE name='Bulls'), 24, 5, 5, 2, 1, 3, 2, 34.0, 2023, 'c3d4e5f67890abcd');
