package br.com.estacionapark.parking.handler;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.common.exception.ParkingSessionNotFoundException;
import br.com.estacionapark.common.exception.SectorNotFoundException;
import br.com.estacionapark.common.exception.SpotNotAvailableException;
import br.com.estacionapark.parking.domain.ParkingEvent;
import br.com.estacionapark.parking.ParkingEventHandler;
import br.com.estacionapark.parking.domain.ParkingOccupancy;
import br.com.estacionapark.parking.domain.ParkingSession;
import br.com.estacionapark.parking.domain.ParkingSessionStatus;
import br.com.estacionapark.parking.pricing.CalculatedPricing;
import br.com.estacionapark.parking.pricing.ParkingPricingService;
import br.com.estacionapark.parking.repository.ParkingEventRepository;
import br.com.estacionapark.parking.repository.ParkingSessionRepository;
import br.com.estacionapark.sector.Sector;
import br.com.estacionapark.sector.SectorRepository;
import br.com.estacionapark.spot.Spot;
import br.com.estacionapark.spot.SpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExitEventHandler implements ParkingEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExitEventHandler.class);

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingEventRepository parkingEventRepository;
    private final SpotRepository spotRepository;
    private final SectorRepository sectorRepository;
    private final ParkingPricingService parkingPricingService;

    public ExitEventHandler(ParkingSessionRepository parkingSessionRepository,
                            ParkingEventRepository parkingEventRepository,
                            SpotRepository spotRepository,
                            SectorRepository sectorRepository,
                            ParkingPricingService parkingPricingService) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.parkingEventRepository = parkingEventRepository;
        this.spotRepository = spotRepository;
        this.sectorRepository = sectorRepository;
        this.parkingPricingService = parkingPricingService;
    }

    @Override
    public EventType supports() {
        return EventType.EXIT;
    }

    @Override
    @Transactional
    public void handle(ParkingEvent event) {
        logger.info("handling exit event, event={}", event);

        ParkingSession session = parkingSessionRepository
                .findSessionByLicensePlateAndStatus(event.getLicensePlate(), ParkingSessionStatus.PARKED)
                .orElseThrow(() -> new ParkingSessionNotFoundException("Parking Session not found"));

        Spot spot = spotRepository.findById(session.spotId())
                .orElseThrow(() -> new SpotNotAvailableException(String.format("Spot with id %d not found", session.spotId())));

        Sector sector = sectorRepository.findById(session.sectorId())
                .orElseThrow(() -> new SectorNotFoundException(String.format("Sector with id %d not found", session.sectorId())));

        ParkingOccupancy parkingOccupancy = spotRepository.getOccupancy();

        CalculatedPricing calculatedPricing = parkingPricingService.calculate(session, sector, parkingOccupancy, event.getExitTime());

        ParkingSession sessionFinished = session.finish(event.getExitTime(), calculatedPricing);

        parkingSessionRepository.update(sessionFinished);

        spotRepository.vacateSpot(spot.getId());

        parkingEventRepository.save(session.id(), event);
    }
}
