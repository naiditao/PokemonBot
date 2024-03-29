package edu.northeastern.cs5500.starterbot.model;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Battle implements Model {
    ObjectId id;
    Pokemon myPokemon;
    final Pokemon enemyPokemon;
    Set<Pokemon> pokemonEngaged;
    Pokemon startPokemon;
    Pokemon endPokemon;

    public Battle(Pokemon myPokemon, Pokemon enemyPokemon) {
        this.myPokemon = myPokemon;
        this.enemyPokemon = enemyPokemon;
        determineStartPokemon();
        pokemonEngaged = new HashSet<>();
    }

    public void determineStartPokemon() {
        if (myPokemon.getCurSpeed() > enemyPokemon.getCurSpeed()) {
            startPokemon = myPokemon;
            endPokemon = enemyPokemon;
        } else {
            startPokemon = enemyPokemon;
            endPokemon = myPokemon;
        }
    }
}
