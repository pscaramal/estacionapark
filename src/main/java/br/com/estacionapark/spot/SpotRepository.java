package br.com.estacionapark.spot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDateTime;

@Repository
public class SpotRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpotRepository.class);

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SPOT_QUERY =
            """
                    INSERT INTO spot
                           (id,
                            sector_id,
                            latitude,
                            longitude,
                            occupied)
                    VALUES (?, ?, ?, ?, ?)
                    """;

    private static final String HAS_SPOT_AVAILABLE_QUERY = """
            SELECT EXISTS (
                SELECT
                    1
                FROM sector s
                INNER JOIN spot sp on sp.sector_id = s.id
                WHERE
                    ? BETWEEN s.open_hour AND s.close_hour
                AND sp.occupied = false);
            """;

    public SpotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Spot spot) {

        logger.info("Saving spot, spot={}", spot);

        jdbcTemplate.update(
                INSERT_SPOT_QUERY,
                spot.getId(),
                spot.getSectorId(),
                spot.getLatitude(),
                spot.getLongitude(),
                spot.getOccupied());
    }

    public boolean hasAvailableSpots(LocalDateTime entryTime) {

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(
                        HAS_SPOT_AVAILABLE_QUERY,
                        Boolean.class,
                        Time.valueOf(entryTime.toLocalTime())));
    }
}
