package br.com.estacionapark.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${garage.client.base.url}")
    private String garageClientBaseUrl;

    @Bean
    RestClient garageRestClient() {
        return RestClient.builder()
                .baseUrl(garageClientBaseUrl)
                .build();
    }
}
