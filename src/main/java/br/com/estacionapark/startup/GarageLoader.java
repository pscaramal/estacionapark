package br.com.estacionapark.startup;

import br.com.estacionapark.garage.GarageImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GarageLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(GarageLoader.class);

    private final GarageImportService garageImportService;

    public GarageLoader(GarageImportService garageImportService) {
        this.garageImportService = garageImportService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Getting garage configuration");

        try {
            garageImportService.importGarageConfiguration();
        } catch (Exception e) {
            logger.error("Unexpected error during garage initialization. errorMessage={}",e.getMessage(), e);
        }
    }
}
