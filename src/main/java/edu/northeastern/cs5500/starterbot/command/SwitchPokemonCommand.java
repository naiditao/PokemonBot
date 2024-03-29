package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.Iterator;
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
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.bson.types.ObjectId;

/**
 * PokemonViewCommand represents a discord bot command that allows user to check pokemons in player
 * inventory.
 */
@Slf4j
public class SwitchPokemonCommand implements StringSelectHandler {
    static final String NAME = "switchpokemon";
    /** The PlayerController for managing players. */
    @Inject PlayerController playerController;

    @Inject LocationController locationController;
    /**
     * Constructor for PokemonViewCommand,
     *
     * @param playerController - PlayerController of PokemonViewCommand.
     */
    @Inject
    public SwitchPokemonCommand() {}

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
        return Commands.slash(getName(), "Access pokemon in player's inventory");
    }

    /**
     * Handles slash command interactions for checking pokémons in inventory.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    @SuppressWarnings("null")
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /switchPokemon");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Switch Team Member");
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        StringSelectMenu.Builder pokemons = StringSelectMenu.create(NAME);

        if (player.getPokemonInventory().isEmpty()) {
            embedBuilder.setDescription("You do not have pokémons.");
            backToMainMenu(location, channel);
        } else {
            embedBuilder.setDescription("Choose which pokemon would you like to bring: ");

            pokemons.setPlaceholder("Select Pokemon:");

            for (Pokemon pokemon : player.getPokemonInventory()) {
                pokemons.addOption(pokemon.getName(), "Bring:" + pokemon.getId().toString());
            }
        }
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addEmbeds(embedBuilder.build());
        messageCreateBuilder.addActionRow(pokemons.build());
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    @SuppressWarnings("null")
    public void leavePokemon(MessageChannel channel, Player player) {
        log.info("event: /switchPokemon");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Switch Team Member");

        embedBuilder.setDescription("Choose which pokemon would you like to left behind: ");

        StringSelectMenu.Builder pokemons = StringSelectMenu.create(NAME);
        pokemons.setPlaceholder("Select Pokemon:");

        for (Pokemon pokemon : player.getTeams().getTeam()) {
            pokemons.addOption(pokemon.getName(), "Leave:" + pokemon.getId().toString());
        }
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addEmbeds(embedBuilder.build());
        messageCreateBuilder.addActionRow(pokemons.build());
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    public void backToMainMenu(Location location, MessageChannel channel) {
        MainViewCommand mainViewCommand = new MainViewCommand();
        if (location.getLocationType().equals(LocationType.TOWN)) {
            mainViewCommand.townView(location, channel);
        } else {
            mainViewCommand.wildView(location, channel);
        }
    }

    @Override
    @SuppressWarnings("null")
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0).split(":")[1];
        final String type = event.getInteraction().getValues().get(0).split(":")[0];
        Objects.requireNonNull(response);
        Objects.requireNonNull(type);
        ObjectId pokemonId = new ObjectId(response);

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        switch (type) {
            case "Bring":
                for (Pokemon pokemon : player.getPokemonInventory()) {
                    if (pokemonId.equals(pokemon.getId())) {
                        player.getTeams().getTeam().add(pokemon);
                        event.reply(String.format("%s has join your team", pokemon.getName()))
                                .queue();
                    }
                }
                if (player.getTeams().getTeam().size() > 6) {
                    leavePokemon(event.getChannel(), player);
                }
                break;

            case "Leave":
                Iterator<Pokemon> iterator = player.getTeams().getTeam().iterator();
                while (iterator.hasNext()) {
                    Pokemon pokemon = iterator.next();
                    if (pokemonId.equals(pokemon.getId())) {
                        iterator.remove();
                        player.addPokemon(pokemon);
                        event.reply(String.format("%s has left in warehouse", pokemon.getName()))
                                .queue();
                    }
                }
                backToMainMenu(location, event.getChannel());
                break;

            default:
                break;
        }
    }
}
