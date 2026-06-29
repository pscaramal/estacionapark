package br.com.estacionapark.common.exception;

public class ParkingSessionNotFoundException extends RuntimeException {
    public ParkingSessionNotFoundException(String message) {
        super(message);
    }
}
