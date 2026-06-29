package br.com.estacionapark.parking.repository;

import br.com.estacionapark.parking.ParkingSession;
import br.com.estacionapark.parking.ParkingSessionStatus;
import br.com.estacionapark.parking.repository.rowmapper.ParkingSessionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class ParkingSessionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ParkingSession> rowMapper = new ParkingSessionRowMapper();

    private static final String INSERT_PARKING_SESSION_QUERY = """
            INSERT INTO parking_session
                (
                    id,
                    license_plate,
                    sector_id,
                    spot_id,
                    entry_time,
                    parked_duration,
                    exit_time,
                    amount,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String FIND_PARKING_SESSION_QUERY = """
                SELECT
                    id,
                    license_plate,
                    sector_id,
                    spot_id,
                    entry_time,
                    parked_duration,
                    exit_time,
                    amount,
                    status
                FROM parking_session
                WHERE
                    license_plate = ?
                AND status = ?
            """;

    public ParkingSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ParkingSession session) {

        jdbcTemplate.update(INSERT_PARKING_SESSION_QUERY,
                session.getId().toString(),
                session.getLicensePlate(),
                session.getSectorId(),
                session.getSpotId(),
                session.getEntryTime() != null? Timestamp.valueOf(session.getEntryTime()) : null,
                session.getParkedDuration(),
                session.getExitTime() != null? Timestamp.valueOf(session.getEntryTime()) : null,
                session.getAmount(),
                session.getStatus().name());
    }

    public Optional<ParkingSession> findOpenByLicensePlate(String licensePlate) {
        return jdbcTemplate.query(
                        FIND_PARKING_SESSION_QUERY,
                        rowMapper,
                        licensePlate,
                        ParkingSessionStatus.OPEN.name())
                .stream()
                .findFirst();
    }
}
