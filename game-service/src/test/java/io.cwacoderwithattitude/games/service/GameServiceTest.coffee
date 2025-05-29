import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.repo.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Java
package io.github.cwacoderwithattitude.games.service;


class GameServiceTest {

    private GameRepository gameRepository;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameRepository = Mockito.mock(GameRepository.class);
        gameService = new GameService();
        // Use reflection to inject the mock since @Autowired is not processed in plain unit tests
        try {
            java.lang.reflect.Field repoField = GameService.class.getDeclaredField("gameRepository");
            repoField.setAccessible(true);
            repoField.set(gameService, gameRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testListReturnsAllGames() {
        Game game1 = new Game();
        Game game2 = new Game();
        List<Game> mockGames = Arrays.asList(game1, game2);

        when(gameRepository.findAll()).thenReturn(mockGames);

        List<Game> result = gameService.list();

        assertEquals(mockGames, result);
        verify(gameRepository, times(1)).findAll();
    }
}