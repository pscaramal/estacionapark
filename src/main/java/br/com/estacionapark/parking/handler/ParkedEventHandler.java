package br.com.estacionapark.parking.handler;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.common.exception.ParkingSessionNotFoundException;
import br.com.estacionapark.common.exception.SpotNotAvailableException;
import br.com.estacionapark.parking.domain.ParkingEvent;
import br.com.estacionapark.parking.ParkingEventHandler;
import br.com.estacionapark.parking.domain.ParkingSession;
import br.com.estacionapark.parking.domain.ParkingSessionStatus;
import br.com.estacionapark.parking.repository.ParkingEventRepository;
import br.com.estacionapark.parking.repository.ParkingSessionRepository;
import br.com.estacionapark.spot.Spot;
import br.com.estacionapark.spot.SpotRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ParkedEventHandler implements ParkingEventHandler {

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingEventRepository parkingEventRepository;
    private final SpotRepository spotRepository;

    public ParkedEventHandler(ParkingSessionRepository parkingSessionRepository,
                              ParkingEventRepository parkingEventRepository,
                              SpotRepository spotRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.parkingEventRepository = parkingEventRepository;
        this.spotRepository = spotRepository;
    }

    @Override
    public EventType supports() {
        return EventType.PARKED;
    }

    @Override
    @Transactional
    public void handle(ParkingEvent event) {
        ParkingSession session = parkingSessionRepository
                .findSessionByLicensePlateAndStatus(event.getLicensePlate(), ParkingSessionStatus.OPEN)
                .orElseThrow(() -> new ParkingSessionNotFoundException("Parking Session not found"));

        Spot spot = spotRepository.findByCoordinates(event.getCoordinate())
                .orElseThrow(() -> new SpotNotAvailableException(
                        String.format("Spot in coordinate latitude=%s, longitude=%s not found",
                        event.getCoordinate().latitude(), event.getCoordinate().longitude())));

        if (spot.isOcuppied()) {
            throw new SpotNotAvailableException(String.format("Spot in coordinate latitude=%s, longitude=%s is occupied",
                    event.getCoordinate().latitude(), event.getCoordinate().longitude()));
        }

        ParkingSession parkedSession = session.park(spot.getId(), spot.getSectorId());

        parkingSessionRepository.update(parkedSession);

        spotRepository.occupy(spot.getId());

        parkingEventRepository.save(parkedSession.id(), event);
    }
}
