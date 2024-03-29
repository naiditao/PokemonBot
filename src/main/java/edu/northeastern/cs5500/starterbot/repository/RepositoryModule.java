package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.Battle;
import edu.northeastern.cs5500.starterbot.model.Campaign;
import edu.northeastern.cs5500.starterbot.model.Player;
import edu.northeastern.cs5500.starterbot.model.UserPreference;
import edu.northeastern.cs5500.starterbot.model.area.Location;

@Module
public class RepositoryModule {

    @Provides
    public GenericRepository<Location> provideLocationRepository(
            InMemoryRepository<Location> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<UserPreference> provideUserPreferencesRepository(
            InMemoryRepository<UserPreference> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<Battle> provideBattleRepository(
            InMemoryRepository<Battle> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<Campaign> provideCampaignRepository(
            InMemoryRepository<Campaign> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<Player> providePlayerRepository(
            InMemoryRepository<Player> repository) {
        return repository;
    }
}
