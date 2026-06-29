package br.com.estacionapark.spot;

import br.com.estacionapark.parking.domain.Coordinate;
import br.com.estacionapark.parking.domain.ParkingOccupancy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class SpotRepository {

    private static final Logger logger = LoggerFactory.getLogger(SpotRepository.class);
    private final SpotRowMapper spotRowMapper = new SpotRowMapper();

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

    private static final String FIND_SPOT_BY_COORDINATE = """
                    SELECT
                        id,
                        sector_id,
                        latitude,
                        longitude,
                        occupied
                    FROM spot
                    WHERE
                        latitude = ?
                    AND longitude = ?
            """;

    private static final String OCCUPY_SPOT_QUERY = """
            UPDATE spot set occupied = 1 where id = ?
            """;

    private static final String VACATE_SPOT_QUERY = """
            UPDATE spot set occupied = 0 where id = ?
            """;

    private static final String FIND_SPOT_BY_ID = """
                    SELECT
                        id,
                        sector_id,
                        latitude,
                        longitude,
                        occupied
                    FROM spot
                    WHERE
                        id = ?
            """;
    private static final String GET_SPOT_OCCUPANCY = """
                SELECT
                    COUNT(*) as total,
                    COALESCE(SUM(CASE WHEN occupied THEN 1 ELSE 0 END), 0) as occupied
                FROM spot;
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

    public Optional<Spot> findByCoordinates(Coordinate coordinate) {
        return jdbcTemplate.query(
                        FIND_SPOT_BY_COORDINATE,
                        spotRowMapper,
                        coordinate.latitude(),
                        coordinate.longitude())
                .stream()
                .findFirst();
    }

    public void occupy(Long id) {
        jdbcTemplate.update(OCCUPY_SPOT_QUERY,
                id);
    }

    public Optional<Spot> findById(Long spotId) {
        return jdbcTemplate.query(
                        FIND_SPOT_BY_ID,
                        spotRowMapper,
                        spotId)
                .stream()
                .findFirst();
    }

    public void vacateSpot(Long id) {
        jdbcTemplate.update(VACATE_SPOT_QUERY,
                id);
    }

    public ParkingOccupancy getOccupancy() {

        return jdbcTemplate.queryForObject(
                GET_SPOT_OCCUPANCY,
                (rs, rowNum) -> new ParkingOccupancy(
                        rs.getInt("occupied"),
                        rs.getInt("total")));
    }
}
