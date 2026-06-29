package br.com.estacionapark.revenue;

import br.com.estacionapark.api.response.RevenueResponse;
import br.com.estacionapark.parking.repository.ParkingSessionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RevenueService {

    private final ParkingSessionRepository parkingSessionRepository;

    public RevenueService(ParkingSessionRepository parkingSessionRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
    }

    public RevenueResponse getRevenue(LocalDate date, String sector) {
        BigDecimal amount =
                parkingSessionRepository.calculateRevenue(date, sector);

        return new RevenueResponse(
                amount,
                "BRL",
                LocalDateTime.now());
    }
}
