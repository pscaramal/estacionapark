package br.com.estacionapark.parking.repository;

import br.com.estacionapark.parking.domain.ParkingSession;
import br.com.estacionapark.parking.domain.ParkingSessionStatus;
import br.com.estacionapark.parking.repository.rowmapper.ParkingSessionRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class ParkingSessionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ParkingSessionRepository.class);

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
    private static final String UPDATE_PARKING_SESSION_QUERY = """
            UPDATE parking_session
              SET sector_id = ?,
                  spot_id = ?,
                  parked_duration = ?,
                  exit_time = ?,
                  amount = ?,
                  status = ?
            WHERE id = ?
            """;
    private static final String CALCULATE_REVENUE = """
                SELECT
                    COALESCE(SUM(ps.amount), 0)
                FROM parking_session ps
                INNER JOIN sector s
                    ON s.id = ps.sector_id
                WHERE
                    s.code = ?
                AND DATE(ps.exit_time) = ?
                AND ps.status = ?
            """;
    public ParkingSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ParkingSession session) {

        jdbcTemplate.update(INSERT_PARKING_SESSION_QUERY,
                session.id().toString(),
                session.licensePlate(),
                session.sectorId(),
                session.spotId(),
                session.entryTime() != null? Timestamp.valueOf(session.entryTime()) : null,
                session.parkedDuration(),
                session.exitTime() != null? Timestamp.valueOf(session.exitTime()) : null,
                session.amount(),
                session.status().name());
    }

    public Optional<ParkingSession> findSessionByLicensePlateAndStatus(String licensePlate, ParkingSessionStatus status) {
        return jdbcTemplate.query(
                        FIND_PARKING_SESSION_QUERY,
                        rowMapper,
                        licensePlate,
                        status.name())
                .stream()
                .findFirst();
    }

    public void update(ParkingSession session) {
        logger.info("updating parking session, session={}", session);

        jdbcTemplate.update(
                UPDATE_PARKING_SESSION_QUERY,
                session.sectorId(),
                session.spotId(),
                session.parkedDuration(),
                session.exitTime(),
                session.amount(),
                session.status().name(),
                session.id().toString()
        );
    }

    public BigDecimal calculateRevenue(LocalDate date, String sector) {
        return jdbcTemplate.queryForObject(
                CALCULATE_REVENUE,
                BigDecimal.class,
                sector,
                Date.valueOf(date),
                ParkingSessionStatus.FINISH.name());
    }
}
