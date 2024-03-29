package edu.northeastern.cs5500.starterbot.model.pokemon;

import edu.northeastern.cs5500.starterbot.controller.SkillType;
import edu.northeastern.cs5500.starterbot.controller.StatusEffect;
import lombok.Data;

@Data
public class Skill {
    String skillId;
    int power;
    String typeName;
    PokemonType type;
    SkillType skillType;
    StatusEffect effect;
    int chanceForStatus;

    public Skill() {}
}
