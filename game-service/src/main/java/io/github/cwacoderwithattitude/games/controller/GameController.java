package io.github.cwacoderwithattitude.games.controller;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import io.github.cwacoderwithattitude.games.service.SeedDataReader;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("games")
public class GameController {
    private static final int MIN_NUMBER_OF_GAMES = 10;
    @Autowired
    GameService gameService;
    @Autowired
    SeedDataReader seedDataReader;
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GameController.class.getName());
    @Autowired
    private MeterRegistry meterRegistry;
    @Value("classpath:board_games.json")
    Resource resource; // = new ClassPathResource("rules_of_acquisiton.json");

    @GetMapping(value = "/", produces = "application/json")
    public List<Game> getGames() {
        Counter counter = Counter.builder("api_games_list")
                // .tag("title", StringUtils.isEmpty(title) ? "all" : title)
                .description("a number of GET requests to /games/ endpoint")
                .register(meterRegistry);
        counter.increment();
        return gameService.list();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Counter counter = Counter.builder("api_games_getById")
                // .tag("title", StringUtils.isEmpty(title) ? "all" : title)
                .description("a number of GET requests to /games/{id} endpoint")
                .register(meterRegistry);
        counter.increment();
        try {
            Game game = gameService.getById(Long.parseLong(id));
            if (game == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(game);

            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> update(@PathVariable Long id, @RequestBody Game game) {
        Counter counter = Counter.builder("api_games_update")
                // .tag("title", StringUtils.isEmpty(title) ? "all" : title)
                .description("a number of PUT requests to /games/{id} endpoint")
                .register(meterRegistry);
        counter.increment();
        try {
            Game updatedGame = gameService.update(id, game);
            return ResponseEntity.ok(updatedGame);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/new", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> save(@RequestBody Game game) {
        Counter counter = Counter.builder("api_games_new")
                // .tag("title", StringUtils.isEmpty(title) ? "all" : title)
                .description("a number of POST requests to /games/new endpoint")
                .register(meterRegistry);
        counter.increment();
        logger.info(null == game ? "Game is null" : "Game: " + game);
        Game savedGame = gameService.save(game);
        return ResponseEntity.ok(savedGame);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteGameById(@PathVariable Long id) {
        Counter counter = Counter.builder("api_games_deleteById")
                .description("a number of DELETE requests to /games/{id} endpoint")
                .register(meterRegistry);
        counter.increment();
        try {
            boolean deleted = gameService.deleteById(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostConstruct
    private void init() {
        try {
            if (gameService.list().size() >= MIN_NUMBER_OF_GAMES) {
                logger.info(
                        "Games already exist in the database, skipping seed data.");
                return;
            } else {
                logger.info("No games found in the database, seeding with initial data.");
                seedDataReader.readSeedData(resource);
            }
        } catch (IOException | ParseException e) {
            logger.info("Error reading seed data: " + e);
        }
    }

}
