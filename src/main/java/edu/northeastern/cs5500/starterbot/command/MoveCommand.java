package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
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
 * The MoveCommand class handles the move command to move the player to a different location. It
 * implements the SlashCommandHandler and StringSelectHandler interfaces to handle interaction
 * events.
 */
@Slf4j
public class MoveCommand implements StringSelectHandler {

    static final String NAME = "move";

    // The controller for managing locations.
    private final LocationController locationController;
    // The controller for managing players.
    private final PlayerController playerController;

    /**
     * Constructs a new move command with the specified LocationController and PlayerController.
     *
     * @param locationController The controller for managing locations.
     * @param playerController The controller for managing players.
     */
    @Inject
    public MoveCommand(LocationController locationController, PlayerController playerController) {
        this.locationController = locationController;
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
     * Gets the CommandData for the slash command, providing information about the command.
     *
     * @return The CommandData for the slash command.
     */
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Move the player to a different location");
    }

    /**
     * Handles the interaction event for the move command.
     *
     * @param event The SlashCommandInteractionEvent representing the interaction.
     */
    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    public void onSlashCommandInteraction(MessageChannel channel, Player player) {
        log.info("event: /move");

        Location playerLocation = locationController.getLocation(player.getLocationId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Your are at %s", playerLocation.getName()));
        embedBuilder.setImage(
                "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/5f5094a9-bbf3-44a3-ab0c-b2137a32a72b/d6s9gq7-e6637d09-bbac-4b3a-abeb-2d94afe5e21e.jpg/v1/fit/w_828,h_828,q_70,strp/pokemon___labeled_kanto_map_by_theartfridge_d6s9gq7-414w-2x.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTAyNCIsInBhdGgiOiJcL2ZcLzVmNTA5NGE5LWJiZjMtNDRhMy1hYjBjLWIyMTM3YTMyYTcyYlwvZDZzOWdxNy1lNjYzN2QwOS1iYmFjLTRiM2EtYWJlYi0yZDk0YWZlNWUyMWUuanBnIiwid2lkdGgiOiI8PTEwMjQifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6aW1hZ2Uub3BlcmF0aW9ucyJdfQ.6tIAjLg7yaha0IeMjdVVxT7yxpcaK88zoIWIG_mH3Ws");

        StringSelectMenu.Builder locations = StringSelectMenu.create(NAME);

        locations.setPlaceholder("Choose your destination");

        for (Location location : locationController.getDestinations(playerLocation.getId())) {
            locations.addOption(location.getName(), location.getId().toString());
        }

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder = messageCreateBuilder.addActionRow(locations.build());
        messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    /**
     * Handles the string select interaction event when a location is chosen.
     *
     * @param event The StringSelectInteractionEvent representing the interaction.
     */
    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String locationIdString = event.getSelectedOptions().get(0).getValue();
        Objects.requireNonNull(locationIdString); // required parameter; will never be null

        String playerDiscordId = event.getUser().getId();
        Location location = locationController.getLocation(new ObjectId(locationIdString));

        playerController.setPlayerCurrentLocation(playerDiscordId, location);

        event.reply(String.format("You have moved to %s", location.getName()))
                .setEphemeral(true)
                .queue();

        MainViewCommand mainViewCommand = new MainViewCommand();
        if (location.getLocationType().equals(LocationType.TOWN)) {
            mainViewCommand.townView(location, event.getChannel());
        } else {
            mainViewCommand.wildView(location, event.getChannel());
        }
    }
}
