package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.Teams;
import edu.northeastern.cs5500.starterbot.model.area.*;
import edu.northeastern.cs5500.starterbot.model.pokemon.Pokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import lombok.Data;

@Data
public class PlayerController {
    GenericRepository<Player> playerRepository;
    @Inject LocationController locationController;
    @Inject PokemonTypeController pokemonTypeController;
    @Inject SkillController skillController;
    @Inject ItemController itemController;

    @Inject
    public PlayerController(GenericRepository<Player> playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void setPlayerCurrentLocation(String discordUserId, Location location) {
        Player player = getPlayerForUserId(discordUserId);
        player.setLocationId(location.getId());
        playerRepository.update(player);
    }

    public Player getPlayerForUserId(String discordUserId) {
        Collection<Player> userPreferences = playerRepository.getAll();
        for (Player currentPlayer : userPreferences) {
            if (currentPlayer.getDiscordUserId().equals(discordUserId)) {
                return currentPlayer;
            }
        }
        return newPlayer(discordUserId);
    }

    public Player newPlayer(String discordUserId) {
        Player player = new Player();
        player.setDiscordUserId(discordUserId);
        player.setLocationId(locationController.getSpawningLocation().getId());
        player.setTeams(new Teams(new ArrayList<>()));
        for (int i = 0; i < 5; i++) {
            itemController.addItem(player, "PokeBall small");
        }
        playerRepository.add(player);
        return player;
    }

    public void addPokemonToPlayer(String discordUserId, Pokemon pokemon) {
        Player curPlayer = getPlayerForUserId(discordUserId);
        curPlayer.addPokemon(pokemon);
    }

    public void removePokemonFromPlayer(String discordUserId, Pokemon pokemon) {
        Player curPlayer = getPlayerForUserId(discordUserId);
        curPlayer.removePokemon(pokemon);
    }

    public void retrievePokemonFromPlayer(String discordUserId, Pokemon pokemon) {
        Player curPlayer = getPlayerForUserId(discordUserId);
        removePokemonFromPlayer(discordUserId, pokemon);
        curPlayer.addPokemon(pokemon);
    }
}
