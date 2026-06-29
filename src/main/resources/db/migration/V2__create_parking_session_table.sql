CREATE TABLE parking_session (
        id CHAR(36) NOT NULL,
        license_plate VARCHAR(10) NOT NULL,
        sector_id BIGINT NULL,
        spot_id BIGINT NULL,
        entry_time DATETIME NOT NULL,
        parked_duration INTEGER NULL,
        exit_time DATETIME NULL,
        amount DECIMAL(10,2) NULL,
        status VARCHAR(20) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     ON UPDATE CURRENT_TIMESTAMP,

        PRIMARY KEY (id),
        CONSTRAINT fk_parking_session_sector FOREIGN KEY (sector_id) REFERENCES sector(id),
        CONSTRAINT fk_parking_session_spot   FOREIGN KEY (spot_id)   REFERENCES spot(id)
);

CREATE INDEX idx_parking_session_plate  ON parking_session (license_plate);

CREATE INDEX idx_parking_session_status ON parking_session (status);

CREATE INDEX idx_parking_session_entry_time ON parking_session (entry_time);