package br.com.estacionapark.parking;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.parking.domain.ParkingEvent;

public interface ParkingEventHandler {
    EventType supports();

    void handle(ParkingEvent event);
}
