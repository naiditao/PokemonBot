package edu.northeastern.cs5500.starterbot.player_package;

/**
 * The {@code ItemFactory} class is responsible for creating instances of {@code Item} subclasses
 * based on the provided item type, price, and effect.
 *
 * <p>It follows a factory pattern, allowing the creation of different types of items with a common
 * interface.
 */
public class ItemFactory {

    /** Private constructor to prevent instantiation of the PokemonStat class.. */
    private ItemFactory() {}

    /**
     * Creates and returns an instance of item based on the provided parameters.
     *
     * @param itemType The type of item to create.
     * @param effect The effect of the item, represented by an instance of item 3ffect.
     * @return A new instance of the appropriate item subclass.
     * @throws IllegalArgumentException If the provided item type is invalid.
     */
    public static Item createItem(String itemType, ItemEffect effect) {
        switch (itemType) {
            case "HealPotion":
                return new HealPotion(itemType, effect);
            case "PokeBall":
                return new PokeBall(itemType, effect);
            case "ExpCandy":
                return new ExpCandy(itemType, effect);
            default:
                throw new IllegalArgumentException("Invalid item type: " + itemType);
        }
    }
}
