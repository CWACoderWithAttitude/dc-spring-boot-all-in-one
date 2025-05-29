package io.github.cwacoderwithattitude.games.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.repo.GameRepository;
import jakarta.annotation.PostConstruct;

@Service
public class GameService {

    Logger logger = Logger.getLogger(GameService.class.getName());

    @PostConstruct
    private void initMetrics() {
        // Initialize any metrics here if needed
        // For example, you can create counters or gauges

    }

    @Autowired
    private GameRepository gameRepository;

    public List<Game> list() {
        return gameRepository.findAll();
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Game update(Long id, Game game) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        if (gameOptional.isEmpty()) {
            return null;
        }
        Game existingGame = gameOptional.get();
        existingGame.setTitle(game.getTitle());
        return gameRepository.save(existingGame);
    }

    public Game getById(Long id) {
        logger.info("Game ID: " + id);
        Optional<Game> gameptional = gameRepository.findById(id);
        logger.info("Game Optional: " + gameptional);
        if (gameptional.isEmpty()) {
            logger.info("Game not found");
            return null;
        } else {
            return gameptional.get();
        }
    }

    public boolean deleteById(Long id) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        if (gameOptional.isPresent()) {
            gameRepository.deleteById(id);
            logger.info("Deleted game with ID: " + id);
            return true;
        } else {
            logger.info("Game with ID " + id + " not found for deletion.");
            return false;
        }
    }

}
