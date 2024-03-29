package edu.northeastern.cs5500.starterbot.player_package;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.Random;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 * The {@code PokeBall} class represents a PokeBall item in the game, which is a specific type of
 * item. It extends the {@code Item} class and inherits common attributes like type, price, and
 * effect.
 */
public class PokeBall extends Item {

    /**
     * Parameterized constructor for the PokeBall class.
     *
     * @param type The type of the PokeBall.
     * @param effect The effect of the PokeBall.
     */
    Random rand = new Random();

    public PokeBall(@Nonnull String type, ItemEffect effect) {
        this.type = type;
        this.effect = effect;
    }

    /**
     * Overrides the abstract method from the item class to provide specific implementation for
     * casting the PokeBall item.
     */
    @Override
    public String castItem(@NonNull Player player, Pokemon pokemon, Pokemon enemyPokemon) {
        int chanceForCatching =
                switch (this.effect) {
                    case SMALL -> 75;
                    case MEDIUM -> 90;
                    case LARGE -> 100;
                    default -> 0;
                };

        if (rand.nextInt(100) < chanceForCatching) {
            return "Catch Successful";
        } else {
            return "Catch Fail";
        }
    }
}
