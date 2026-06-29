package br.com.estacionapark.spot;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpotRowMapper implements RowMapper<Spot> {
    @Override
    public Spot mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Spot.builder()
                .id(rs.getLong("id"))
                .sectorId(rs.getLong("sector_id"))
                .longitude(rs.getDouble("longitude"))
                .latitude(rs.getDouble("latitude"))
                .occupied(rs.getBoolean("occupied"))
                .build();
    }
}
