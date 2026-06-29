package br.com.estacionapark.startup;

import br.com.estacionapark.shared.BaseConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GarageLoaderTest {

    static WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());

    static {
        wireMockServer.start();
        wireMockServer.stubFor(get("/garage")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "garage": [
                                        {
                                            "sector": "A",
                                            "base_price": 40.5,
                                            "max_capacity": 3,
                                            "open_hour": "00:00",
                                            "close_hour": "23:59",
                                            "duration_limit_minutes": 1440
                                        }
                                    ],
                                    "spots": [
                                        {
                                            "id": 1,
                                            "sector": "A",
                                            "lat": -23.561684,
                                            "lng": -46.655981,
                                            "occupied": false
                                        },
                                        {
                                            "id": 2,
                                            "sector": "A",
                                            "lat": -23.561664,
                                            "lng": -46.655961,
                                            "occupied": false
                                        },
                                        {
                                            "id": 3,
                                            "sector": "A",
                                            "lat": -23.561644,
                                            "lng": -46.655941,
                                            "occupied": false
                                        }
                                    ]
                                }
                                """)));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("garage.client.base.url",
                () -> wireMockServer.baseUrl());
    }

    @AfterAll
    static void clean() {
        wireMockServer.stop();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Deve carregar a configuração inicial da garagem corretamente")
    void scenario01() {
        Integer countSector = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sector",
                Integer.class
        );
        assertEquals(1, countSector);

        Integer countSpot = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM spot",
                Integer.class
        );

        assertEquals(3, countSpot);
    }
}
