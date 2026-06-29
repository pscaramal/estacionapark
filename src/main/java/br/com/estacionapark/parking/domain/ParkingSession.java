package br.com.estacionapark.parking.domain;

import br.com.estacionapark.parking.pricing.CalculatedPricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ParkingSession(UUID id, String licensePlate, Long sectorId, Long spotId, LocalDateTime entryTime,
                             Integer parkedDuration, LocalDateTime exitTime, BigDecimal amount,
                             ParkingSessionStatus status) {

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

    public ParkingSession park(Long spotId, Long sectorId) {
        return new ParkingSession(
                this.id,
                this.licensePlate,
                sectorId,
                spotId,
                this.entryTime,
                this.parkedDuration,
                this.exitTime,
                this.amount,
                ParkingSessionStatus.PARKED
        );
    }

    public ParkingSession finish(LocalDateTime exitTime, CalculatedPricing calculatedPricing) {
        return new ParkingSession(
                this.id,
                this.licensePlate,
                this.sectorId,
                this.spotId,
                this.entryTime,
                calculatedPricing.minutesParked().intValue(),
                exitTime,
                calculatedPricing.amount(),
                ParkingSessionStatus.FINISH
        );    }
}
