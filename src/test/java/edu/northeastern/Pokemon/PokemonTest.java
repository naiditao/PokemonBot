package edu.northeastern.Pokemon;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonBuilder;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PokemonTest {

    @Test
    void testConstructor() {
        Set<PokemonType> types = new HashSet<>();
        Set<Skill> skills = new HashSet<>();
        int curLV = 5;
        String name = "Mew";
        PokemonSpecies pokemonSpecies = new PokemonSpecies(name);

        Pokemon pokemon =
                new PokemonBuilder()
                        .withTypes(types)
                        .withSkills(skills)
                        .withCurLV(curLV)
                        .withName(name)
                        .withPokemonSpecies(pokemonSpecies)
                        .build();
        ;

        assertEquals(types, pokemon.getTypes());
        assertEquals(skills, pokemon.getSkills());
        assertEquals(curLV, pokemon.getCurLV());
        assertEquals(0, pokemon.getCurEXP());
        assertEquals(name, pokemon.getName());
        assertEquals(pokemonSpecies, pokemon.getPokemonSpecies());
        assertNotNull(pokemon.getId());
    }

    @Test
    void testLevelUp() {
        Set<PokemonType> types = new HashSet<>();
        Set<Skill> skills = new HashSet<>();
        int curLV = 5;
        String name = "Mewtwo";
        PokemonSpecies pokemonSpecies = new PokemonSpecies("Mewtwo");

        Pokemon pokemon =
                new PokemonBuilder()
                        .withTypes(types)
                        .withSkills(skills)
                        .withCurLV(curLV)
                        .withName(name)
                        .withPokemonSpecies(pokemonSpecies)
                        .build();
        int initialAttack = pokemon.getCurAtt();

        pokemon.levelUp();
        pokemon.levelUp();
        pokemon.levelUp();
        pokemon.levelUp();
        pokemon.levelUp();

        assertEquals(curLV + 5, pokemon.getCurLV());
        assertEquals(0, pokemon.getCurEXP());
        assertTrue(pokemon.getCurAtt() > initialAttack);
    }

    @Test
    void testSetters() {
        Set<PokemonType> types = new HashSet<>();
        Set<Skill> skills = new HashSet<>();
        int curLV = 5;
        String name = "Squirtle";
        PokemonSpecies pokemonSpecies = new PokemonSpecies("Squirtle");

        Pokemon pokemon =
                new PokemonBuilder()
                        .withTypes(types)
                        .withSkills(skills)
                        .withCurLV(curLV)
                        .withName(name)
                        .withPokemonSpecies(pokemonSpecies)
                        .build();
        ;

        pokemon.setCurMaxHp(120);
        assertEquals(120, pokemon.getCurMaxHp());

        pokemon.setCurAtt(50);
        assertEquals(50, pokemon.getCurAtt());

        pokemon.setCurDef(40);
        assertEquals(40, pokemon.getCurDef());

        pokemon.setCurSpa(60);
        assertEquals(60, pokemon.getCurSpa());

        pokemon.setCurSpd(55);
        assertEquals(55, pokemon.getCurSpd());

        pokemon.setCurSpeed(70);
        assertEquals(70, pokemon.getCurSpeed());
    }
}
