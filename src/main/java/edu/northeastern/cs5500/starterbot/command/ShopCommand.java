package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ItemController;
import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bson.types.ObjectId;

/** ShopCommand is a discord bot command that allows users to shop items using pokecoins. */
@Slf4j
public class ShopCommand implements StringSelectHandler {
    static final String NAME = "shop";
    /** The PlayerController for managing players. */
    private final PlayerController playerController;
    /** The ItemController for managing items. */
    private final ItemController itemController;
    /** The LocationController for managing locations. */
    @Inject LocationController locationController;
    /**
     * Constructor for ShopCommand.
     *
     * @param playerController - controller for managing players.
     * @param itemController - controller for managing items.
     */
    @Inject
    public ShopCommand(PlayerController playerController, ItemController itemController) {
        this.playerController = playerController;
        this.itemController = itemController;
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
        return Commands.slash(getName(), "Allowing player buy items");
    }

    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    /**
     * Handles slash command interactions for purchasing items.
     *
     * @param channel - MessageChannel for delivering message
     * @param player - player of the game
     */
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /shop");

        StringSelectMenu.Builder store = StringSelectMenu.create(NAME);
        Map<String, Integer> itemList = ItemController.getItemPrices();
        for (Map.Entry<String, Integer> entry : itemList.entrySet()) {
            String itemName = entry.getKey();
            Integer price = entry.getValue();
            Objects.requireNonNull(itemName);
            store.addOption(itemName + " - " + price + " PokeCoins", itemName);
        }
        channel.sendMessage("Your PokeCoins: " + player.getPokeCoin() + "\nSelect an item to buy:")
                .addActionRow(store.build())
                .queue();
    }
    /**
     * Handles button interactions for purchasing items.
     *
     * @param event The ButtonInteractionEvent.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        String itemName = event.getSelectedOptions().get(0).getValue();
        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        if (player.getPokeCoin() < itemController.getItemPrice(itemName)) {
            event.reply("Purchase failed. Your balance is low.").setEphemeral(true).queue();
        } else {
            itemController.buyItem(player, itemName);
            int remainingPokeCoins = player.getPokeCoin();

            event.reply(
                            "Item purchased: "
                                    + itemName
                                    + ". Remaining PokeCoins: "
                                    + remainingPokeCoins)
                    .setEphemeral(true)
                    .queue();
        }

        MainViewCommand mainViewCommand = new MainViewCommand();
        mainViewCommand.townView(location, event.getChannel());
    }
}
