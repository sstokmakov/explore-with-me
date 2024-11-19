CREATE TABLE IF NOT EXISTS categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE ,
                       name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
                                            id SERIAL PRIMARY KEY,
                                            pinned BOOLEAN NOT NULL,
                                            title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
                        id BIGSERIAL PRIMARY KEY,
                        annotation VARCHAR(2000) NOT NULL,
                        category_id BIGINT NOT NULL REFERENCES categories(id),
                        confirmed_requests INTEGER,
                        created_on TIMESTAMP,
                        description varchar(7000),
                        event_date TIMESTAMP NOT NULL,
                        initiator_id BIGSERIAL NOT NULL REFERENCES users(id),
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

CREATE TABLE IF NOT EXISTS participations (
                                id SERIAL PRIMARY KEY,
                                created TIMESTAMP NOT NULL,
                                event_id BIGINT NOT NULL,
                                requester_id BIGINT NOT NULL,
                                status VARCHAR(50) NOT NULL,
                                CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
                                CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);
