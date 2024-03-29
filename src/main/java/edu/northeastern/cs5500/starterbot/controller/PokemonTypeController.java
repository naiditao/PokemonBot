package edu.northeastern.cs5500.starterbot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonBuilder;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonStat;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PokemonTypeController {
    ObjectMapper mapper = new ObjectMapper();
    File pokemonTypeFile =
            new File("/workspaces/bot-corgi/src/main/resources/pokemonType.json").getAbsoluteFile();
    File pokemonFile =
            new File("/workspaces/bot-corgi/src/main/resources/pokemonToTypes.json")
                    .getAbsoluteFile();

    @Inject
    public PokemonTypeController() {
        /* This is an empty constructor used for inject. */
    }

    /**
     * This function takes the name of the PokemonType and search for it in pokemonType.json If it
     * find the corresponse entry it will return the corresponse PokemonType object It will return
     * null if it cannot find the name
     *
     * @param typeName name of the PokemonType (ie. Grass) first letter need to be uppercase
     * @return PokemonType object
     */
    public PokemonType getPokemonTypeFromName(String typeName) {
        try {
            JsonNode rootNode = mapper.readTree(pokemonTypeFile);
            return mapper.convertValue(rootNode.get(typeName), PokemonType.class);
        } catch (IOException e) {
            log.info("Object not found!");
        }
        return null;
    }

    /**
     * This function is used in Pokemon Creation. It will find the Pokemon's corresponse types in
     * pokemonType.json and pokemonStats with function in PokemonSpecies, this function will return
     * a Pokemon with corresponse type and stats
     *
     * @param pokedex pokemon identifier
     * @return Pokemon object
     */
    public Pokemon generatePokemon(String pokedex) {
        Set<PokemonType> type = new HashSet<>();
        Set<Skill> skill = new HashSet<>();
        PokemonSpecies ps = new PokemonSpecies(PokemonStat.getPokemonName(pokedex));

        try {
            JsonNode rootNode = mapper.readTree(pokemonFile);
            JsonNode pokeTypeNode = rootNode.get(PokemonStat.getPokemonName(pokedex));
            Iterator<JsonNode> elements = pokeTypeNode.elements();
            while (elements.hasNext()) {
                type.add(getPokemonTypeFromName(elements.next().asText()));
            }
        } catch (IOException e) {
            log.info("Object not found!");
        }

        Pokemon pokemon =
                new PokemonBuilder()
                        .withTypes(type)
                        .withSkills(skill)
                        .withCurLV(PokemonStat.getChallengeLevel(pokedex))
                        .withName(ps.getSpeciesName())
                        .withPokemonSpecies(ps)
                        .build();
        pokemon.setImageUrl(PokemonStat.getUrl(pokedex));

        return pokemon;
    }
}
