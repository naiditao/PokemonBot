package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.BattleController;
import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.controller.PokemonTypeController;
import edu.northeastern.cs5500.starterbot.controller.SkillController;
import edu.northeastern.cs5500.starterbot.model.Battle;
import edu.northeastern.cs5500.starterbot.model.Campaign;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.Teams;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.PokemonStat;
import java.util.ArrayList;
import java.util.Objects;
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
import org.bson.types.ObjectId;

/**
 * The CatchCommand class represents a Discord bot command for catching Pokemon in a wild area. It
 * implements both SlashCommandHandler and ButtonHandler to handle slash commands and button
 * interactions.
 */
@Slf4j
public class CatchCommand implements ButtonHandler {

    static final String NAME = "catch";

    /** The LocationController for managing locations. */
    private final LocationController locationController;
    /** The PlayerController for managing players. */
    private final PlayerController playerController;
    /** The PokemonTypeController for managing Pokemon types. */
    private final PokemonTypeController pokemonTypeController;
    /** The BattleController for managing battles. */
    private final BattleController battleController;
    /** The TestBattleView for testing battle views. */
    private final TestBattleView testBattleView;

    @Inject SkillController skillController;

    /**
     * Constructs a new catch command with the specified fields.
     *
     * @param locationController The controller for managing locations.
     * @param playerController The controller for managing players.
     * @param pokemonTypeController The controller for managing pokemons.
     * @param battleController The controller for managing battles.
     * @param testBattleView The Battle view test scene.
     */
    @Inject
    public CatchCommand(
            LocationController locationController,
            PlayerController playerController,
            PokemonTypeController pokemonTypeController,
            BattleController battleController,
            TestBattleView testBattleView) {
        this.locationController = locationController;
        this.playerController = playerController;
        this.pokemonTypeController = pokemonTypeController;
        this.battleController = battleController;
        this.testBattleView = testBattleView;
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
        return Commands.slash(getName(), "Catch a Pokemon at current location");
    }

    /**
     * Handles slash command interactions for catching Pokemon.
     *
     * @param event The SlashCommandInteractionEvent.
     */
    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /catch");

        Location playerLocation = locationController.getLocation(player.getLocationId());

        String pokedex = LocationController.randomEncounter(playerLocation.getWildPokemon());
        Pokemon caught = pokemonTypeController.generatePokemon(pokedex);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("You encouter a %s!", caught.getName()));
        embedBuilder.setImage(PokemonStat.getUrl(pokedex));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(getName() + ":" + pokedex, "Catch"),
                        Button.danger(getName() + ":cancel", "Cancel"));
        messageCreateBuilder = messageCreateBuilder.setContent("Catch");
        messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    /**
     * Handles button interactions for catching Pokemon.
     *
     * @param event The ButtonInteractionEvent.
     */
    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        Location playerLocation = locationController.getLocation(player.getLocationId());
        String pokedex = event.getButton().getId().split(":")[1];
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        if (event.getButton().getLabel().endsWith("Catch")) {
            Pokemon caught = pokemonTypeController.generatePokemon(pokedex);
            skillController.fillSkillSet(caught);
            ArrayList<Pokemon> team = new ArrayList<>();
            team.add(caught);

            battleController.startNewCampaign(player, player.getTeams(), new Teams(team));
            Campaign campaign = battleController.getCampaignForObjectId(player.getCurCampaignId());

            battleController.startNewBattle(
                    player,
                    campaign.getMyTeam().getTeam().get(0),
                    campaign.getEnemyTeam().getTeam().get(0));
            Battle battle = battleController.getBattleForObjectId(player.getCurBattleId());

            testBattleView.oneTurn(event.getChannel(), battle, campaign, location);

            event.reply("Enter Battle.").queue();
        } else {
            event.reply("Pokemon Escape").queue();
            MainViewCommand mainViewCommand = new MainViewCommand();
            mainViewCommand.wildView(playerLocation, event.getChannel());
        }
    }
}
