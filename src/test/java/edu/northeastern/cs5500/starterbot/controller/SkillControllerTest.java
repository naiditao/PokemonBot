package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import org.junit.Test;

public class SkillControllerTest {
    SkillController skillController = new SkillController();
    PokemonTypeController pokemonTypeController = new PokemonTypeController();

    @Test
    public void getSkillFromNameTest() {
        Skill testSkill1 = skillController.getSkillFromName("Tackle");
        assertEquals(testSkill1.getPower(), 35);
        assertEquals(testSkill1.getSkillId(), "Tackle");
        assertEquals(testSkill1.getSkillType(), SkillType.PHYSICAL);
        assertEquals(testSkill1.getType(), pokemonTypeController.getPokemonTypeFromName("Normal"));

        Skill testSkill2 = skillController.getSkillFromName("Ember");
        assertEquals(testSkill2.getPower(), 40);
        assertEquals(testSkill2.getEffect(), StatusEffect.BURN);
        assertEquals(testSkill2.getSkillId(), "Ember");
        assertEquals(testSkill2.getChanceForStatus(), 10);
        assertEquals(testSkill2.getType(), pokemonTypeController.getPokemonTypeFromName("Fire"));
        assertEquals(testSkill2.getSkillType(), SkillType.SPECIAL);
    }

    @Test
    public void fillSkillSetTest() {
        Pokemon test = pokemonTypeController.generatePokemon("0001");
        test.setCurLV(15);
        skillController.fillSkillSet(test);

        assertEquals(3, test.getSkills().size());
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Tackle")));
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Leech Seed")));
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Vine Whip")));

        test = pokemonTypeController.generatePokemon("0001");
        test.setCurLV(50);
        skillController.fillSkillSet(test);
        assertEquals(4, test.getSkills().size());
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Solar Beam")));
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Sleep Powder")));
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Razor Leaf")));
        assertFalse(test.getSkills().contains(skillController.getSkillFromName("Tackle")));
    }

    @Test
    public void addSkillAtLevelUpTest() {
        Pokemon test = pokemonTypeController.generatePokemon("0001");
        test.setCurLV(12);
        skillController.fillSkillSet(test);
        assertFalse(test.getSkills().contains(skillController.getSkillFromName("Vine Whip")));

        test.levelUp();
        assertTrue(test.getSkills().contains(skillController.getSkillFromName("Vine Whip")));
    }
}
