package edu.northeastern.PlayerPackage;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.player_package.*;
import org.junit.jupiter.api.Test;

class ItemFactoryTest {

    @Test
    void createHealPotion() {
        Item item = ItemFactory.createItem("HealPotion", ItemEffect.SMALL);
        assertTrue(item instanceof HealPotion);
        assertEquals("HealPotion", item.getType());
        assertEquals(ItemEffect.SMALL, item.getEffect());
    }

    @Test
    void createPokeBall() {
        Item item = ItemFactory.createItem("PokeBall", ItemEffect.MEDIUM);
        assertTrue(item instanceof PokeBall);
        assertEquals("PokeBall", item.getType());
        assertEquals(ItemEffect.MEDIUM, item.getEffect());
    }

    @Test
    void createExpCandy() {
        Item item = ItemFactory.createItem("ExpCandy", ItemEffect.SMALL);
        assertTrue(item instanceof ExpCandy);
        assertEquals("ExpCandy", item.getType());
        assertEquals(ItemEffect.SMALL, item.getEffect());
    }

    @Test
    void createInvalidItem() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    ItemFactory.createItem("InvalidItem", ItemEffect.MEDIUM);
                });
    }
}
