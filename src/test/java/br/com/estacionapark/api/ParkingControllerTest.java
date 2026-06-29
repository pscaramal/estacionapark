package br.com.estacionapark.api;

import br.com.estacionapark.shared.BaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ParkingControllerTest extends BaseConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {

        jdbcTemplate.update("DELETE FROM parking_event");
        jdbcTemplate.update("DELETE FROM parking_session");

        jdbcTemplate.update("UPDATE spot SET occupied = false");
    }

    @Test
    @DisplayName("Deve registrar evento de entrada")
    void scenario01() throws Exception {

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "license_plate": "ABC1234",
                                  "entry_time": "2025-01-01T10:00:00",
                                  "event_type": "ENTRY"
                                }
                                """))
                .andExpect(status().isOk());

        assertParkingSessionCreated();

        assertParkingEventCreated("ENTRY");
    }

    @Test
    @DisplayName("Deve registrar evento de estacionamento")
    void scenario02() throws Exception {

        createEntry();

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "license_plate": "ABC1234",
                                  "lat": -23.561684,
                                  "lng": -46.655981,
                                  "event_type": "PARKED"
                                }
                                """))
                .andExpect(status().isOk());

        assertSpotOccupied(1L);

        assertParkingSessionParked();

        assertParkingEventCreated("PARKED");
    }

    @Test
    @DisplayName("Deve registrar evento de saída")
    void scenario03() throws Exception {

        createEntry();
        createParked();

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "license_plate": "ABC1234",
                                  "exit_time": "2025-01-01T11:15:00",
                                  "event_type": "EXIT"
                                }
                                """))
                .andExpect(status().isOk());

        assertSpotReleased(1L);

        assertParkingSessionFinished();

        assertParkingEventCreated("EXIT");
    }

    private void createEntry() throws Exception {

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "license_plate": "ABC1234",
                                  "entry_time": "2025-01-01T10:00:00",
                                  "event_type": "ENTRY"
                                }
                                """))
                .andExpect(status().isOk());
    }

    private void createParked() throws Exception {

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "license_plate": "ABC1234",
                                  "lat": -23.561684,
                                  "lng": -46.655981,
                                  "event_type": "PARKED"
                                }
                                """))
                .andExpect(status().isOk());
    }

    private void assertParkingSessionCreated() {

        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM parking_session
                WHERE license_plate = 'ABC1234'
                """, Integer.class);

        assertThat(count).isEqualTo(1);

        String status = jdbcTemplate.queryForObject("""
                SELECT status
                FROM parking_session
                WHERE license_plate = 'ABC1234'
                """, String.class);

        assertThat(status).isEqualTo("OPEN");
    }

    private void assertParkingSessionParked() {

        String status = jdbcTemplate.queryForObject("""
                SELECT status
                FROM parking_session
                WHERE license_plate = 'ABC1234'
                """, String.class);

        assertThat(status).isEqualTo("PARKED");

        Long spotId = jdbcTemplate.queryForObject("""
                SELECT spot_id
                FROM parking_session
                WHERE license_plate = 'ABC1234'
                """, Long.class);

        assertThat(spotId).isEqualTo(1L);
    }

    private void assertParkingSessionFinished() {

        String status = jdbcTemplate.queryForObject("""
                SELECT status
                FROM parking_session
                WHERE license_plate = 'ABC1234'
                """, String.class);

        assertThat(status).isEqualTo("FINISH");

        BigDecimal amount = jdbcTemplate.queryForObject("""
                SELECT amount
                FROM parking_session
                WHERE license_plate = 'ABC1234'
                """, BigDecimal.class);

        assertThat(amount).isNotNull();
    }

    private void assertParkingEventCreated(String type) {

        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM parking_event
                WHERE license_plate = ?
                  AND type = ?
                """, Integer.class,
                "ABC1234",
                type);

        assertThat(count).isEqualTo(1);
    }

    private void assertSpotOccupied(Long id) {

        Boolean occupied = jdbcTemplate.queryForObject("""
                SELECT occupied
                FROM spot
                WHERE id = ?
                """,
                Boolean.class,
                id);

        assertThat(occupied).isTrue();
    }

    private void assertSpotReleased(Long id) {

        Boolean occupied = jdbcTemplate.queryForObject("""
                SELECT occupied
                FROM spot
                WHERE id = ?
                """,
                Boolean.class,
                id);

        assertThat(occupied).isFalse();
    }
}
