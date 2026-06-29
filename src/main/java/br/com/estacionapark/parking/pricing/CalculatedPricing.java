package br.com.estacionapark.parking.pricing;

import java.math.BigDecimal;

public record CalculatedPricing (
        BigDecimal amount,
        Long minutesParked
) {
}
