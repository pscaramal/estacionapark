package br.com.estacionapark.spot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
