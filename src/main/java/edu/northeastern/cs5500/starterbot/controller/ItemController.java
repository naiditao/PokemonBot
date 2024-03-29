package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.player_package.Item;
import edu.northeastern.cs5500.starterbot.player_package.ItemEffect;
import edu.northeastern.cs5500.starterbot.player_package.ItemFactory;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.inject.Inject;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ItemController {
    @Inject LocationController locationController;
    GenericRepository<Player> playerRepository;
    static Map<String, Integer> itemPrices = new HashMap<>();

    @Inject
    public ItemController(GenericRepository<Player> playerRepository) {
        this.playerRepository = playerRepository;
        setItemPrices();
    }

    public static Map<String, Integer> getItemPrices() {
        return itemPrices;
    }

    public void addItem(@NonNull Player player, String itemName) {
        player.getItemsNum().replace(itemName, player.getItemsNum().get(itemName) + 1);
        playerRepository.update(player);
    }

    public void removeItem(@NonNull Player player, String itemName) {
        if (player.getItemsNum().containsKey(itemName)) {
            Map<String, Integer> items = player.getItemsNum();
            int oldQuantity = items.get(itemName);
            int newQuantity = Math.max(oldQuantity - 1, 0);
            if (newQuantity > 0) {
                items.put(itemName, newQuantity);
            } else {
                items.remove(itemName);
            }
            player.setItemsNum(items);
            playerRepository.update(player);
        } else {
            log.error("This item {} does not exist in player's inventory.", itemName);
        }
    }

    public int getItemQuantity(@NonNull Player player, String itemName) {
        return player.getItemsNum().getOrDefault(itemName, 0);
    }

    private void setItemPrices() {
        InputStream inputStream =
                ItemController.class.getClassLoader().getResourceAsStream("items.txt");
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                itemPrices.put(line[0] + " " + line[2].toLowerCase(), Integer.parseInt(line[1]));
            }
        } catch (NoSuchElementException e) {
            log.error("Exception while parsing items.txt", e);
            throw e;
        }
    }

    public int getItemPrice(String itemName) {
        return itemPrices.getOrDefault(itemName, -1);
    }

    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    public void buyItem(@NonNull Player player, String itemName) {
        Location currentLocation = locationController.getLocation(player.getLocationId());
        if (currentLocation.getLocationType().equals(LocationType.TOWN)) {
            int itemPrice = getItemPrice(itemName);

            if (player.getPokeCoin() >= itemPrice) {
                player.setPokeCoin(player.getPokeCoin() - itemPrice);
                Map<String, Integer> items = player.getItemsNum();
                items.put(itemName, items.getOrDefault(itemName, 0) + 1);
                player.setItemsNum(items);
                playerRepository.update(player);
            } else {
                log.error("Not enough pokecoins to buy this item {}", itemName);
            }
        } else {
            log.error("There is no item store to buy this item {}", itemName);
        }
    }

    public String useItem(
            @NonNull Player player, Pokemon pokemon, String itemName, Pokemon enemyPokemon) {
        Map<String, Integer> items = player.getItemsNum();
        String info = null;
        if (items.containsKey(itemName) && items.get(itemName) > 0) {
            String name = itemName.split(" ")[0];
            String type = itemName.split(" ")[1];
            Item item;
            switch (type) {
                case "small":
                    item = ItemFactory.createItem(name, ItemEffect.SMALL);
                    info = item.castItem(player, pokemon, enemyPokemon);
                    break;

                case "medium":
                    item = ItemFactory.createItem(name, ItemEffect.MEDIUM);
                    info = item.castItem(player, pokemon, enemyPokemon);
                    break;

                case "large":
                    item = ItemFactory.createItem(name, ItemEffect.LARGE);
                    info = item.castItem(player, pokemon, enemyPokemon);
                    break;

                default:
                    break;
            }
            removeItem(player, itemName);
        } else {
            log.error("There is no item {} in player's inventory", itemName);
        }
        return info;
    }
}
