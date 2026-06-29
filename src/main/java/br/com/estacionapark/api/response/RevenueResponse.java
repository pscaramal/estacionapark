package br.com.estacionapark.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RevenueResponse(
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp
) {
}
