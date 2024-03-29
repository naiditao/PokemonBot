package edu.northeastern.PlayerPackage;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonBuilder;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.player_package.*;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

class HealPotionTest {

    @Test
    void castItem() {
        PokemonSpecies species = new PokemonSpecies("Pikachu");
        Pokemon pokemon =
                new PokemonBuilder()
                        .withTypes(new HashSet<>())
                        .withSkills(new HashSet<>())
                        .withCurLV(1)
                        .withName("Pikachu")
                        .withPokemonSpecies(species)
                        .build();
        pokemon.setCurHP(10);
        Player player = new Player();

        HealPotion testSmallHealPotion = new HealPotion("HealPotion", ItemEffect.SMALL);
        testSmallHealPotion.castItem(player, pokemon, null);
        assertEquals(20, pokemon.getCurHP());

        HealPotion testMediumHealPotion = new HealPotion("HealPotion", ItemEffect.MEDIUM);
        testMediumHealPotion.castItem(player, pokemon, null);
        assertEquals(35, pokemon.getCurHP());

        HealPotion testLargeHealPotion = new HealPotion("HealPotion", ItemEffect.LARGE);
        testLargeHealPotion.castItem(player, pokemon, null);
        assertEquals(35, pokemon.getCurHP());
    }

    @Test
    void getterTest() {
        HealPotion testSmallHealPotion = new HealPotion("HealPotion", ItemEffect.SMALL);
        assertEquals("HealPotion small", testSmallHealPotion.getItemName());
        assertEquals(ItemEffect.SMALL, testSmallHealPotion.getEffect());

        HealPotion testMediumHealPotion = new HealPotion("HealPotion", ItemEffect.MEDIUM);
        assertEquals("HealPotion medium", testMediumHealPotion.getItemName());
        assertEquals(ItemEffect.MEDIUM, testMediumHealPotion.getEffect());

        HealPotion testLargeHealPotion = new HealPotion("HealPotion", ItemEffect.LARGE);
        assertEquals("HealPotion large", testLargeHealPotion.getItemName());
        assertEquals(ItemEffect.LARGE, testLargeHealPotion.getEffect());
    }
}
