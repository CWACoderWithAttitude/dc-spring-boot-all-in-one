package io.github.cwacoderwithattitude.games.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameUploadController.class)
class GameUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Test
    void uploadGames_withJsonBody_shouldReturnOk() throws Exception {
        Game game = new Game();
        game.setTitle("Test Game");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Arrays.asList(game));

        mockMvc.perform(post("/games/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Successfully uploaded")));
    }

    @Test
    void uploadGames_withNoInput_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/games/upload"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No file or JSON body provided")));
    }
}