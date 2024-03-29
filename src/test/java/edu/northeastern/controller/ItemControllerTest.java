package edu.northeastern.controller;

import static junit.framework.TestCase.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.ItemController;
import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.pokemon.*;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.HashSet;
import java.util.Set;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemControllerTest {
    private InMemoryRepository<Player> repository;
    private ItemController testItemController;
    private LocationController locationController;
    private InMemoryRepository<Location> repositoryLocation;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository<>();
        repositoryLocation = new InMemoryRepository<>();
        locationController = new LocationController(repositoryLocation);
        testItemController = new ItemController(repository);
        testItemController.setLocationController(locationController);
    }

    @Test
    void testGetItemPrices() {
        assertEquals(12, ItemController.getItemPrices().size());
    }

    @Test
    void testAddItems() {
        Player player = new Player();
        ObjectId playerId = new ObjectId();
        player.setId(playerId);
        repository.add(player);

        String itemName = "HealPotion small";

        int curNum = player.getItemsNum().get(itemName);
        testItemController.addItem(player, itemName);
        repository.update(player);
        assertEquals(1, player.getItemsNum().get(itemName) - curNum);
    }

    @Test
    void testRemoveItems() {
        Player player = new Player();
        ObjectId playerId = new ObjectId();
        player.setId(playerId);
        repository.add(player);

        String itemName = "HealPotion small";

        player.getItemsNum().put(itemName, 2);
        testItemController.removeItem(player, itemName);
        repository.update(player);
        int curNum = player.getItemsNum().get(itemName);
        assertEquals(1, curNum);
    }

    @Test
    void testGetItemPrice() {
        int price = this.testItemController.getItemPrice("HealPotion small");
        assertEquals(300, price);
    }

    @Test
    void testBuyItem() {
        Player player = new Player();
        Location curLocation = locationController.getSpawningLocation();
        ObjectId locationId = curLocation.getId();
        player.setLocationId(locationId);
        repository.add(player);

        String itemName = "HealPotion small";
        testItemController.buyItem(player, itemName);
        repository.update(player);

        assertEquals(9700, player.getPokeCoin());
    }

    @Test
    void testUseItem() {
        Player player = new Player();
        ObjectId playerId = new ObjectId();
        Set<PokemonType> types = new HashSet<>();
        Set<Skill> skills = new HashSet<>();
        int curLV = 5;
        String name = "Squirtle";
        String enemyName = "Mew";

        PokemonSpecies species = new PokemonSpecies("Squirtle");
        Pokemon pokemon =
                new PokemonBuilder()
                        .withTypes(types)
                        .withSkills(skills)
                        .withCurLV(curLV)
                        .withName(name)
                        .withPokemonSpecies(species)
                        .build();
        Pokemon enemyPokemon =
                new PokemonBuilder()
                        .withTypes(types)
                        .withSkills(skills)
                        .withCurLV(3)
                        .withName(enemyName)
                        .withPokemonSpecies(new PokemonSpecies("Mew"))
                        .build();
        ;
        repository.add(player);

        String itemName = "ExpCandy small";
        testItemController.addItem(player, itemName);
        testItemController.useItem(player, pokemon, itemName, enemyPokemon);
        repository.update(player);

        assertEquals(10, pokemon.getCurEXP());
    }
}
