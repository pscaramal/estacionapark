package br.com.estacionapark.common.exception.handler;

import br.com.estacionapark.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(InvalidParkingEventException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParkingEventException(InvalidParkingEventException ex,
                                                                            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ParkingLotFullException.class)
    public ResponseEntity<ErrorResponse> handleParkingLotFullException(ParkingLotFullException ex,
                                                               HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(ParkingSessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParkingSessionNotFoundException(ParkingSessionNotFoundException ex,
                                                                               HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(SpotNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleSpotNotAvailableException(SpotNotAvailableException ex,
                                                                         HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(SectorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSectorNotFoundException(SectorNotFoundException ex,
                                                                         HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(CustomSQLException.class)
    public ResponseEntity<ErrorResponse> handleCustomSQLException(CustomSQLException ex,
                                                                       HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(500).body(error);
    }
}
