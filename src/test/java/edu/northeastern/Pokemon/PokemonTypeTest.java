package edu.northeastern.Pokemon;

import static org.junit.Assert.*;

import edu.northeastern.cs5500.starterbot.model.pokemon.Effectiveness;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonType;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PokemonTypeTest {
    @Test
    void EffectivenessTest() {
        Set<Integer> empty = new HashSet<>();

        Set<Integer> normalWeakAgainst = new HashSet<>();
        normalWeakAgainst.add(6);
        normalWeakAgainst.add(9);

        Set<Integer> normalNoDamageAgainst = new HashSet<>();
        normalNoDamageAgainst.add(8);

        PokemonType normal =
                new PokemonType(1, "Normal", empty, normalWeakAgainst, normalNoDamageAgainst);

        Set<Integer> fightingStrongAgainst = new HashSet<>();
        fightingStrongAgainst.add(1);
        fightingStrongAgainst.add(6);
        fightingStrongAgainst.add(9);
        fightingStrongAgainst.add(15);
        fightingStrongAgainst.add(17);

        Set<Integer> fightingWeakAgainst = new HashSet<>();
        fightingWeakAgainst.add(3);
        fightingWeakAgainst.add(4);
        fightingWeakAgainst.add(7);
        fightingWeakAgainst.add(14);
        fightingWeakAgainst.add(18);

        PokemonType fighting =
                new PokemonType(
                        2,
                        "Fighting",
                        fightingStrongAgainst,
                        fightingWeakAgainst,
                        normalNoDamageAgainst);

        Set<PokemonType> testSet1 = new HashSet<>();
        testSet1.add(normal);

        assertEquals(
                Effectiveness.DOUBLE_EFFECT, fighting.determineEffectiveness(fighting, testSet1));

        Set<Integer> rockStrongAgainst = new HashSet<>();
        rockStrongAgainst.add(3);
        rockStrongAgainst.add(7);
        rockStrongAgainst.add(10);
        rockStrongAgainst.add(15);

        Set<Integer> rockWeakAgainst = new HashSet<>();
        rockWeakAgainst.add(2);
        rockWeakAgainst.add(5);
        rockWeakAgainst.add(9);

        PokemonType rock = new PokemonType(6, "rock", rockStrongAgainst, rockWeakAgainst, empty);

        Set<PokemonType> testSet2 = new HashSet<>();
        testSet2.add(rock);
        testSet2.add(normal);

        assertEquals(Effectiveness.QUAD_EFFECT, rock.determineEffectiveness(fighting, testSet2));
    }
}
