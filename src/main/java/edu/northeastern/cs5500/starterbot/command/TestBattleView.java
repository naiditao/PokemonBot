package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.BattleController;
import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.controller.PlayerController;
import edu.northeastern.cs5500.starterbot.model.Battle;
import edu.northeastern.cs5500.starterbot.model.Campaign;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import edu.northeastern.cs5500.starterbot.model.pokemon.Status;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.bson.types.ObjectId;

@Slf4j
public class TestBattleView implements ButtonHandler, StringSelectHandler {

    static final String NAME = "battle";

    @Inject PlayerController playerController;
    @Inject BattleController battleController;
    @Inject ItemCommand itemCommand;
    @Inject LocationController locationController;

    @Inject
    public TestBattleView() {
        // Defined public and empty for Dagger injection
    }

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    public MessageCreateBuilder primaryView(Battle battle) {
        EmbedBuilder embedBuilder1 = new EmbedBuilder();
        embedBuilder1.setTitle(String.format("Enemy: %s", battle.getEnemyPokemon().getName()));

        embedBuilder1.setDescription(
                String.format(
                        "HP: %d/%d",
                        battle.getEnemyPokemon().getCurHP(),
                        battle.getEnemyPokemon().getCurMaxHp()));

        embedBuilder1.setThumbnail(battle.getEnemyPokemon().getImageUrl());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Player: %s", battle.getMyPokemon().getName()));

        embedBuilder.setDescription(
                String.format(
                        "HP: %d/%d",
                        battle.getMyPokemon().getCurHP(), battle.getMyPokemon().getCurMaxHp()));

        embedBuilder.setThumbnail(battle.getMyPokemon().getImageUrl());
        log.info(battle.getMyPokemon().getImageUrl());

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder1.build());
        messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(getName() + ":skill", "Skill"),
                        Button.success(getName() + ":item", "Item"),
                        Button.secondary(getName() + ":switch", "Switch"),
                        Button.danger(getName() + ":escape", "Escape"));

        return messageCreateBuilder;
    }

    public void onInteraction(MessageChannel channel, Battle battle) {
        log.info("event: /battle");

        MessageCreateBuilder messageCreateBuilder = primaryView(battle);
        channel.sendMessage(messageCreateBuilder.build()).queue();
    }

    @SuppressWarnings(
            "null") // Data is internalized, no need to check null if the program is logically
    // correct.
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        Battle battle = battleController.getBattleForObjectId(player.getCurBattleId());
        Campaign campaign = battleController.getCampaignForObjectId(player.getCurCampaignId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();

        switch (event.getButton().getLabel()) {
            case "Skill":
                embedBuilder.setTitle("Choose Skills:");

                StringSelectMenu.Builder skills = StringSelectMenu.create(NAME);

                skills.setPlaceholder("Choose Skills:");

                for (Skill x : battle.getMyPokemon().getSkills()) {
                    skills.addOption(x.getSkillId(), "Skill:" + x.getSkillId());
                }

                messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
                messageCreateBuilder = messageCreateBuilder.addActionRow(skills.build());
                event.reply(messageCreateBuilder.build()).queue();
                break;

            case "Item":
                messageCreateBuilder = itemCommand.useItemCommand(player);
                event.reply(messageCreateBuilder.build()).queue();
                break;

            case "Switch":
                embedBuilder.setTitle("Choose which pokemon:");

                StringSelectMenu.Builder pokemons = StringSelectMenu.create(NAME);
                pokemons.setPlaceholder("Choose Pokemon:");

                for (Pokemon x : campaign.getMyTeam().getTeam()) {
                    if (!x.equals(battle.getMyPokemon())) {
                        pokemons.addOption(x.getName(), "Switch:" + x.getName());
                    }
                }

                messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
                messageCreateBuilder = messageCreateBuilder.addActionRow(pokemons.build());
                event.reply(messageCreateBuilder.build()).queue();
                break;

            case "Escape":
                battleController.endCompaign(campaign);
                event.reply("You have escaped!").queue();
                backToMainMenu(location, event.getChannel());
                break;

            default:
                break;
        }
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0).split(":")[1];
        final String type = event.getInteraction().getValues().get(0).split(":")[0];
        Objects.requireNonNull(response);
        Objects.requireNonNull(type);

        log.info(response);

        String playerDiscordId = event.getUser().getId();
        Player player = playerController.getPlayerForUserId(playerDiscordId);
        ObjectId id = player.getLocationId();
        Objects.requireNonNull(id);
        Location location = locationController.getLocation(id);

        Battle battle = battleController.getBattleForObjectId(player.getCurBattleId());
        Campaign campaign = battleController.getCampaignForObjectId(player.getCurCampaignId());
        String info;
        Boolean catched = false;

        switch (type) {
            case "Skill":
                info =
                        battleController.damageCalculator(
                                battle.getMyPokemon(), battle.getEnemyPokemon(), response);
                Objects.requireNonNull(info);
                event.reply(info).queue();
                break;

            case "Item":
                info = itemCommand.onStringSelectInteraction(response, battle, player);
                Objects.requireNonNull(info);

                if (info.compareTo("Catch Successful") == 0) {
                    catched = true;
                }
                event.reply(info).queue();
                break;

            case "Switch":
                info = battleController.switchPokemon(battle, campaign, response);
                Objects.requireNonNull(info);
                event.reply(info).queue();
                break;

            default:
                break;
        }

        if (Boolean.TRUE.equals(catched)) {
            player.addPokemon(battle.getEnemyPokemon());
            battleController.endBattle(battle, player);
            battleController.endCompaign(campaign);
            backToMainMenu(location, event.getChannel());
        } else if (battle.getEnemyPokemon().getCurHP() != 0) {
            oneTurn(event.getChannel(), battle, campaign, location);
        } else {
            info = battleController.endBattle(battle, player);
            Objects.requireNonNull(info);
            event.getChannel().sendMessage(info).queue();

            int nextPokemon = campaign.getEnemyDefeat() + 1;

            if (nextPokemon < campaign.getTotalEnemy()) {
                info = battleController.nextBattle(player, nextPokemon, campaign, battle);
                Objects.requireNonNull(info);
                event.getChannel().sendMessage(info).queue();
                oneTurn(
                        event.getChannel(),
                        battleController.getBattleForObjectId(player.getCurBattleId()),
                        campaign,
                        location);
            } else {
                event.getChannel().sendMessage("You Won!!").queue();
                battleController.endCompaign(campaign);
                backToMainMenu(location, event.getChannel());
            }
        }
    }

    public void backToMainMenu(Location location, MessageChannel channel) {
        MainViewCommand mainViewCommand = new MainViewCommand();
        if (location.getLocationType().equals(LocationType.TOWN)) {
            mainViewCommand.townView(location, channel);
        } else {
            mainViewCommand.wildView(location, channel);
        }
    }

    public void action(
            Pokemon pokemon,
            Pokemon targetPokemon,
            MessageChannel channel,
            Battle battle,
            Campaign campaign,
            Location location) {
        for (Status status : pokemon.getStatus()) {
            String info = status.getEffect().preActionEffect(pokemon, targetPokemon);
            if (Objects.nonNull(info)) {
                channel.sendMessage(info).queue();
            }
        }
        String info = battleController.checkSkipTurn(targetPokemon);
        if (Objects.nonNull(info)) {
            channel.sendMessage(info).queue();
        }
        if (pokemon == battle.getMyPokemon()) {
            onInteraction(channel, battle);
        } else {
            info = battleController.aiUseSkill(pokemon, targetPokemon);
            Objects.requireNonNull(info);
            channel.sendMessage(info).queue();
            if (battle.getMyPokemon().getCurHP() == 0) {
                boolean allTeamsFail = true;
                info = String.format("%s has lost", battle.getMyPokemon().getName());
                for (Pokemon pokemonx : campaign.getMyTeam().getTeam()) {
                    if (pokemonx.getCurHP() != 0) {
                        battle.setMyPokemon(pokemonx);
                        battle.determineStartPokemon();
                        battle.getPokemonEngaged().add(pokemonx);
                        allTeamsFail = false;
                        info = info + String.format("%n%s has join the battle", pokemonx.getName());
                        channel.sendMessage(info).queue();
                        oneTurn(channel, battle, campaign, location);
                    }
                }
                if (allTeamsFail) {
                    battleController.endCompaign(campaign);
                    channel.sendMessage("You lost!").queue();
                    backToMainMenu(location, channel);
                }
            }
        }
        for (Status status : pokemon.getStatus()) {
            status.getEffect().effectFinish(status, pokemon);
        }
    }

    public void oneTurn(
            MessageChannel channel, Battle battle, Campaign campaign, Location location) {
        action(
                battle.getStartPokemon(),
                battle.getEndPokemon(),
                channel,
                battle,
                campaign,
                location);
        action(
                battle.getEndPokemon(),
                battle.getStartPokemon(),
                channel,
                battle,
                campaign,
                location);
    }
}
