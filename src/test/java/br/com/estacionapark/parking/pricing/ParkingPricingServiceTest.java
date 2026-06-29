package br.com.estacionapark.parking.pricing;

import br.com.estacionapark.parking.domain.ParkingOccupancy;
import br.com.estacionapark.parking.domain.ParkingSession;
import br.com.estacionapark.parking.domain.ParkingSessionStatus;
import br.com.estacionapark.sector.Sector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingPricingServiceTest {

    private ParkingPricingService service;

    @BeforeEach
    void setUp() {
        service = new ParkingPricingService();
    }

    @Test
    @DisplayName("Deve retornar valor zero para permanência de até 30 minutos")
    void scenario01() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(10, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(30));

        assertEquals(new BigDecimal("0"), pricing.amount());
        assertEquals(30, pricing.minutesParked());
    }

    @Test
    @DisplayName("Deve cobrar uma hora ao ultrapassar 30 minutos")
    void scenario02() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(30, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(31));

        assertEquals(new BigDecimal("40.00"), pricing.amount());
        assertEquals(31, pricing.minutesParked());
    }

    @Test
    @DisplayName("Deve arredondar para duas horas quando permanência ultrapassar uma hora")
    void scenario03() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(30, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(61));

        assertEquals(new BigDecimal("80.00"), pricing.amount());
        assertEquals(61, pricing.minutesParked());
    }

    @Test
    @DisplayName("Deve aplicar desconto de 10% quando ocupação for inferior a 25%")
    void scenario04() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(24, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(31));

        assertEquals(new BigDecimal("36.000"), pricing.amount());
    }

    @Test
    @DisplayName("Deve manter o preço base quando ocupação for igual a 25%")
    void scenario05() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(25, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(31));

        assertEquals(new BigDecimal("40.00"), pricing.amount());
    }

    @Test
    @DisplayName("Deve aplicar acréscimo de 10% quando ocupação for igual a 50%")
    void scenario06() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(50, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(31));

        assertEquals(new BigDecimal("44.000"), pricing.amount());
    }

    @Test
    @DisplayName("Deve aplicar acréscimo de 25% quando ocupação for igual a 75%")
    void scenario07() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(75, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(31));

        assertEquals(new BigDecimal("50.0000"), pricing.amount());
    }

    @Test
    @DisplayName("Deve aplicar o multiplicador antes de calcular as horas")
    void scenario08() {

        ParkingSession session = parkingSession(0);
        Sector sector = sector(new BigDecimal("40.00"));
        ParkingOccupancy occupancy = new ParkingOccupancy(75, 100);

        CalculatedPricing pricing = service.calculate(
                session,
                sector,
                occupancy,
                session.entryTime().plusMinutes(121));

        assertEquals(new BigDecimal("150.0000"), pricing.amount());
    }

    private ParkingSession parkingSession(int parkedDuration) {
        return new ParkingSession(
                UUID.randomUUID(),
                "ABC1234",
                1L,
                1L,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                parkedDuration,
                null,
                null,
                ParkingSessionStatus.OPEN);
    }

    private Sector sector(BigDecimal basePrice) {

        return Sector.builder()
                .id(1L)
                .code("A")
                .basePrice(basePrice)
                .maxCapacity(10)
                .openHour(LocalTime.MIN)
                .closeHour(LocalTime.MAX)
                .durationLimitMinutes(1440)
                .build();
    }
}
