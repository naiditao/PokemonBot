package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.Status;

public enum StatusEffect {
    BURN {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            int damage = pokemon.getCurMaxHp() / 16;
            pokemon.setCurHP(pokemon.getCurHP() - damage);
            return String.format("%s takes %d damage due to BURN", pokemon.getName(), damage);
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            return null;
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    },
    PARALYZED {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            return null;
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            if (rand < 25) {
                return String.format("%s has skip turn due to Paralyzed", pokemon.getName());
            }
            return null;
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    },
    POSIONED {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            int damage = pokemon.getCurMaxHp() / 16;
            pokemon.setCurHP(pokemon.getCurHP() - damage);
            return String.format("%s takes %d damage due to Posion", pokemon.getName(), damage);
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            return null;
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    },

    FORZEN {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            return null;
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            return String.format("%s has skip turn due to Forzen", pokemon.getName());
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    },

    FLINCH {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            return null;
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            return String.format("%s has skip turn due to Flinch", pokemon.getName());
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    },

    SLEEP {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            return null;
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            return String.format("%s has skip turn due to Sleep", pokemon.getName());
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    },

    LEECHSEED {
        public String preActionEffect(Pokemon pokemon, Pokemon opponent) {
            int damage = pokemon.getCurMaxHp() / 16;
            pokemon.setCurHP(pokemon.getCurHP() - damage);
            opponent.setCurHP(Math.min((opponent.getCurHP() + damage), opponent.getCurMaxHp()));
            return String.format(
                    "%s takes %d damage due to Leech Seed%n%s regenerate %d HP due to Leech Seed",
                    pokemon.getName(), damage, opponent.getName(), damage);
        }

        public String skipTurn(Pokemon pokemon, int rand) {
            return null;
        }

        public void effectFinish(Status status, Pokemon pokemon) {
            status.setTurnRemain(status.getTurnRemain() - 1);
        }
    };

    public abstract String preActionEffect(Pokemon pokemon, Pokemon opponent);

    public abstract String skipTurn(Pokemon pokemon, int rand);

    public abstract void effectFinish(Status status, Pokemon pokemon);
}
