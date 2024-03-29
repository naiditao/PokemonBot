package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

/**
 * The LocationController class manages locations in the game world, including towns, wild areas. It
 * provides methods for adding locations, loading locations from resources, constructing a world
 * map, generating wild Pokemon encounters, and retrieving information about locations.
 * Additionally, it handles random encounters based on encounter rates.
 */
@Singleton
public class LocationController {
    static final String STARTING_LOCATION_NAME = "Fuchsia_City";
    private static final int POKEDEX_LENGTH = 4;

    /** The repository for managing locations. */
    GenericRepository<Location> locationRepository;

    /** A map to store location names and their corresponding ID. */
    Map<String, ObjectId> locationIdByName = new HashMap<>();

    /** The world map, represented as a map of location IDs to sets of neighboring location ID. */
    private Map<ObjectId, Set<ObjectId>> worldMap = new HashMap<>();

    /**
     * Constructs a new LocationController with the given location repository.
     *
     * @param locationRepository The repository for managing locations.
     */
    @Inject
    public LocationController(GenericRepository<Location> locationRepository) {
        this.locationRepository = locationRepository;
        loadLocationsFromResource("kantoCities.txt", LocationType.TOWN);
        loadLocationsFromResource("kantoWilds.txt", LocationType.WILD);
        if (!locationIdByName.containsKey(STARTING_LOCATION_NAME)) {
            throw new IllegalStateException(
                    String.format(
                            "Starting area is '%s' but it was not found in loaded resources!",
                            STARTING_LOCATION_NAME));
        }

        constructWorldMap();
        generateWildPokemon();
    }

    /**
     * Adds a new location to the system with the specified name and type.
     *
     * @param locationName The name of the new location.
     * @param locationType The type of the new location.
     */
    private void addLocation(String locationName, LocationType locationType) {
        Location location = new Location(locationName, locationType);
        location = locationRepository.add(location);
        locationIdByName.put(location.getName(), location.getId());
    }

    /**
     * Loads location names from a specified resource file and adds them to the map.
     *
     * @param resourcePath The path to the resource file containing location names.
     * @param locationType The type of locations to be loaded.
     */
    private void loadLocationsFromResource(String resourcePath, LocationType locationType) {
        InputStream inputStream =
                LocationController.class.getClassLoader().getResourceAsStream(resourcePath);

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                addLocation(scanner.nextLine(), locationType);
            }
        }
    }

    /**
     * Constructs the world map by reading location from resource file, and store them in a adjcency
     * list.
     */
    private void constructWorldMap() {
        InputStream inputStream =
                LocationController.class.getClassLoader().getResourceAsStream("kantoMap.txt");

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] locations = line.split(" ");

                ObjectId current = locationIdByName.get(locations[0]);

                Set<ObjectId> neighbors = new HashSet<>();
                for (int i = 1; i < locations.length; i++) {
                    neighbors.add(locationIdByName.get(locations[i]));
                }
                this.worldMap.put(current, neighbors);
            }
        }
    }

    /** Generates wild Pokemon encounters for each location based on the resource file. */
    private void generateWildPokemon() {
        InputStream inputStream =
                LocationController.class
                        .getClassLoader()
                        .getResourceAsStream("pokemonLocation.txt");

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try (Scanner lineScanner = new Scanner(line)) {

                    String locationName = lineScanner.next();
                    ObjectId locationId = locationIdByName.get(locationName);
                    if (locationId == null) {
                        throw new IllegalStateException(
                                String.format(
                                        "Location %s was in pokemonLocation.txt but did not exist elsewhere",
                                        locationName));
                    }
                    Location location = locationRepository.get(locationId);
                    Objects.requireNonNull(location);

                    while (lineScanner.hasNext()) {
                        lineScanner.next(); // ignore the pokemon name
                        int pokemonId = lineScanner.nextInt();
                        int appearanceRate = lineScanner.nextInt();
                        String pokedex = String.valueOf(pokemonId);
                        pokedex = "0".repeat(POKEDEX_LENGTH - pokedex.length()) + pokedex;
                        location.getWildPokemon().put(pokedex, appearanceRate);
                    }

                    locationRepository.update(location);
                }
            }
        }
    }

    /**
     * Gets a set of adjacent destinations for a given location based on its ObjectId.
     *
     * @param locationId The ObjectId of the location for which to retrieve adjacent locations.
     * @return A set of adjacent Location objects.
     * @throws IllegalStateException If the requested location has no adjacent locations.
     */
    @SuppressWarnings("null") // it is hard coded in resource file.
    public Set<Location> getDestinations(ObjectId locationId) {
        Set<ObjectId> adjacentLocationIds = worldMap.get(locationId);
        if (adjacentLocationIds == null || adjacentLocationIds.isEmpty()) {
            throw new IllegalStateException("Requested location has no adjacent locations!");
        }

        return adjacentLocationIds.stream()
                .map(i -> locationRepository.get(i))
                .collect(Collectors.toSet());
    }

    public Location getLocation(String name) {
        ObjectId locationId = locationIdByName.get(name);
        if (locationId == null) return null;
        return locationRepository.get(locationId);
    }

    /**
     * Gets a location from the repository based on the provided ObjectId.
     *
     * @param id The ObjectId of the location to retrieve.
     * @return The Location object corresponding to the provided ObjectId.
     */
    public Location getLocation(@Nonnull ObjectId id) {
        return locationRepository.get(id);
    }

    /**
     * Gets the spawning location (starting location) based on the starting location name.
     *
     * @return The spawning location.
     * @throws NullPointerException If the starting location ID is null in the locationIdByName map.
     */
    @SuppressWarnings("null") // verified in the constructor
    public Location getSpawningLocation() {
        return locationRepository.get(locationIdByName.get(STARTING_LOCATION_NAME));
    }

    static Random randomEncounterSeed = new Random();
    /**
     * Given an encounter table that maps (id) to (encounter rate), return a random id weighted by
     * encounter rate. Higher encounter rate means a higher chance of that encounter proportional to
     * all other possible encounters; an encounter rate of zero means the encounter will be
     * disabled.
     *
     * @param encounterTable
     * @return
     */
    public static String randomEncounter(@Nonnull Map<String, Integer> encounterTable) {
        int totalPool =
                encounterTable.values().stream()
                        .mapToInt(Integer::intValue)
                        .filter(i -> i > 0)
                        .sum();
        if (totalPool == 0) {
            throw new IllegalStateException("encounterTable must not be empty");
        }
        int encounterIndex = randomEncounterSeed.nextInt(totalPool);
        for (Entry<String, Integer> entry : encounterTable.entrySet()) {
            int value = entry.getValue();
            if (value <= 0) continue;
            encounterIndex -= value;
            if (encounterIndex <= 0) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException();
    }
}
