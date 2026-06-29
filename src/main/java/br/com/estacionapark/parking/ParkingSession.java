package br.com.estacionapark.parking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingSession {

    private final UUID id;
    private final String licensePlate;
    private final Long sectorId;
    private final Long spotId;
    private final LocalDateTime entryTime;
    private final Integer parkedDuration;
    private final LocalDateTime exitTime;
    private final BigDecimal amount;
    private final ParkingSessionStatus status;

    public ParkingSession(UUID id, String licensePlate, Long sectorId, Long spotId,
                          LocalDateTime entryTime, Integer parkedDuration, LocalDateTime exitTime,
                          BigDecimal amount, ParkingSessionStatus status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.sectorId = sectorId;
        this.spotId = spotId;
        this.entryTime = entryTime;
        this.parkedDuration = parkedDuration;
        this.exitTime = exitTime;
        this.amount = amount;
        this.status = status;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public Integer getParkedDuration() {
        return parkedDuration;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ParkingSessionStatus getStatus() {
        return status;
    }

    public static ParkingSession entry(
            UUID id,
            String licensePlate,
            LocalDateTime entryTime) {

        return new ParkingSession(
                id,
                licensePlate,
                null,
                null,
                entryTime,
                null,
                null,
                BigDecimal.ZERO,
                ParkingSessionStatus.OPEN);
    }
}
