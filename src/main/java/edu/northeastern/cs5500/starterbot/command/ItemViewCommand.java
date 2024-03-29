package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ItemController;
import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.bson.types.ObjectId;

/** ItemViewCommand is a discord bot command that allows users to check items in inventory. */
@Slf4j
public class ItemViewCommand {

    static final String NAME = "itemview";
    /** The PlayerController for managing players. */
    @Inject PlayerController playerController;
    /** The ItemController for managing items. */
    @Inject ItemController itemController;

    @Inject LocationController locationController;
    /**
     * Constructor for ItemViewCommand,
     *
     * @param itemController - ItemController of ItemViewCommand.
     * @param playerController - PlayerController of ItemViewCommand.
     */
    @Inject
    public ItemViewCommand(ItemController itemController, PlayerController playerController) {
        this.itemController = itemController;
        this.playerController = playerController;
    }
    /**
     * Gets the name of the command.
     *
     * @return The command name.
     */
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
        return Commands.slash(getName(), "Access items in player's item inventory");
    }

    /**
     * Handles slash command interactions for checking items in player's inventory.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /itemview");

        Map<String, Integer> inventory = player.getItemsNum();
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

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

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addEmbeds(embedBuilder.build());
        channel.sendMessage(messageCreateBuilder.build()).queue();

        MainViewCommand mainViewCommand = new MainViewCommand();
        if (location.getLocationType().equals(LocationType.TOWN)) {
            mainViewCommand.townView(location, channel);
        } else {
            mainViewCommand.wildView(location, channel);
        }
    }
}
