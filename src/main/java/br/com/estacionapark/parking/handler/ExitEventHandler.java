package br.com.estacionapark.parking.handler;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.parking.ParkingEvent;
import br.com.estacionapark.parking.ParkingEventHandler;
import org.springframework.stereotype.Component;

@Component
public class ExitEventHandler implements ParkingEventHandler {

    @Override
    public EventType supports() {
        return EventType.EXIT;
    }

    @Override
    public void handle(ParkingEvent event) {

    }
}
