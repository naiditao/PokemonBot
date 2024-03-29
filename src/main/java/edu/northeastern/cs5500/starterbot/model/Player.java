package edu.northeastern.cs5500.starterbot.model;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.io.InputStream;
import java.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Data
@Slf4j
public class Player implements Model {
    ObjectId id;
    String discordUserId;
    String preferredName;

    ObjectId locationId;

    ObjectId curBattleId;
    ObjectId curCampaignId;

    Teams teams;

    Map<String, Integer> itemsNum;
    List<Pokemon> pokemonInventory;
    int pokeCoin;

    static final int START_POKECOIN = 1000000;

    public Player() {
        itemsNum = makeItemInventory();
        pokeCoin = START_POKECOIN;
        pokemonInventory = new ArrayList<>();
    }

    private static Map<String, Integer> makeItemInventory() {
        InputStream inputStream =
                LocationController.class.getClassLoader().getResourceAsStream("items.txt");
        Map<String, Integer> itemMap = new HashMap<>();

        int lineNumber = 1;
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                itemMap.put(line[0] + " " + line[2].toLowerCase(), 0);
                lineNumber++;
            }
        } catch (NumberFormatException | NoSuchElementException e) {
            log.error("items.txt line {} is not correctly formatted", lineNumber);
            log.error("Exception while parsing items.txt", e);
            throw e;
        }
        return itemMap;
    }

    public void addPokemon(Pokemon pokemon) {
        if (teams.getNum() < 6) {
            teams.getTeam().add(pokemon);
            teams.setNum(teams.getNum() + 1);
            log.info(Integer.toString(teams.getNum()));
        } else {
            pokemonInventory.add(pokemon);
        }
    }

    public void removePokemon(Pokemon pokemon) {
        pokemonInventory.remove(pokemon);
    }
}
