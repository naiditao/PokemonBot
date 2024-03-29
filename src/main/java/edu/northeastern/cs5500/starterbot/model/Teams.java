package edu.northeastern.cs5500.starterbot.model;

import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Teams {
    @Getter ArrayList<Pokemon> team;
    @Getter @Setter int num;

    public Teams(ArrayList<Pokemon> team) {
        this.team = team;
        num = team.size();
    }
}
