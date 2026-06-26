CREATE TABLE sector (
    id BIGINT              PRIMARY KEY AUTO_INCREMENT,
    code                   VARCHAR(10) NOT NULL UNIQUE,
    base_price             DECIMAL(10,2) NOT NULL,
    max_capacity           INTEGER NOT NULL,
    open_hour              TIME NOT NULL,
    close_hour             TIME NOT NULL,
    duration_limit_minutes INTEGER NOT NULL
);

CREATE TABLE spot (
    id                     BIGINT PRIMARY KEY,
    sector_id              BIGINT NOT NULL,
    latitude               DECIMAL(10,6) NOT NULL,
    longitude              DECIMAL(10,6) NOT NULL,
    occupied               BOOLEAN NOT NULL,
    CONSTRAINT fk_spot_sector FOREIGN KEY (sector_id) REFERENCES sector(id)
);