package edu.northeastern.cs5500.starterbot.model.pokemon;

import java.util.Set;
import lombok.Data;

@Data
public class PokemonType {

    int typeId;
    String name;
    Set<Integer> strongAgainst;
    Set<Integer> weakAgainst;
    Set<Integer> noDamageAgainst;

    public PokemonType() {}

    public PokemonType(
            int typeId,
            String name,
            Set<Integer> strongAgainst,
            Set<Integer> weakAgainst,
            Set<Integer> noDamageAgainst) {
        this.typeId = typeId;
        this.name = name;
        this.strongAgainst = strongAgainst;
        this.weakAgainst = weakAgainst;
        this.noDamageAgainst = noDamageAgainst;
    }

    public Effectiveness determineEffectiveness(PokemonType attack, Set<PokemonType> defense) {

        int effectId = 4;

        for (PokemonType x : defense) {
            if (attack.getNoDamageAgainst().contains(x.getTypeId())) {
                return Effectiveness.NO_EFFECT;
            } else if (attack.getStrongAgainst().contains(x.getTypeId())) {
                effectId += 1;
            } else if (attack.getWeakAgainst().contains(x.getTypeId())) {
                effectId -= 1;
            }
        }

        for (Effectiveness x : Effectiveness.values()) {
            if (x.getEffectId() == effectId) {
                return x;
            }
        }

        return null;
    }
}
