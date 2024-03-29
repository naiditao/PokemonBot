package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bson.types.ObjectId;

/**
 * HealCommand represents a discord bot command that allows users heal their pokemon in teams after
 * battle.
 */
@Slf4j
public class HealCommand implements StringSelectHandler {
    static final String NAME = "heal";
    /** The PlayerController for managing players. */
    private final PlayerController playerController;
    /** The LocationController for managing players. */
    @Inject LocationController locationController;
    /**
     * Constructor for HealCommand.
     *
     * @param playerController - PlayerController of HealCommand,
     */
    @Inject
    public HealCommand(PlayerController playerController) {
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
        return Commands.slash(getName(), "Heal a selected pokemon");
    }

    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    /**
     * Handles slash command interactions for healing the selected Pokemon.
     *
     * @param channel - MessageChannel for delivering message
     * @param player - player of the game
     */
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /heal");

        ArrayList<Pokemon> team = player.getTeams().getTeam();
        if (team.isEmpty()) {
            channel.sendMessage("You do not have any Pokémons.").queue();
        } else {
            StringSelectMenu.Builder pokemons = StringSelectMenu.create(NAME);
            pokemons.setPlaceholder("Select a Pokémon");

            boolean needHealed = false;
            for (Pokemon pokemon : team) {
                if (pokemon.getCurHP() < pokemon.getCurMaxHp()) {
                    pokemons.addOption(pokemon.getName(), pokemon.getName());
                    needHealed = true;
                }
            }
            if (needHealed) {
                channel.sendMessage("Please pick a Pokémon to heal:")
                        .addActionRow(pokemons.build())
                        .queue();
            } else {
                channel.sendMessage("You do not have any Pokémon that needs to be healed.").queue();
            }
        }
    }
    /**
     * Handles button interactions for healing selected Pokémon.
     *
     * @param event The ButtonInteractionEvent.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        String pokemonName = event.getSelectedOptions().get(0).getValue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        ArrayList<Pokemon> team = player.getTeams().getTeam();

        for (Pokemon pokemon : team) {
            if (pokemonName.compareTo(pokemon.getName()) == 0) {
                pokemon.setCurHP(pokemon.getCurMaxHp());
            }
        }

        embedBuilder.setDescription(pokemonName + " has been healed to maximum HP.");

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

        MainViewCommand mainViewCommand = new MainViewCommand();
        mainViewCommand.townView(location, event.getChannel());
    }
}
