CREATE TABLE IF NOT EXISTS categories (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
                                            id SERIAL PRIMARY KEY,
                                            pinned BOOLEAN NOT NULL,
                                            title VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
                        id SERIAL PRIMARY KEY,
                        annotation VARCHAR(255) NOT NULL,
                        category_id INTEGER NOT NULL REFERENCES categories(id),
                        confirmed_requests INTEGER,
                        created_on TIMESTAMP,
                        description TEXT,
                        event_date TIMESTAMP NOT NULL,
                        initiator_id INTEGER NOT NULL REFERENCES users(id),
                        lat DOUBLE PRECISION NOT NULL,
                        lon DOUBLE PRECISION NOT NULL,
                        paid BOOLEAN NOT NULL,
                        participant_limit INTEGER,
                        published_on TIMESTAMP,
                        request_moderation BOOLEAN,
                        state VARCHAR(50),
                        title VARCHAR(255) NOT NULL,
                        views INTEGER
);

CREATE TABLE IF NOT EXISTS compilation_events (
                                                  compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE,
                                                  event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
                                                  PRIMARY KEY (compilation_id, event_id)
);