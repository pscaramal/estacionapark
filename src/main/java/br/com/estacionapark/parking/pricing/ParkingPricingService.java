package br.com.estacionapark.parking.pricing;

import br.com.estacionapark.parking.domain.ParkingOccupancy;
import br.com.estacionapark.parking.domain.ParkingSession;
import br.com.estacionapark.sector.Sector;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingPricingService {

    public CalculatedPricing calculate(
            ParkingSession session,
            Sector sector,
            ParkingOccupancy occupancy,
            LocalDateTime exitTime) {

        Duration duration = Duration.between(
                session.entryTime(),
                exitTime);

        if (duration.toMinutes() <= 30) {
            return new CalculatedPricing(BigDecimal.ZERO, duration.toMinutes());
        }

        long hours = (long) Math.ceil(duration.toMinutes() / 60.0);

        BigDecimal hourlyPrice = applyOccupancyRate(
                sector.getBasePrice(),
                occupancy.percentage());

        BigDecimal finalAmount = hourlyPrice.multiply(BigDecimal.valueOf(hours));

        return new CalculatedPricing(finalAmount, duration.toMinutes());
    }

    private BigDecimal applyOccupancyRate(
            BigDecimal basePrice,
            int occupancy) {

        if (occupancy < 25) {
            return basePrice.multiply(BigDecimal.valueOf(0.90));
        }

        if (occupancy < 50) {
            return basePrice;
        }

        if (occupancy < 75) {
            return basePrice.multiply(BigDecimal.valueOf(1.10));
        }

        return basePrice.multiply(BigDecimal.valueOf(1.25));
    }

}
