package br.com.estacionapark.garage.client;

import br.com.estacionapark.garage.client.dto.GarageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GarageClient {

    private final RestClient garageRestClient;

    public GarageClient(RestClient garageRestClient) {
        this.garageRestClient = garageRestClient;
    }

    public GarageResponse loadGarage() {

        return garageRestClient.get()
                .uri("/garage")
                .retrieve()
                .body(GarageResponse.class);
    }
}
