package io.github.cwacoderwithattitude.games.controller;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import io.github.cwacoderwithattitude.games.service.SeedDataReader;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("games")
public class GameController {
    private static final int MIN_NUMBER_OF_GAMES = 10;
    @Autowired
    GameService gameService;
    @Autowired
    SeedDataReader seedDataReader;
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GameController.class.getName());
    @Value("classpath:board_games.json")
    Resource resource; // = new ClassPathResource("rules_of_acquisiton.json");
    @Autowired
    private MeterRegistry meterRegistry;
    private Counter gameListCounter;
    private Counter gameByIdCounter;
    private Counter gamesUpdateCounter;
    private Counter newGameCounter;
    private Counter deleteGameCounter;

    @PostConstruct
    private void init() {
        try {
            gameListCounter = buildCounter(meterRegistry, "api_games_list", "a number of GET requests to /games/ endpoint");
            gameByIdCounter = buildCounter(meterRegistry, "api_games_getById", "a number of GET requests to /games/{id} endpoint");
            gamesUpdateCounter = buildCounter(meterRegistry, "api_games_update", "a number of PUT requests to /games/{id} endpoint");
            newGameCounter = buildCounter(meterRegistry, "api_games_new", "a number of POST requests to /games/new endpoint");
            deleteGameCounter = buildCounter(meterRegistry, "api_games_deleteById", "a number of DELETE requests to /games/{id} endpoint");
            if (gameService.list().size() >= MIN_NUMBER_OF_GAMES) {
                logger.info("Games already exist in the database, skipping seed data.");
            } else {
                logger.info("No games found in the database, seeding with initial data.");
                seedDataReader.readSeedData(resource);
            }
        } catch (IOException | ParseException e) {
            logger.info("Error reading seed data: " + e);
        }
    }

    private Counter buildCounter(MeterRegistry registry, String name, String description) {
        return Counter.builder(name)
                // .tag("title", StringUtils.isEmpty(title) ? "all" : title)
                .description(description)
                .register(registry);
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<Game> getGames() {
        gameListCounter.increment();
        return gameService.list();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        try {
            gameByIdCounter.increment();
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
        try {
            gamesUpdateCounter.increment();
            Game updatedGame = gameService.update(id, game);
            return ResponseEntity.ok(updatedGame);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/new", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> save(@RequestBody Game game) {
        try {
            newGameCounter.increment();
            logger.info(null == game ? "Game is null" : "Game: " + game);
            Game savedGame = gameService.save(game);
            return ResponseEntity.ok(savedGame);
        } catch (InvalidDataAccessResourceUsageException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteGameById(@PathVariable Long id) {
        try {
            deleteGameCounter.increment();
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

}
