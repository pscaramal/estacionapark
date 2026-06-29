package br.com.estacionapark.api;

import br.com.estacionapark.api.request.ParkingEventRequest;
import br.com.estacionapark.parking.domain.ParkingEvent;
import br.com.estacionapark.parking.ParkingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParkingController {

    private static final Logger logger = LoggerFactory.getLogger(ParkingController.class);

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> parkingEvents(@Valid @RequestBody ParkingEventRequest request) {
        logger.info("parking event, request={}", request);

        ParkingEvent event = ParkingEvent.from(request);

        parkingService.process(event);

        return ResponseEntity.status(200).build();
    }
}
