package edu.northeastern.cs5500.starterbot.model.pokemon;

import lombok.Getter;

/**
 * The {@code PokemonSpecies} class represents a species of Pokemon. It contains information such as
 * the Pokedex entry, species name, base information, and maximum information.
 */
public class PokemonSpecies {
    @Getter private String pokedex; // The Pokedex entry associated with the Pokemon species.
    @Getter private String speciesName; // The name of the Pokemon species.

    @Getter
    private int[]
            baseInfo; // An array representing the base information of the Pokemon species at level
    // 1.

    /**
     * Constructs a new PokemonSpecies with the specified species name. Retrieves the Pokedex entry
     * and generates base information using the PokemonStat utility class.
     *
     * @param speciesName The name of the Pokemon species.
     */
    public PokemonSpecies(String speciesName) {
        this.speciesName = speciesName;
        this.pokedex = PokemonStat.getPokedex(speciesName);
        this.baseInfo = PokemonStat.generateBase(this.pokedex);
    }
}
