package br.com.estacionapark.sector;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SectorRowMapper implements RowMapper<Sector> {

    @Override
    public Sector mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Sector.builder()
                .id(rs.getLong("id"))
                .code(rs.getString("code"))
                .basePrice(rs.getBigDecimal("base_price"))
                .maxCapacity(rs.getInt("max_capacity"))
                .openHour(rs.getTime("open_hour").toLocalTime())
                .closeHour(rs.getTime("close_hour").toLocalTime())
                .build();
    }
}
