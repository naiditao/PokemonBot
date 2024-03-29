package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;

/** A enum class for skill type, each skill type will have different damage equation */
public enum SkillType {
    PHYSICAL {
        public int damage(
                Pokemon targetPokemon, Pokemon myPokemon, double effect, int random, Skill skill) {
            return (int)
                    (((myPokemon.getCurLV() * 0.4 + 2)
                                            * skill.getPower()
                                            * myPokemon.getCurAtt()
                                            / targetPokemon.getCurDef()
                                            / 50
                                    + 2)
                            * effect
                            * random
                            / 255);
        }
    },
    SPECIAL {
        public int damage(
                Pokemon targetPokemon, Pokemon myPokemon, double effect, int random, Skill skill) {
            return (int)
                    (((myPokemon.getCurLV() * 0.4 + 2)
                                            * skill.getPower()
                                            * myPokemon.getCurSpa()
                                            / targetPokemon.getCurSpd()
                                            / 50
                                    + 2)
                            * effect
                            * random
                            / 255);
        }
    },
    STATUS {
        public int damage(
                Pokemon targetPokemon, Pokemon myPokemon, double effect, int random, Skill skill) {
            return 0;
        }
    };

    public abstract int damage(
            Pokemon targetPokemon, Pokemon myPokemon, double effect, int random, Skill skill);
}
