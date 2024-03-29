package edu.northeastern.cs5500.starterbot.player_package;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 * The {@code EXPCandy} class represents a EXP candy item in the game, which is a specific type of
 * item. It extends the {@code Item} class and inherits common attributes like type, price, and
 * effect.
 */
public class ExpCandy extends Item {

    /**
     * Parameterized constructor for the EXP candy class.
     *
     * @param type The type of the Item.
     * @param effect The effect of the Item.
     */
    public ExpCandy(@Nonnull String type, ItemEffect effect) {
        this.type = type;
        this.effect = effect;
    }

    /**
     * Overrides the abstract method from the item class to provide specific implementation for
     * casting the heal potion item.
     */
    @Override
    public String castItem(@NonNull Player player, Pokemon pokemon, Pokemon enemyPokemon) {
        int expGain = 0;
        if (this.effect.equals(ItemEffect.SMALL)) {
            expGain = 10;
        } else if (this.effect.equals(ItemEffect.MEDIUM)) {
            expGain = 50;
        } else if (this.effect.equals(ItemEffect.LARGE)) {
            expGain = 100;
        }
        pokemon.addEXP(expGain);
        return String.format("%s has gain %d exp", pokemon.getName(), expGain);
    }
}
