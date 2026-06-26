package br.com.estacionapark.garage;

import br.com.estacionapark.garage.client.GarageClient;
import br.com.estacionapark.garage.client.dto.GarageResponse;
import br.com.estacionapark.garage.client.dto.SectorResponse;
import br.com.estacionapark.garage.client.dto.SpotResponse;
import br.com.estacionapark.garage.mapper.GarageMapper;
import br.com.estacionapark.sector.Sector;
import br.com.estacionapark.sector.SectorRepository;
import br.com.estacionapark.spot.Spot;
import br.com.estacionapark.spot.SpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class GarageImportService {

    private static final Logger logger = LoggerFactory.getLogger(GarageImportService.class);

    private final GarageClient garageClient;
    private final GarageMapper garageMapper;
    private final SectorRepository sectorRepository;
    private final SpotRepository spotRepository;

    public GarageImportService(GarageClient garageClient,
                               GarageMapper garageMapper,
                               SectorRepository sectorRepository,
                               SpotRepository spotRepository) {
        this.garageClient = garageClient;
        this.garageMapper = garageMapper;
        this.sectorRepository = sectorRepository;
        this.spotRepository = spotRepository;
    }

    public void importGarageConfiguration() {
        GarageResponse garage = garageClient.loadGarage();

        logger.info("garage configuration, response={}", garage);

        Map<String, Long> sectors = saveSectors(garage);

        saveSpots(garage, sectors);
    }

    private Map<String, Long> saveSectors(GarageResponse garage) {

        Map<String, Long> sectors = new HashMap<>();

        for (SectorResponse response : garage.garage()) {

            Sector sector = garageMapper.toSector(response);

            Long id = sectorRepository.save(sector);

            sectors.put(sector.getCode(), id);
        }

        return sectors;
    }

    private void saveSpots(GarageResponse garage, Map<String, Long> sectors) {

        for (SpotResponse spotResponse : garage.spots()) {

            Spot spot = garageMapper.toSpot(
                    spotResponse,
                    sectors.get(spotResponse.sector()));

            spotRepository.save(spot);
        }
    }
}
