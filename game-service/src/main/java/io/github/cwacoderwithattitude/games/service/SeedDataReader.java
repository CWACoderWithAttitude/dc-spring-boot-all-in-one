package io.github.cwacoderwithattitude.games.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.github.cwacoderwithattitude.games.model.Game;

@Service
public class SeedDataReader {
    private Logger logger = java.util.logging.Logger.getLogger(SeedDataReader.class.getName());
    @Autowired
    GameService gameService;

    /**
     * Read rule seed data from Resource and sava each rule to DB
     */
    public void readSeedData(Resource resource) throws IOException, ParseException {
        logger.info("seed data file: " + resource.getFilename());
        InputStream in = resource.getInputStream();
        String jsonString = new String(in.readAllBytes());
        // logger.info("seed data jsonString: " + jsonString);
        // Object json = new JSONParser(jsonString).parse();
        ArrayList<Object> array = new JSONParser(jsonString).parseArray();
        // logger.info("seed data contents: " + array);
        array.forEach(item -> {
            Game g = mapToGame(item);

            logger.info("Game: {}" + g);
            gameService.save(g);
        });
    }

    private Game mapToGame(Object item) {
        Map map = (Map<String, Object>) item;
        Game g = new Game(map.get("title").toString());
        g.setEan13(map.get("ean13").toString());
        g.setPublisher(map.get("publisher").toString());
        String minPlayers = map.get("max_number_of_players").toString();
        String maxPlayers = map.get("max_number_of_players").toString();
        String minAge = map.get("min_age").toString();
        g.setMax_number_of_players(Integer.parseInt(maxPlayers));
        g.setMin_number_of_players(Integer.parseInt(minPlayers));
        g.setMin_age(Integer.parseInt(minAge));
        // g.setGenre(map.get("genre").toString());
        // sg.setTypical_duration(map.get("typical_duration").toString());
        return g;
    }
}
