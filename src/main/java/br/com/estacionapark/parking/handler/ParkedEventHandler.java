package br.com.estacionapark.parking.handler;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.parking.ParkingEvent;
import br.com.estacionapark.parking.ParkingEventHandler;
import br.com.estacionapark.parking.repository.ParkingEventRepository;
import br.com.estacionapark.parking.repository.ParkingSessionRepository;
import org.springframework.stereotype.Component;

@Component
public class ParkedEventHandler implements ParkingEventHandler {

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingEventRepository parkingEventRepository;

    public ParkedEventHandler(ParkingSessionRepository parkingSessionRepository,
                              ParkingEventRepository parkingEventRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.parkingEventRepository = parkingEventRepository;
    }

    @Override
    public EventType supports() {
        return EventType.PARKED;
    }

    @Override
    public void handle(ParkingEvent event) {

    }
}
