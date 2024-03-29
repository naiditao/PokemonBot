package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ItemController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Battle;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/** ItemCommand represents a discord bot command that allows users use items in the inventory. */
@Slf4j
public class ItemCommand implements SlashCommandHandler {

    static final String NAME = "item";
    /** The PlayerController for managing players. */
    @Inject PlayerController playerController;
    /** The ItemController for managing items. */
    @Inject ItemController itemController;
    /**
     * Constructor for ItemCommand,
     *
     * @param itemController - ItemController of ItemCommand.
     * @param playerController - PlayerController of ItemCommand.
     */
    @Inject
    public ItemCommand(ItemController itemController, PlayerController playerController) {
        this.itemController = itemController;
        this.playerController = playerController;
    }
    /**
     * Gets the name of the command.
     *
     * @return The command name.
     */
    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    /**
     * Gets the command data for registration with Discord.
     *
     * @return The command data.
     */
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Access items in player's inventory");
    }

    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    @Override
    /**
     * Handles slash command interactions for using items.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /item");

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);

        Map<String, Integer> inventory = player.getItemsNum();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Item Inventory");

        boolean emptyInventory = true;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                embedBuilder.addField(entry.getKey(), "Quantity: " + entry.getValue(), false);
                emptyInventory = false;
            }
        }

        if (emptyInventory) {
            embedBuilder.setDescription("Your inventory is empty.");
        }

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
    /**
     * Method for creating a MessageCreateBuilder
     *
     * @param player - player of this game.
     * @return a MessageCreateBuilder to deliver message of a player's items.
     */
    public MessageCreateBuilder useItemCommand(Player player) {
        Map<String, Integer> inventory = player.getItemsNum();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Select item to use: ");

        StringSelectMenu.Builder items = StringSelectMenu.create("battle");
        items.setPlaceholder("Select item to use: ");

        boolean emptyInventory = true;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                String itemName = entry.getKey();
                Objects.requireNonNull(itemName);
                items.addOption(itemName, "Item:" + itemName);
                emptyInventory = false;
            }
        }

        if (emptyInventory) {
            embedBuilder.setDescription("Your inventory is empty.");
        }

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder = messageCreateBuilder.addActionRow(items.build());
        messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
        return messageCreateBuilder;
    }
    /**
     * Handles slash command interactions for using items in inventory.
     *
     * @param response - name of selected item.
     * @param battle - battle which is in.
     * @param player - player of the game.
     * @return a string that shows which item is used by player.
     */
    public String onStringSelectInteraction(String response, Battle battle, Player player) {
        return itemController.useItem(
                player, battle.getMyPokemon(), response, battle.getEnemyPokemon());
    }
}
