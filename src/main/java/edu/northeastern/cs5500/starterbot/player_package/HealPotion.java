package edu.northeastern.cs5500.starterbot.player_package;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 * The {@code HealPotion} class represents a heal potion item in the game, which is a specific type
 * of item. It extends the {@code Item} class and inherits common attributes like type, price, and
 * effect.
 */
public class HealPotion extends Item {

    /**
     * Parameterized constructor for the heal potion class.
     *
     * @param type The type of the HealPotion.
     * @param effect The effect of the HealPotion.
     */
    public HealPotion(@Nonnull String type, ItemEffect effect) {
        this.type = type;
        this.effect = effect;
    }

    /**
     * Overrides the abstract method from the item class to provide specific implementation for
     * casting the heal potion item.
     */
    @Override
    public String castItem(@NonNull Player player, Pokemon pokemon, Pokemon enemyPokemon) {
        int newHP = 0;
        int curHP = pokemon.getCurHP();
        if (this.effect.equals(ItemEffect.SMALL)) {
            newHP = pokemon.getCurHP() + 10;
        } else if (this.effect.equals(ItemEffect.MEDIUM)) {
            newHP = pokemon.getCurHP() + 50;
        } else if (this.effect.equals(ItemEffect.LARGE)) {
            newHP = pokemon.getCurHP() + 100;
        }
        pokemon.setCurHP(Math.min(newHP, pokemon.getCurMaxHp()));
        return String.format("%s has gain %d HP", pokemon.getName(), pokemon.getCurHP() - curHP);
    }
}
