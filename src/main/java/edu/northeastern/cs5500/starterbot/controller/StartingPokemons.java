package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;

public class StartingPokemons {
    PokemonTypeController pokemonTypeController = new PokemonTypeController();
    SkillController skillController = new SkillController();

    @Getter Set<Pokemon> startingPokemons = new HashSet<>();

    @Inject
    public StartingPokemons() {
        buildStartingPokemons();
    }

    public void buildStartingPokemons() {
        Pokemon pokemon1 = pokemonTypeController.generatePokemon("0001");
        skillController.fillSkillSet(pokemon1);
        startingPokemons.add(pokemon1);

        Pokemon pokemon2 = pokemonTypeController.generatePokemon("0004");
        skillController.fillSkillSet(pokemon2);
        startingPokemons.add(pokemon2);

        Pokemon pokemon3 = pokemonTypeController.generatePokemon("0007");
        skillController.fillSkillSet(pokemon3);
        startingPokemons.add(pokemon3);
    }
}
