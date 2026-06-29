package br.com.estacionapark.common.exception;

public class CustomSQLException extends RuntimeException {
    public CustomSQLException(String message) {
        super(message);
    }
}
