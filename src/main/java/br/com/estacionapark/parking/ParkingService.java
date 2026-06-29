package br.com.estacionapark.parking;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.parking.domain.ParkingEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final Map<EventType, ParkingEventHandler> handlers;

    public ParkingService(List<ParkingEventHandler> handlers) {

        this.handlers = handlers.stream()
                .collect(Collectors.toUnmodifiableMap(ParkingEventHandler::supports, Function.identity()));
    }

    public void process(ParkingEvent event) {
        ParkingEventHandler handler = handlers.get(event.getType());

        if (handler == null) {
            throw new IllegalArgumentException(
                    "Unsupported event type: " + event.getType());
        }

        handler.handle(event);
    }
}
