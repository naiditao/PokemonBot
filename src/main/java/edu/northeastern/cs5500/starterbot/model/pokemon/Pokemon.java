package edu.northeastern.cs5500.starterbot.model.pokemon;

import edu.northeastern.cs5500.starterbot.controller.SkillController;
import edu.northeastern.cs5500.starterbot.model.Model;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bson.types.ObjectId;

/**
 * The {@code Pokemon} class represents a creature in the Pokemon world. It implements the {@code
 * Model} interface. It includes information such as types, skills, current health points (HP),
 * level (LV), experience points (EXP), species, statistics, name, and image URL.
 */
@Data
@EqualsAndHashCode
public class Pokemon implements Model {
    private ObjectId id; // MongoDB automatic generate this.
    private Set<PokemonType> types; // The set of Pokemon types(grass fire...) of the Pokemon.
    private Set<Skill> skills; // The set of moves that the Pokemon can use.
    private int curHP; // The current health point of the Pokemon.
    private int curLV; // The current level of the Pokemon.
    private int curEXP; // The current experience points of the Pokemon.
    private PokemonSpecies pokemonSpecies; // The species of the Pokemon.
    private int curMaxHp; // The current max hp of the Pokemon.
    private int curAtt; // The current attack of the Pokemon.
    private int curDef; // The current defense of the Pokemon.
    private int curSpa; // The current special attack of the Pokemon.
    private int curSpd; // The current special defense of the Pokemon.
    private int curSpeed; // The current speed of the Pokemon.
    @NonNull private String name; // The name of the Pokemon given by the Player.
    Set<Status> status = new HashSet<>();
    private String imageUrl; // The URL(From Pokemon Database website) for the image to display the
    // Pokemon.

    private static final int MAX_HP_INDEX = 0;
    private static final int ATTACK_INDEX = 1;
    private static final int DEFENSE_INDEX = 2;
    private static final int SPECIAL_ATTACK_INDEX = 3;
    private static final int SPECIAL_DEFENSE_INDEX = 4;
    private static final int SPEED_INDEX = 5;
    private static final int INITIAL_EXP = 0;
    private static final int MAX_EXP = 100;

    /**
     * Static factory method to obtain a new instance of the {@code PokemonBuilder}. This method is
     * used to start the process of building a new Pokemon instance using the builder pattern.
     *
     * @return A new instance of the {@code PokemonBuilder}.
     */
    public static PokemonBuilder builder() {
        return new PokemonBuilder();
    }

    /**
     * Constructs a new Pokemon with the specified types, skills, current HP, current level, name,
     * and species.
     *
     * @param types The set of Pokemon types.
     * @param skills The set of skills.
     * @param curHP The current health points.
     * @param curLV The current level.
     * @param name The name of the Pokemon.
     * @param pokemonSpecies The species of the Pokemon.
     */
    Pokemon(
            Set<PokemonType> types,
            Set<Skill> skills,
            int curLV,
            String name,
            PokemonSpecies pokemonSpecies) {
        this.types = types;
        this.skills = skills;
        this.curLV = curLV;
        this.curEXP = INITIAL_EXP;
        this.name = name;
        this.pokemonSpecies = pokemonSpecies;
        int[] baseInfo = pokemonSpecies.getBaseInfo();
        updateStat(baseInfo);
        this.curHP = this.curMaxHp;
        id = new ObjectId();
    }

    /** Increases the level of the Pokemon, updating its statistics based on the new level. */
    public void levelUp() {
        this.curLV += 1;
        this.curEXP = Math.max(this.curEXP - MAX_EXP, 0);
        String pokedex = this.pokemonSpecies.getPokedex();
        int[] levelUpStat = PokemonStat.levelUp(pokedex, this.curLV);
        SkillController skillController = new SkillController();
        skillController.addSkillAtLevelUp(this);
        updateStat(levelUpStat);
    }

    /**
     * Add EXP to this Pokemon. If current exp exceeds MAX, call level up.
     *
     * @param gainEXP The EXP added to this Pokemon.
     */
    public void addEXP(int gainEXP) {
        this.curEXP += gainEXP;
        while (this.curEXP > MAX_EXP) {
            levelUp();
        }
    }

    private void updateStat(int[] stat) {
        this.curMaxHp = stat[MAX_HP_INDEX];
        this.curAtt = stat[ATTACK_INDEX];
        this.curDef = stat[DEFENSE_INDEX];
        this.curSpa = stat[SPECIAL_ATTACK_INDEX];
        this.curSpd = stat[SPECIAL_DEFENSE_INDEX];
        this.curSpeed = stat[SPEED_INDEX];
    }
}
