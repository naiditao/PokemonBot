package edu.northeastern.cs5500.starterbot.model.pokemon;

import lombok.Getter;

public enum Effectiveness {
    NO_EFFECT(1, "It has not effect!", 0),
    QUARTER_EFFECT(2, "It's not very effective...", 0.25),
    HALF_EFFECT(3, "It's not very effective...", 0.5),
    FULL_EFFECT(4, "It's effective.", 1),
    DOUBLE_EFFECT(5, "It's super effective!", 2),
    QUAD_EFFECT(6, "It's super effective!", 4);

    @Getter private int effectId;
    @Getter private String text;
    @Getter private double effect;

    Effectiveness(int effectId, String text, double effect) {
        this.effectId = effectId;
        this.text = text;
        this.effect = effect;
    }
}
