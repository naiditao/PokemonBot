package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Teams;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonBuilder;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Data;

@Data
public class TestBattlePokemon {
    PokemonTypeController pokemonTypeController = new PokemonTypeController();
    SkillController skillController = new SkillController();
    Pokemon myPokemon;
    Pokemon enemyPokemon;

    Teams myTeam;
    Teams enemyTeam;

    @Inject
    public TestBattlePokemon() {
        Set<Integer> grassStrongAgainst = new HashSet<>();
        grassStrongAgainst.add(5);
        grassStrongAgainst.add(6);
        grassStrongAgainst.add(11);

        Set<Integer> grassWeakAgainst = new HashSet<>();
        grassWeakAgainst.add(3);
        grassWeakAgainst.add(4);
        grassWeakAgainst.add(7);
        grassWeakAgainst.add(9);
        grassWeakAgainst.add(10);
        grassWeakAgainst.add(12);
        grassWeakAgainst.add(16);

        PokemonType grass = pokemonTypeController.getPokemonTypeFromName("Grass");

        Set<Integer> posionStrongAgainst = new HashSet<>();
        posionStrongAgainst.add(12);
        posionStrongAgainst.add(18);

        Set<Integer> posionWeakAgainst = new HashSet<>();
        posionWeakAgainst.add(4);
        posionWeakAgainst.add(5);
        posionWeakAgainst.add(6);
        posionWeakAgainst.add(8);

        Set<Integer> posionNoDamageAgainst = new HashSet<>();
        posionNoDamageAgainst.add(9);

        PokemonType posion = pokemonTypeController.getPokemonTypeFromName("Posion");
        Set<PokemonType> bulbasaurType = new HashSet<>();
        bulbasaurType.add(posion);
        bulbasaurType.add(grass);

        Skill vineWhip = skillController.getSkillFromName("Vine Whip");
        Skill razorLeaf = skillController.getSkillFromName("Razor Leaf");
        Set<Skill> bulbasaurSkills = new HashSet<>();
        bulbasaurSkills.add(vineWhip);
        bulbasaurSkills.add(razorLeaf);

        PokemonSpecies pokemonSpecies = new PokemonSpecies("Bulbasaur");

        myPokemon =
                new PokemonBuilder()
                        .withTypes(bulbasaurType)
                        .withSkills(bulbasaurSkills)
                        .withCurLV(10)
                        .withName("Bulbasaur")
                        .withPokemonSpecies(pokemonSpecies)
                        .build();

        Pokemon test2 =
                new PokemonBuilder()
                        .withTypes(bulbasaurType)
                        .withSkills(bulbasaurSkills)
                        .withCurLV(12)
                        .withName("Bulbasaur")
                        .withPokemonSpecies(pokemonSpecies)
                        .build();

        Set<Integer> fireStrongAgainst = new HashSet<>();
        fireStrongAgainst.add(7);
        fireStrongAgainst.add(9);
        fireStrongAgainst.add(12);
        fireStrongAgainst.add(15);

        Set<Integer> fireWeakAgainst = new HashSet<>();
        fireWeakAgainst.add(6);
        fireWeakAgainst.add(10);
        fireWeakAgainst.add(11);
        fireWeakAgainst.add(16);

        PokemonType fire = pokemonTypeController.getPokemonTypeFromName("Fire");

        Set<PokemonType> charmanderType = new HashSet<>();
        charmanderType.add(fire);

        Skill ember = skillController.getSkillFromName("Ember");
        Set<Skill> charmanderSkills = new HashSet<>();
        charmanderSkills.add(ember);
        PokemonSpecies pokemonSpecies2 = new PokemonSpecies("Charmander");

        enemyPokemon =
                new PokemonBuilder()
                        .withTypes(charmanderType)
                        .withSkills(charmanderSkills)
                        .withCurLV(10)
                        .withName("Charmander")
                        .withPokemonSpecies(pokemonSpecies2)
                        .build();

        Pokemon test1 =
                new PokemonBuilder()
                        .withTypes(charmanderType)
                        .withSkills(charmanderSkills)
                        .withCurLV(12)
                        .withName("Charmander")
                        .withPokemonSpecies(pokemonSpecies2)
                        .build();

        ArrayList<Pokemon> myTeamPokemons = new ArrayList<>();
        myTeamPokemons.add(enemyPokemon);
        myTeamPokemons.add(myPokemon);

        myTeam = new Teams(myTeamPokemons);

        ArrayList<Pokemon> enemyTeamPokemons = new ArrayList<>();
        enemyTeamPokemons.add(test2);
        enemyTeamPokemons.add(test1);

        enemyTeam = new Teams(enemyTeamPokemons);
    }
}
