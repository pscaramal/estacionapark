package br.com.estacionapark.garage.client.dto;

public record SpotResponse(
        Long id,
        String sector,
        Double lat,
        Double lng,
        boolean occupied
) {
}
