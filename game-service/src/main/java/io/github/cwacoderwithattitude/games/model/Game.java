package io.github.cwacoderwithattitude.games.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Game {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Column(length = 13)
    private String ean13;
    private String publisher;
    private String genre;
    private Integer min_number_of_players;
    private Integer max_number_of_players;
    private Integer min_age;
    private String typical_duration;

    public Game() {
    }

    public Game(String title) {
        this.title = title;
    }
}
