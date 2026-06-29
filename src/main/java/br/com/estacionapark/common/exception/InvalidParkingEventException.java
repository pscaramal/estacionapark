package br.com.estacionapark.common.exception;

public class InvalidParkingEventException extends RuntimeException {
    public InvalidParkingEventException(String message) {
        super(message);
    }
}
