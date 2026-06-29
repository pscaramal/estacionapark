package br.com.estacionapark.parking.repository;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.common.exception.CustomSQLException;
import br.com.estacionapark.parking.domain.ParkingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class ParkingEventRepository {

    private static final Logger logger = LoggerFactory.getLogger(ParkingEventRepository.class);

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_PARKING_EVENT_QUERY = """
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
            """;
    public ParkingEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(UUID sessionId, ParkingEvent event) {
        LocalDateTime eventTime = LocalDateTime.now();

        if (event.getType().equals(EventType.ENTRY)) {
            eventTime = event.getEntryTime();
        }

        if (event.getType().equals(EventType.EXIT)) {
            eventTime = event.getExitTime();
        }

        try {
            jdbcTemplate.update(
                    INSERT_PARKING_EVENT_QUERY,
                    sessionId.toString(),
                    event.getType().name(),
                    event.getLicensePlate(),
                    Timestamp.valueOf(eventTime),
                    event.getCoordinate() != null? event.getCoordinate().latitude() : null,
                    event.getCoordinate() != null? event.getCoordinate().longitude() : null);
        } catch (Exception e ) {
            logger.error("Unexpected sql error. errorMessage={}", e.getMessage(), e);
            throw new CustomSQLException("Unexpected sql error");
        }
    }
}
