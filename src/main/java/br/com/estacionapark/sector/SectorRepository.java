package br.com.estacionapark.sector;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;

@Repository
public class SectorRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SECTOR_QUERY =
            """
                    INSERT INTO sector
                           (code,
                            base_price,
                            max_capacity,
                            open_hour,
                            close_hour,
                            duration_limit_minutes)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

    public SectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Sector sector) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(
                    INSERT_SECTOR_QUERY,
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, sector.getCode());
            ps.setBigDecimal(2, sector.getBasePrice());
            ps.setInt(3, sector.getMaxCapacity());
            ps.setTime(4, Time.valueOf(sector.getOpenHour()));
            ps.setTime(5, Time.valueOf(sector.getCloseHour()));
            ps.setInt(6, sector.getDurationLimitMinutes());

            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
