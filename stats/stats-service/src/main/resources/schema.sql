CREATE TABLE IF NOT EXISTS hits
(
    id SERIAL PRIMARY KEY,
    app VARCHAR(50),
    uri VARCHAR(255),
    ip VARCHAR(45),
    timestamp timestamp
);