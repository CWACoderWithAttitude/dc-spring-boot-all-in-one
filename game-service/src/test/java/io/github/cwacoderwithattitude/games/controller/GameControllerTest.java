package io.github.cwacoderwithattitude.games.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import io.github.cwacoderwithattitude.games.service.SeedDataReader;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@WebMvcTest(GameController.class)
@ContextConfiguration(classes = GameControllerTest.MeterRegistryConfig.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private SeedDataReader seedDataReader;

    @TestConfiguration
    static class MeterRegistryConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Test
    void getGames_shouldReturnList() throws Exception {
        Game game = new Game();
        game.setTitle("Test Game");
        Mockito.when(gameService.list()).thenReturn(Collections.singletonList(game));

        mockMvc.perform(get("/games/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"));
    }

    @Test
    void getGameById_shouldReturnGame() throws Exception {
        Game game = new Game();
        game.setTitle("Test Game");
        Mockito.when(gameService.getById(1L)).thenReturn(game);

        mockMvc.perform(get("/games/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void getGameById_shouldReturnNotFound() throws Exception {
        Mockito.when(gameService.getById(2L)).thenReturn(null);

        mockMvc.perform(get("/games/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturnUpdatedGame() throws Exception {
        Game game = new Game();
        game.setTitle("Updated Game");
        Mockito.when(gameService.update(eq(1L), any(Game.class))).thenReturn(game);

        String data = "{\"id\": 11602,\"title\": \"Die Siedler von Mittelerde, Updated2\"}";

        mockMvc.perform(put("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Game"));
    }

    /*
     * @Test
     * void update_shouldReturnNotFound() throws Exception {
     * Mockito.when(gameService.update(eq(2L), any(Game.class))).thenReturn(null);
     *
     * mockMvc.perform(put("/games/2")
     * .contentType(MediaType.APPLICATION_JSON)
     * .content("{\"title\":\"Not Found\"}"))
     * .andExpect(status().isNotFound());
     * }
     */
    @Test
    void save_shouldReturnSavedGame() throws Exception {
        Game game = new Game();
        game.setTitle("New Game");
        Mockito.when(gameService.save(any(Game.class))).thenReturn(game);

        mockMvc.perform(post("/games/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Game\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Game"));
    }
    /*
     * @Test
     * void deleteGameById_shouldReturnNoContent() throws Exception {
     * Mockito.when(gameService.deleteById(1L)).thenReturn(true);
     *
     * mockMvc.perform(delete("/games/1"))
     * .andExpect(status().isNoContent());
     * }
     *
     * @Test
     * void deleteGameById_shouldReturnNotFound() throws Exception {
     * Mockito.when(gameService.deleteById(2L)).thenReturn(false);
     *
     * mockMvc.perform(delete("/games/2"))
     * .andExpect(status().isNotFound());
     * }
     */
}