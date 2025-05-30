package io.github.cwacoderwithattitude.games.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import io.github.cwacoderwithattitude.games.service.SeedDataReader;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GameControllerRestClientTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private SeedDataReader seedDataReader;

    @MockitoBean
    private MeterRegistry meterRegistry;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/games";
    }

    // @Test
    void update_shouldReturnUpdatedGame() {
        // Arrange
        Game inputGame = new Game();
        inputGame.setTitle("Updated Game");
        Game updatedGame = new Game();
        updatedGame.setTitle("Updated Game");

        Mockito.when(gameService.update(Mockito.eq(1L), Mockito.any(Game.class)))
                .thenReturn(updatedGame);

        RestClient restClient = restClientBuilder.build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Game> requestEntity = new HttpEntity<>(inputGame, headers);

        // Act
        ResponseEntity<Game> response = restClient.exchange(
                baseUrl + "/1",
                HttpMethod.PUT,
                requestEntity,
                Game.class);
        // ResponseEntity<Game> response = restClient.exchange(
        // baseUrl + "/1",
        // HttpMethod.PUT,
        // requestEntity,
        // Game.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Game");
    }
}