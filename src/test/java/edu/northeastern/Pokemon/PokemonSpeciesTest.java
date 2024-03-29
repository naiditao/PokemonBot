package edu.northeastern.Pokemon;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonSpecies;
import org.junit.jupiter.api.Test;

class PokemonSpeciesTest {

    @Test
    void testConstructor() {
        String speciesName = "Bulbasaur";
        PokemonSpecies pokemonSpecies = new PokemonSpecies(speciesName);
        assertEquals(speciesName, pokemonSpecies.getSpeciesName());
        assertNotNull(pokemonSpecies.getPokedex());
        assertNotNull(pokemonSpecies.getBaseInfo());
    }

    @Test
    void testGeneratedBaseInfoLength() {
        String speciesName = "Charmander";
        PokemonSpecies pokemonSpecies = new PokemonSpecies(speciesName);
        assertEquals(6, pokemonSpecies.getBaseInfo().length);
    }
}
