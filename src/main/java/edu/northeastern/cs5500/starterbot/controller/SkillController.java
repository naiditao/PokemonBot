package edu.northeastern.cs5500.starterbot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkillController {

    PokemonTypeController pokemonTypeController = new PokemonTypeController();

    File skillFile =
            new File("/workspaces/bot-corgi/src/main/resources/skill.json").getAbsoluteFile();
    File learnSetFile =
            new File("/workspaces/bot-corgi/src/main/resources/pokemonLearnSet.json")
                    .getAbsoluteFile();
    ObjectMapper mapper = new ObjectMapper();

    @Inject
    public SkillController() {
        /* This is an empty constructor used for inject. */
    }

    /**
     * This function will take in the name of the skill and search for coresponse entry in
     * skill.json, if it find the specific entry it will return the skill object, if it cannot find
     * the specific entry it will return a null
     *
     * @param name of the skill (with space)
     * @return Skill Object
     */
    public Skill getSkillFromName(String name) {

        try {
            JsonNode rootNode = mapper.readTree(skillFile);
            Skill skill = mapper.convertValue(rootNode.get(name), Skill.class);
            skill.setType(pokemonTypeController.getPokemonTypeFromName(skill.getTypeName()));
            return skill;
        } catch (IOException e) {
            log.info("Object not found!");
        }

        return null;
    }

    /**
     * This function fills the input Pokemon Skill Set with information in pokemonLearnSet.json
     * Skill number for loading is up to 4, skill that has higher level requirement will be loaded
     * first
     *
     * @param pokemon Pokemon Object
     */
    public void fillSkillSet(Pokemon pokemon) {
        if (!Objects.isNull(getLearnMap(pokemon))) {
            for (Entry<String, Integer> entry : getLearnMap(pokemon).entrySet()) {
                if (pokemon.getSkills().size() == 4) break;
                if (entry.getValue() <= pokemon.getCurLV()) {
                    log.info(entry.getKey());
                    pokemon.getSkills().add(getSkillFromName(entry.getKey()));
                }
            }
        } else {
            pokemon.getSkills().add(getSkillFromName("Tackle"));
        }
    }

    /**
     * This function return the map, the key is name for the Skill and the value is the level for
     * the pokemon to gain that skill
     *
     * @param pokemon
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getLearnMap(Pokemon pokemon) {
        try {
            JsonNode rootNode = mapper.readTree(learnSetFile);
            return mapper.convertValue(rootNode.get(pokemon.getName()), Map.class);
        } catch (IOException e) {
            log.info("Object not found!");
        }
        return Collections.emptyMap();
    }

    /**
     * This function is called every time the pokemon level up, if pokemon's curLv after level up is
     * in the learnMap's value set, it will gain a skill, otherwise nothing will happened
     *
     * @param pokemon
     */
    public void addSkillAtLevelUp(Pokemon pokemon) {
        if (!Objects.isNull(getLearnMap(pokemon))) {
            for (Entry<String, Integer> entry : getLearnMap(pokemon).entrySet()) {
                if (entry.getValue() == pokemon.getCurLV()) {
                    pokemon.getSkills().add(getSkillFromName(entry.getKey()));
                    break;
                }
            }
        }
    }
}
