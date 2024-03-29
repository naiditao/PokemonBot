package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.controller.StartingPokemons;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.bson.types.ObjectId;

/**
 * MainViewCommand is a discord bot command that allows user to choose a pokemon at the start of a
 * game.
 */
@Slf4j
public class MainViewCommand implements SlashCommandHandler, StringSelectHandler, ButtonHandler {

    static final String NAME = "startnewgame";
    /** startingPokemons for creating starting pokemons for player. */
    @Inject StartingPokemons startingPokemons;
    /** the playerCOntroller for managing players. */
    @Inject PlayerController playerController;
    /** the locationCOntroller for managing locations. */
    @Inject LocationController locationController;

    @Inject MoveCommand moveCommand;
    @Inject ShopCommand shopCommand;
    @Inject HealCommand healCommand;
    @Inject CatchCommand catchCommand;
    @Inject CheckPokeCommand checkPokeCommand;

    /** Constructor for MainViewCommand. */
    @Inject
    public MainViewCommand() {}
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
    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Access main commands of the Pokémon bot");
    }
    /**
     * Handles slash command interactions for choosing a starting pokemon.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /mainview");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Welcome to the Pokemon World!");

        StringSelectMenu.Builder pokemons = StringSelectMenu.create(NAME);
        pokemons.setPlaceholder("Please Select Your Companion:");

        for (Pokemon pokemon : startingPokemons.getStartingPokemons()) {
            String name = pokemon.getName();
            Objects.requireNonNull(name);
            pokemons.addOption(name, name);
        }

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addEmbeds(embedBuilder.build());
        messageCreateBuilder.addActionRow(pokemons.build());

        event.reply(messageCreateBuilder.build()).setEphemeral(true).queue();
    }
    /**
     * Handles button interactions for choosing a starting pokemon.
     *
     * @param event The ButtonInteractionEvent.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getSelectedOptions().get(0).getValue();
        log.info(response);
        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);

        for (Pokemon pokemon : startingPokemons.getStartingPokemons()) {
            log.info(pokemon.getName());
            if (response.compareTo(pokemon.getName()) == 0) {
                player.addPokemon(pokemon);
                String info = String.format("You have got %s", pokemon.getName());
                Objects.requireNonNull(info);
                event.reply(info).queue();
                break;
            }
        }

        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);
        townView(location, event.getChannel());
    }

    /**
     * Method allows user to select move or shop or heal in a town.
     *
     * @param location - location of player
     * @param channel - MessageChannel
     */
    public void townView(Location location, MessageChannel channel) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        String titile = String.format("You're at %s", location.getName());
        Objects.requireNonNull(titile);
        embedBuilder.setTitle(titile);
        embedBuilder.setDescription("Please select what do you want to do next:");

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addEmbeds(embedBuilder.build());
        messageCreateBuilder.addActionRow(
                Button.primary(getName() + ":move", "Move"),
                Button.secondary(getName() + ":shop", "Shop"),
                Button.success(getName() + ":heal", "Heal"),
                Button.danger(getName() + ":check", "Check"));
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    /**
     * Method allows user to select move or catch in wild.
     *
     * @param location - location of player
     * @param channel - MessageChannel
     */
    public void wildView(Location location, MessageChannel channel) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        String titile = String.format("You're at %s", location.getName());
        Objects.requireNonNull(titile);
        embedBuilder.setTitle(titile);
        embedBuilder.setDescription("Please select what do you want to do next:");

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addEmbeds(embedBuilder.build());
        messageCreateBuilder.addActionRow(
                Button.primary(getName() + ":move", "Move"),
                Button.success(getName() + ":catch", "Catch"),
                Button.danger(getName() + ":check", "Check"));
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    /**
     * Handle buttons to deal with user's actions.
     *
     * @param event - ButtonInteractionEvent
     */
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);

        switch (event.getButton().getLabel()) {
            case "Move":
                event.reply("Select where would you like to move to").queue();
                moveCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Shop":
                event.reply("Select which item would you like to purchase").queue();
                shopCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Heal":
                event.reply("Heal Pokemon").queue();
                healCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Catch":
                event.reply("Catch Pokemon").queue();
                catchCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            case "Check":
                event.reply("What would you like to check: ").queue();
                checkPokeCommand.onSlashCommandInteraction(event.getChannel(), player);
                break;

            default:
                break;
        }
    }
}
