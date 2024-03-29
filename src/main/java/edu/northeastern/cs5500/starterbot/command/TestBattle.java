package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.BattleController;
import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.controller.TestBattlePokemon;
import edu.northeastern.cs5500.starterbot.model.Battle;
import edu.northeastern.cs5500.starterbot.model.Campaign;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.types.ObjectId;

@Slf4j
public class TestBattle implements SlashCommandHandler {

    static final String NAME = "testbattle";

    @Inject TestBattlePokemon testBattlePokemon;
    @Inject PlayerController playerController;
    @Inject TestBattleView testBattleView;
    @Inject BattleController battleController;
    @Inject LocationController locationController;

    @Inject
    public TestBattle() {
        // Defined public and empty for Dagger injection
    }

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Test Battle");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /testbattle");

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        battleController.startNewCampaign(
                player, testBattlePokemon.getMyTeam(), testBattlePokemon.getEnemyTeam());
        Campaign campaign = battleController.getCampaignForObjectId(player.getCurCampaignId());

        battleController.startNewBattle(
                player,
                campaign.getMyTeam().getTeam().get(0),
                campaign.getEnemyTeam().getTeam().get(0));
        Battle battle = battleController.getBattleForObjectId(player.getCurBattleId());

        testBattleView.oneTurn(event.getChannel(), battle, campaign, location);
    }
}
