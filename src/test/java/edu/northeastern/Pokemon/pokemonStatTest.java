package edu.northeastern.Pokemon;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.pokemon.*;
import org.junit.jupiter.api.Test;

class pokemonStatTest {

    @Test
    void testGetPokedex() {
        assertEquals("0001", PokemonStat.getPokedex("Bulbasaur"));
        assertEquals("0004", PokemonStat.getPokedex("Charmander"));
        assertEquals("0007", PokemonStat.getPokedex("Squirtle"));
    }

    @Test
    void testGenerateBase() {
        assertArrayEquals(new int[] {45, 49, 49, 65, 65, 45}, PokemonStat.generateBase("0001"));
        assertArrayEquals(new int[] {60, 62, 63, 80, 80, 60}, PokemonStat.generateBase("0002"));
        assertArrayEquals(new int[] {80, 82, 83, 100, 100, 80}, PokemonStat.generateBase("0003"));
    }

    @Test
    void testLevelUp() {
        assertArrayEquals(
                new int[] {294, 216, 216, 251, 251, 207}, PokemonStat.levelUp("0001", 100));
        assertArrayEquals(
                new int[] {324, 245, 247, 284, 284, 240}, PokemonStat.levelUp("0002", 100));
        assertArrayEquals(
                new int[] {364, 289, 291, 328, 328, 284}, PokemonStat.levelUp("0003", 100));
    }

    @Test
    void testGetUrl() {
        assertEquals(
                "https://img.pokemondb.net/artwork/large/bulbasaur.jpg",
                PokemonStat.getUrl("0001"));
        assertEquals(
                "https://img.pokemondb.net/artwork/large/ivysaur.jpg", PokemonStat.getUrl("0002"));
        assertEquals(
                "https://img.pokemondb.net/artwork/large/venusaur.jpg", PokemonStat.getUrl("0003"));
    }

    @Test
    void testGetChallengeLevel() {
        assertEquals(5, PokemonStat.getChallengeLevel("0001"));
        assertEquals(16, PokemonStat.getChallengeLevel("0002"));
        assertEquals(32, PokemonStat.getChallengeLevel("0003"));
    }

    @Test
    void testGetEvolveLevel() {
        assertEquals(16, PokemonStat.getEvolveLevel("0001"));
        assertEquals(32, PokemonStat.getEvolveLevel("0002"));
        assertEquals(101, PokemonStat.getEvolveLevel("0003"));
    }
}
