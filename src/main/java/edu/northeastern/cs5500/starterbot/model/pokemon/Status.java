package edu.northeastern.cs5500.starterbot.model.pokemon;

import edu.northeastern.cs5500.starterbot.controller.StatusEffect;
import lombok.Data;

@Data
public class Status {
    final StatusEffect effect;
    int turnRemain = 5;
}
