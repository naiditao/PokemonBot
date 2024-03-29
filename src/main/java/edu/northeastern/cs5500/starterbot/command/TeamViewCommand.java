package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.ArrayList;
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

/** TeamViewCommand represents a discord bot command that allows user to check pokemons in team. */
@Slf4j
public class TeamViewCommand {

    static final String NAME = "teamview";
    /** The PlayerController for managing players. */
    @Inject PlayerController playerController;

    @Inject LocationController locationController;

    /**
     * Constructor for TeamViewCommand,
     *
     * @param playerController - PlayerController of TeamViewCommand.
     */
    @Inject
    public TeamViewCommand(PlayerController playerController) {
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
        return Commands.slash(getName(), "Access pokemon in player's team");
    }

    /**
     * Handles slash command interactions for checking pokémons in player team.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /teamview");

        ArrayList<Pokemon> teams = player.getTeams().getTeam();
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        if (teams.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Your Team");
            embedBuilder.setDescription("Your team does not have pokémons.");
            MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
            messageCreateBuilder.addEmbeds(embedBuilder.build());
            channel.sendMessage(messageCreateBuilder.build()).queue();
        } else {
            for (Pokemon pokemon : teams) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Your Team");
                int[] baseStats = pokemon.getPokemonSpecies().getBaseInfo();
                String stats =
                        String.format(
                                "CurrentHP: %d\nAttack: %d\nDefense: %d\nSpecial Attack: %d\nSpecial Defense: %d\nSpeed: %d",
                                pokemon.getCurHP(),
                                baseStats[1],
                                baseStats[2],
                                baseStats[3],
                                baseStats[4],
                                baseStats[5]);
                embedBuilder.setImage(pokemon.getImageUrl());
                embedBuilder.addField("Name", pokemon.getName(), false);
                embedBuilder.addField("Base Status", stats, false);
                MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
                messageCreateBuilder.addEmbeds(embedBuilder.build());
                channel.sendMessage(messageCreateBuilder.build()).queue();
            }
        }

        MainViewCommand mainViewCommand = new MainViewCommand();
        if (location.getLocationType().equals(LocationType.TOWN)) {
            mainViewCommand.townView(location, channel);
        } else {
            mainViewCommand.wildView(location, channel);
        }
    }
}
