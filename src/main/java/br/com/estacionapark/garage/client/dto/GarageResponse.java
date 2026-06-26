package br.com.estacionapark.garage.client.dto;

import java.util.List;

public record GarageResponse(
        List<SectorResponse> garage,
        List<SpotResponse> spots
) {
}
