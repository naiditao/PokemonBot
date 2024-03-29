package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * CheckpokeCommand represents a discord bot command that allows user to check a pokemon's basic
 * information in inventory.
 */
@Slf4j
public class CheckPokeCommand implements ButtonHandler {
    static final String NAME = "checkpoke";
    /** The PlayerController for managing players. */
    private final PlayerController playerController;

    @Inject TeamViewCommand teamViewCommand;
    @Inject PokemonViewCommand pokemonViewCommand;
    @Inject ItemViewCommand itemViewCommand;
    @Inject SwitchPokemonCommand switchPokemonCommand;

    /**
     * Constructor for CheckPokeCommand,
     *
     * @param playerController - PlayerController of CheckPokeCommand.
     */
    @Inject
    public CheckPokeCommand(PlayerController playerController) {
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
        return Commands.slash(getName(), "Get information of a Pokémon");
    }

    /**
     * Handles slash command interactions for checking a pokémons in player inventory.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /checkpoke");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Check your status:");

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(getName() + ":team", "Team"),
                        Button.secondary(getName() + ":warehouse", "Warehouse"),
                        Button.success(getName() + ":inventory", "Inventory"),
                        Button.danger(getName() + ":switch", "Switch"));
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    /**
     * Handles slash command interactions for checking a pokemon in inventory.
     *
     * @param event - StringSelectInteractionEvent
     */
    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);

        switch (event.getButton().getLabel()) {
            case "Team":
                event.reply("Your team member: ").queue();
                teamViewCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Warehouse":
                event.reply("Your pokemon warehouse: ").queue();
                pokemonViewCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Inventory":
                event.reply("Your item inventory: ").queue();
                itemViewCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Switch":
                event.reply("Switch Pokemon").queue();
                switchPokemonCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            default:
                break;
        }
    }
}
