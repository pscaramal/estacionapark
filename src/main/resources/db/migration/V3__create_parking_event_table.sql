CREATE TABLE parking_event (
        id BIGINT NOT NULL AUTO_INCREMENT,
        parking_session_id CHAR(36) NOT NULL,
        type VARCHAR(20) NOT NULL,
        license_plate VARCHAR(10) NOT NULL,
        event_time DATETIME NOT NULL,
        latitude DECIMAL(10,6) NULL,
        longitude DECIMAL(10,6) NULL,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

        PRIMARY KEY (id),
        CONSTRAINT fk_parking_event_session FOREIGN KEY (parking_session_id) REFERENCES parking_session(id)
);

CREATE INDEX idx_parking_event_session ON parking_event (parking_session_id);

CREATE INDEX idx_parking_event_plate ON parking_event (license_plate);

CREATE INDEX idx_parking_event_time ON parking_event (event_time);