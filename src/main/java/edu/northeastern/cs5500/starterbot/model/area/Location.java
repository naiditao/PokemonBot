package edu.northeastern.cs5500.starterbot.model.area;

import edu.northeastern.cs5500.starterbot.model.Model;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Location implements Model {
    ObjectId id;
    String name;
    LocationType locationType;
    // key: pokedex number; value: appearance rate
    // if empty, there are no wild pokemon in the area
    Map<String, Integer> wildPokemon = new HashMap<>();

    public Location() {}

    public Location(String name, LocationType locationType) {
        this.name = name;
        this.locationType = locationType;
    }

    public Location(String name, LocationType locationType, Map<String, Integer> wildPokemon) {
        this.name = name;
        this.locationType = locationType;
        this.wildPokemon = wildPokemon;
    }
}
