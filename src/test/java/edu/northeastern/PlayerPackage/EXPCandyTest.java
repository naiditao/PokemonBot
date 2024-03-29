package edu.northeastern.PlayerPackage;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonBuilder;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.player_package.*;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

class EXPCandyTest {

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
        Player player = new Player();

        ExpCandy testSmallEXPCandy = new ExpCandy("EXPCandy", ItemEffect.SMALL);
        testSmallEXPCandy.castItem(player, pokemon, null);
        assertEquals(10, pokemon.getCurEXP());

        ExpCandy testMediumEXPCandy = new ExpCandy("EXPCandy", ItemEffect.MEDIUM);
        testMediumEXPCandy.castItem(player, pokemon, null);
        assertEquals(60, pokemon.getCurEXP());

        ExpCandy testLargeEXPCandy = new ExpCandy("EXPCandy", ItemEffect.LARGE);
        testLargeEXPCandy.castItem(player, pokemon, null);
        assertEquals(60, pokemon.getCurEXP());
        assertEquals(2, pokemon.getCurLV());
    }

    @Test
    void getterTest() {
        ExpCandy testSmallEXPCandy = new ExpCandy("EXPCandy", ItemEffect.SMALL);

        assertEquals("EXPCandy small", testSmallEXPCandy.getItemName());
        assertEquals(ItemEffect.SMALL, testSmallEXPCandy.getEffect());

        ExpCandy testMediumEXPCandy = new ExpCandy("EXPCandy", ItemEffect.MEDIUM);

        assertEquals("EXPCandy medium", testMediumEXPCandy.getItemName());
        assertEquals(ItemEffect.MEDIUM, testMediumEXPCandy.getEffect());

        ExpCandy testLargeEXPCandy = new ExpCandy("EXPCandy", ItemEffect.LARGE);

        assertEquals("EXPCandy large", testLargeEXPCandy.getItemName());
        assertEquals(ItemEffect.LARGE, testLargeEXPCandy.getEffect());
    }
}
