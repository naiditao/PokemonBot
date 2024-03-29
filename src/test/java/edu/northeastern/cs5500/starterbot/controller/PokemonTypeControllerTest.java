package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonType;
import org.junit.Test;

public class PokemonTypeControllerTest {
    PokemonTypeController pokemonTypeController = new PokemonTypeController();

    @Test
    public void getPokemonTypeFromNameTest() {
        PokemonType test1 = pokemonTypeController.getPokemonTypeFromName("Fire");
        PokemonType test2 = pokemonTypeController.getPokemonTypeFromName("Grass");

        PokemonType test3 = pokemonTypeController.getPokemonTypeFromName("Normal");
        PokemonType test4 = pokemonTypeController.getPokemonTypeFromName("Ghost");

        assertEquals(test1.getName(), "Fire");
        assertEquals(test2.getName(), "Grass");
        assertTrue(test1.getStrongAgainst().contains(test2.getTypeId()));
        assertTrue(test2.getWeakAgainst().contains(test1.getTypeId()));

        assertEquals(test3.getName(), "Normal");
        assertEquals(test4.getName(), "Ghost");
        assertTrue(test3.getNoDamageAgainst().contains(test4.getTypeId()));
    }

    @Test
    public void generatePokemon() {
        Pokemon test1 = pokemonTypeController.generatePokemon("0003");

        assertTrue(
                test1.getTypes().contains(pokemonTypeController.getPokemonTypeFromName("Grass")));
        assertTrue(
                test1.getTypes().contains(pokemonTypeController.getPokemonTypeFromName("Posion")));

        Pokemon test2 = pokemonTypeController.generatePokemon("0006");
        assertTrue(test2.getTypes().contains(pokemonTypeController.getPokemonTypeFromName("Fire")));
        assertTrue(
                test2.getTypes().contains(pokemonTypeController.getPokemonTypeFromName("Flying")));
    }
}
