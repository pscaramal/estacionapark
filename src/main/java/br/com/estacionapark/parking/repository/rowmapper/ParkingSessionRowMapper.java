package br.com.estacionapark.parking.repository.rowmapper;

import br.com.estacionapark.parking.ParkingSession;
import br.com.estacionapark.parking.ParkingSessionStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingSessionRowMapper implements RowMapper<ParkingSession> {

    @Override
    public ParkingSession mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ParkingSession(
                UUID.fromString(rs.getString("id")),
                rs.getString("license_plate"),
                getLong(rs, "sector_id"),
                getLong(rs, "spot_id"),
                rs.getTimestamp("entry_time").toLocalDateTime(),
                getInteger(rs, "parked_duration"),
                getLocalDateTime(rs, "exit_time"),
                rs.getBigDecimal("amount"),
                ParkingSessionStatus.valueOf(rs.getString("status"))
        );
    }

    private static Long getLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

    private static Integer getInteger(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private static LocalDateTime getLocalDateTime(ResultSet rs, String column) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
