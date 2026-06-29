package br.com.estacionapark.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ParkingEventRequest(
        @JsonProperty("license_plate")
        @NotBlank(message = "License Plate is required")
        String licensePlate,

        @JsonProperty("entry_time")
        LocalDateTime entryTime,

        @JsonProperty("exit_time")
        LocalDateTime exitTime,

        Double lat,

        Double lng,

        @JsonProperty("event_type")
        @NotNull(message = "Event Type is required")
        EventType eventType
) {
}
