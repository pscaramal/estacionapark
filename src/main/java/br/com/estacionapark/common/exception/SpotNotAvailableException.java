package br.com.estacionapark.common.exception;

public class SpotNotAvailableException extends RuntimeException {
    public SpotNotAvailableException(String message) {
        super(message);
    }
}
