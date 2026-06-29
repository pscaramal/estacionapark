package br.com.estacionapark.parking;

import br.com.estacionapark.api.request.EventType;
import br.com.estacionapark.api.request.ParkingEventRequest;
import br.com.estacionapark.common.exception.InvalidParkingEventException;

import java.time.LocalDateTime;

public class ParkingEvent {
    private final String licensePlate;
    private final EventType type;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final Coordinate coordinate;

    private ParkingEvent(
            String licensePlate,
            EventType type,
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            Coordinate coordinate) {

        this.licensePlate = licensePlate;
        this.type = type;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.coordinate = coordinate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public EventType getType() {
        return type;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public static ParkingEvent from(ParkingEventRequest request) {

        switch (request.eventType()) {

            case ENTRY -> {

                if (request.entryTime() == null) {
                    throw new InvalidParkingEventException("Entry Time is required");
                }

                return new ParkingEvent(
                        request.licensePlate(),
                        EventType.ENTRY,
                        request.entryTime(),
                        null,
                        null);
            }

            case PARKED -> {

                if (request.lat() == null) {
                    throw new InvalidParkingEventException("Latitude is required");
                }

                if (request.lng() == null) {
                    throw new InvalidParkingEventException("Longitude is required");
                }

                return new ParkingEvent(
                        request.licensePlate(),
                        EventType.PARKED,
                        null,
                        null,
                        new Coordinate(request.lat(), request.lng()));
            }

            case EXIT -> {

                if (request.exitTime() == null) {
                    throw new InvalidParkingEventException("Exit Time is required");
                }

                return new ParkingEvent(
                        request.licensePlate(),
                        EventType.EXIT,
                        null,
                        request.exitTime(),
                        null);
            }
        }

        throw new IllegalStateException();
    }
}
