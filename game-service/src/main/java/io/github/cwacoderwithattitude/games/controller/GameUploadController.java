package io.github.cwacoderwithattitude.games.controller;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/games/upload")
public class GameUploadController {

    @Autowired
    private GameService gameService;

    private final Logger logger = Logger.getLogger(GameUploadController.class.getName());

    @PostMapping(consumes = { "multipart/form-data", "application/json" })
    public ResponseEntity<String> uploadGames(
            // @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody(required = false) List<Game> gamesFromBody) {

        List<Game> games;

        if (gamesFromBody != null) {
            games = gamesFromBody;
        } else {
            return ResponseEntity.badRequest().body("No file or JSON body provided.");
        }
        games.forEach(gameService::save);
        logger.info("Uploaded and saved " + games.size() + " games.");
        return ResponseEntity.ok("Successfully uploaded and saved " + games.size() + " games.");

    }
}