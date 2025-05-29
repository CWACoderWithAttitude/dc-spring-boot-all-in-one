package io.github.cwacoderwithattitude.games.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.cwacoderwithattitude.games.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // This class is intentionally left empty.
    // It extends JpaRepository to provide CRUD operations for Game entities.
    // Additional custom query methods can be defined here if needed.

}
