package br.com.estacionapark.startup;

import br.com.estacionapark.shared.BaseConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GarageLoaderTest extends BaseConfig {

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
