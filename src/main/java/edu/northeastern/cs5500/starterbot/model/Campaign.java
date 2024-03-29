package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Campaign implements Model {
    ObjectId id;
    final Teams myTeam;
    final Teams enemyTeam;
    int enemyDefeat;
    int totalEnemy;
}
