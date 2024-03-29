package edu.northeastern.cs5500.starterbot.model.pokemon;

import java.util.Set;

/**
 * The {@code PokemonBuilder} class is a builder for creating instances of the {@code Pokemon}
 * class. It provides a fluent interface for setting various stats of a Pokemon and then
 * constructing the Pokemon object.
 */
public class PokemonBuilder {

    /** The set of Pokemon types. */
    private Set<PokemonType> types;
    /** The set of skills for the Pokemon. */
    private Set<Skill> skills;
    /** The current level of the Pokemon. */
    private int curLV;
    /** The name of the Pokemon. */
    private String name;
    /** The species of the Pokemon. */
    private PokemonSpecies pokemonSpecies;

    /**
     * Sets the types of the Pokemon.
     *
     * @param types The set of Pokemon types.
     * @return This instance for method chaining.
     */
    public PokemonBuilder withTypes(Set<PokemonType> types) {
        this.types = types;
        return this;
    }

    /**
     * Sets the skills of the Pokemon.
     *
     * @param skills The set of skills.
     * @return This instance for method chaining.
     */
    public PokemonBuilder withSkills(Set<Skill> skills) {
        this.skills = skills;
        return this;
    }

    /**
     * Sets the current level of the Pokemon.
     *
     * @param curLV The current level.
     * @return This instance for method chaining.
     */
    public PokemonBuilder withCurLV(int curLV) {
        this.curLV = curLV;
        return this;
    }

    /**
     * Sets the current level of the Pokemon.
     *
     * @param curLV The current level.
     * @return This instance for method chaining.
     */
    public PokemonBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the species of the Pokemon.
     *
     * @param pokemonSpecies The species of the Pokemon.
     * @return This instance for method chaining.
     */
    public PokemonBuilder withPokemonSpecies(PokemonSpecies pokemonSpecies) {
        this.pokemonSpecies = pokemonSpecies;
        return this;
    }

    /**
     * Constructs and returns a new instance of the pokemon class with the specified attributes.
     *
     * @return A new pokemon instance.
     */
    public Pokemon build() {
        return new Pokemon(types, skills, curLV, name, pokemonSpecies);
    }
}
