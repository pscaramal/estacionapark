package br.com.estacionapark.garage.mapper;

import br.com.estacionapark.garage.client.dto.SectorResponse;
import br.com.estacionapark.garage.client.dto.SpotResponse;
import br.com.estacionapark.sector.Sector;
import br.com.estacionapark.spot.Spot;
import org.springframework.stereotype.Component;

@Component
public class GarageMapper {

    public Sector toSector(SectorResponse dto) {
        return Sector.builder()
                .code(dto.sector())
                .basePrice(dto.basePrice())
                .maxCapacity(dto.maxCapacity())
                .openHour(dto.openHour())
                .closeHour(dto.closeHour())
                .durationLimitMinutes(dto.durationLimitMinutes())
                .build();
    }

    public Spot toSpot(SpotResponse dto, Long sectorId) {
        return Spot.builder()
                .id(dto.id())
                .sectorId(sectorId)
                .latitude(dto.lat())
                .longitude(dto.lng())
                .occupied(dto.occupied())
                .build();
    }
}
