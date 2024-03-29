package edu.northeastern.PlayerPackage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.player_package.ItemEffect;
import edu.northeastern.cs5500.starterbot.player_package.PokeBall;
import org.junit.jupiter.api.Test;

class PokeBallTest {
    @Test
    void castItem() {
        PokeBall testSmallPokeBall = new PokeBall("PokeBall", ItemEffect.SMALL);
        assertEquals("PokeBall small", testSmallPokeBall.getItemName());
        assertEquals(ItemEffect.SMALL, testSmallPokeBall.getEffect());

        PokeBall testMediumPokeBall = new PokeBall("PokeBall", ItemEffect.MEDIUM);
        assertEquals("PokeBall medium", testMediumPokeBall.getItemName());
        assertEquals(ItemEffect.MEDIUM, testMediumPokeBall.getEffect());

        PokeBall testLargePokeBall = new PokeBall("PokeBall", ItemEffect.LARGE);
        assertEquals("PokeBall large", testLargePokeBall.getItemName());
        assertEquals(ItemEffect.LARGE, testLargePokeBall.getEffect());
    }

    @Test
    void getterTest() {
        PokeBall testSmallPokeBall = new PokeBall("PokeBall", ItemEffect.SMALL);
        assertEquals("PokeBall small", testSmallPokeBall.getItemName());
        assertEquals(ItemEffect.SMALL, testSmallPokeBall.getEffect());

        PokeBall testMediumPokeBall = new PokeBall("PokeBall", ItemEffect.MEDIUM);
        assertEquals("PokeBall medium", testMediumPokeBall.getItemName());
        assertEquals(ItemEffect.MEDIUM, testMediumPokeBall.getEffect());

        PokeBall testLargePokeBall = new PokeBall("PokeBall", ItemEffect.LARGE);
        assertEquals("PokeBall large", testLargePokeBall.getItemName());
        assertEquals(ItemEffect.LARGE, testLargePokeBall.getEffect());
    }
}
