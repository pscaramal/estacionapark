package br.com.estacionapark.parking.repository;

import br.com.estacionapark.parking.ParkingEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.UUID;

@Repository
public class ParkingEventRepository {

    private final JdbcTemplate jdbcTemplate;

    public ParkingEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveEntryEvent(UUID sessionId, ParkingEvent event) {
        jdbcTemplate.update(
                """
                INSERT INTO parking_event
                (
                    parking_session_id,
                    type,
                    license_plate,
                    event_time,
                    latitude,
                    longitude
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                sessionId.toString(),
                event.getType().name(),
                event.getLicensePlate(),
                Timestamp.valueOf(event.getEntryTime()),
                null,
                null);
    }
}
