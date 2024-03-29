package edu.northeastern.cs5500.starterbot.player_package;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import lombok.Getter;
import lombok.NonNull;

/**
 * The {@code Item} class represents a generic item in the game. It provides common attributes such
 * as type, price, and an abstract method for casting the item. Subclasses should extend this class
 * to provide specific implementations for casting the item.
 */
public abstract class Item {
    /** The type of the item. */
    @Getter protected String type;

    /** The effect of the item. */
    @Getter protected ItemEffect effect;

    /** Abstract method to be implemented by subclasses for casting the item. */
    public abstract String castItem(@NonNull Player player, Pokemon pokemon, Pokemon enemyPokemon);

    /**
     * Gets the name of the item, combining the type and effect in lowercase.
     *
     * @return The name of the item.
     */
    public String getItemName() {
        return this.type + " " + this.effect.toString().toLowerCase();
    }
}
