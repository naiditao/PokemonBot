package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Battle;
import edu.northeastern.cs5500.starterbot.model.Campaign;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.Teams;
import edu.northeastern.cs5500.starterbot.model.pokemon.Effectiveness;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.model.pokemon.Skill;
import edu.northeastern.cs5500.starterbot.model.pokemon.Status;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Objects;
import java.util.Random;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Slf4j
public class BattleController {
    @Inject PlayerController playerController;

    GenericRepository<Battle> battleRepository;
    GenericRepository<Campaign> campaignRepository;

    static Random rand = new Random();

    @Inject
    BattleController(
            GenericRepository<Battle> battleRepository,
            GenericRepository<Campaign> campaignRepository) {
        this.battleRepository = battleRepository;
        this.campaignRepository = campaignRepository;
    }

    @NonNull
    public String nextBattle(Player player, int nextPokemon, Campaign campaign, Battle battle) {
        campaign.setEnemyDefeat(nextPokemon);

        startNewBattle(
                player, battle.getMyPokemon(), campaign.getEnemyTeam().getTeam().get(nextPokemon));
        campaignRepository.update(campaign);

        return String.format("%s has appeared", battle.getEnemyPokemon().getName());
    }

    public void dealDamage(Pokemon targetPokemon, int damage) {
        if (damage > targetPokemon.getCurHP()) {
            targetPokemon.setCurHP(0);
        } else {
            targetPokemon.setCurHP(targetPokemon.getCurHP() - damage);
        }
    }

    public void startNewCampaign(
            @NonNull Player player, @NonNull Teams myTeam, @NonNull Teams enemyTeam) {
        Campaign campaign = new Campaign(myTeam, enemyTeam);
        ObjectId id = new ObjectId();
        campaign.setId(id);
        campaign.setEnemyDefeat(0);
        campaign.setTotalEnemy(enemyTeam.getNum());

        player.setCurCampaignId(campaign.getId());

        campaignRepository.add(campaign);
        playerController.getPlayerRepository().update(player);

        startNewBattle(
                player,
                campaign.getMyTeam().getTeam().get(0),
                campaign.getEnemyTeam().getTeam().get(0));
    }

    public void startNewBattle(
            @NonNull Player player, @NonNull Pokemon myPokemon, @NonNull Pokemon enemyPokemon) {
        Battle battle = new Battle(myPokemon, enemyPokemon);
        ObjectId id = new ObjectId();
        battle.setId(id);
        battle.getPokemonEngaged().add(myPokemon);

        player.setCurBattleId(id);

        battleRepository.add(battle);
        playerController.getPlayerRepository().update(player);
    }

    public String damageCalculator(Pokemon pokemon, Pokemon targetPokemon, String response) {
        for (Skill skill : pokemon.getSkills()) {
            if (skill.getSkillId().compareTo(response) == 0) {

                Effectiveness effect =
                        skill.getType()
                                .determineEffectiveness(skill.getType(), targetPokemon.getTypes());

                int damage =
                        skill.getSkillType()
                                .damage(
                                        targetPokemon,
                                        pokemon,
                                        effect.getEffect(),
                                        rand.nextInt(38) + 217,
                                        skill);
                dealDamage(targetPokemon, damage);
                String info = inflictStatus(skill, targetPokemon);

                return (effect.getText()
                        + String.format(
                                "%n%s have dealt %d damage to %s",
                                pokemon.getName(), damage, targetPokemon.getName())
                        + info);
            }
        }
        return null;
    }

    public String inflictStatus(Skill skill, Pokemon targetPokemon) {
        boolean hasStatus = false;
        log.info("inflict Status");
        Status newStatus = new Status(skill.getEffect());
        int turnRemain = newStatus.getTurnRemain();
        if (rand.nextInt(100) < skill.getChanceForStatus()) {
            for (Status status : targetPokemon.getStatus()) {
                if (status.getEffect().equals(skill.getEffect())) {
                    hasStatus = true;
                    status.setTurnRemain(status.getTurnRemain() + newStatus.getTurnRemain());
                    turnRemain = status.getTurnRemain();
                }
            }
            if (!hasStatus) {
                targetPokemon.getStatus().add(newStatus);
            }
            return String.format(
                    "%n%s has %s (%d)",
                    targetPokemon.getName(), skill.getEffect().toString(), turnRemain);
        }
        return "";
    }

    public String aiUseSkill(Pokemon pokemon, Pokemon targetPokemon) {
        int item = rand.nextInt(pokemon.getSkills().size());
        int i = 0;
        for (Skill skill : pokemon.getSkills()) {
            if (i == item) {
                return String.format("%s has used %s%n", pokemon.getName(), skill.getSkillId())
                        + damageCalculator(pokemon, targetPokemon, skill.getSkillId());
            }
            i++;
        }
        return null;
    }

    public boolean checkPokemonHasStatus(Pokemon targetPokemon, StatusEffect statusEffect) {
        for (Status status : targetPokemon.getStatus()) {
            if (status.getEffect().equals(statusEffect)) {
                return true;
            }
        }
        return false;
    }

    public String switchPokemon(Battle battle, Campaign campaign, String pokemonName) {
        for (Pokemon x : campaign.getMyTeam().getTeam()) {
            if (x.getName().compareTo(pokemonName) == 0) {
                battle.setMyPokemon(x);
                battle.getPokemonEngaged().add(x);
                battle.determineStartPokemon();
                battleRepository.update(battle);
                return String.format("%s has join the battle", battle.getMyPokemon().getName());
            }
        }
        return null;
    }

    public String endBattle(Battle battle, Player player) {
        int expGain = 100 * battle.getEnemyPokemon().getCurLV() / battle.getPokemonEngaged().size();
        int coinGain = 100 * battle.getEnemyPokemon().getCurLV();
        player.setPokeCoin(player.getPokeCoin() + coinGain);

        for (Pokemon pokemon : battle.getPokemonEngaged()) {
            pokemon.addEXP(expGain);
        }

        return String.format(
                "%s has been defeated%nYou have gain %d coins",
                battle.getEnemyPokemon().getName(), coinGain);
    }

    public void endCompaign(Campaign campaign) {
        for (Pokemon pokemon : campaign.getMyTeam().getTeam()) {
            pokemon.getStatus().clear();
        }
    }

    public String checkSkipTurn(Pokemon pokemon) {
        for (Status status : pokemon.getStatus()) {
            String info = status.getEffect().skipTurn(pokemon, rand.nextInt(100));
            if (Objects.nonNull(info)) {
                log.info(info);
                return info;
            }
        }
        return null;
    }

    public Battle getBattleForObjectId(@NonNull ObjectId id) {
        return battleRepository.get(id);
    }

    public Campaign getCampaignForObjectId(@NonNull ObjectId id) {
        return campaignRepository.get(id);
    }

    public void updataBattle(@NonNull Battle battle) {
        battleRepository.update(battle);
    }
}
