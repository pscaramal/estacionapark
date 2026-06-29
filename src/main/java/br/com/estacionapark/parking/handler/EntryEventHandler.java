package br.com.estacionapark.parking.handler;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.common.exception.ParkingLotFullException;
import br.com.estacionapark.parking.ParkingEvent;
import br.com.estacionapark.parking.ParkingEventHandler;
import br.com.estacionapark.parking.ParkingSession;
import br.com.estacionapark.parking.repository.ParkingEventRepository;
import br.com.estacionapark.parking.repository.ParkingSessionRepository;
import br.com.estacionapark.spot.SpotRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class EntryEventHandler implements ParkingEventHandler {

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingEventRepository parkingEventRepository;
    private final SpotRepository spotRepository;

    public EntryEventHandler(ParkingSessionRepository parkingSessionRepository,
                             ParkingEventRepository parkingEventRepository,
                             SpotRepository spotRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.parkingEventRepository = parkingEventRepository;
        this.spotRepository = spotRepository;
    }

    @Override
    public EventType supports() {
        return EventType.ENTRY;
    }

    @Override
    @Transactional
    public void handle(ParkingEvent event) {

        if (!spotRepository.hasAvailableSpots(event.getEntryTime())) {
            throw new ParkingLotFullException("Parking lot is full");
        }

        UUID sessionId = UUID.randomUUID();

        ParkingSession session = ParkingSession.entry(
                sessionId,
                event.getLicensePlate(),
                event.getEntryTime());

        parkingSessionRepository.save(session);

        parkingEventRepository.saveEntryEvent(sessionId, event);
    }
}
