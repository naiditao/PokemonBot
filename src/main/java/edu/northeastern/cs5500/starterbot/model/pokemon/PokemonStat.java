package edu.northeastern.cs5500.starterbot.model.pokemon;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code PokemonStat} class is a utility class providing methods for retrieving Pokemon
 * statistics, including Pokedex information, base and final statistics, URLs for images, challenge
 * levels, and evolution levels. It reads data from a file named "pokemonInfo.txt" and constructs
 * various maps for efficient data retrieval.
 *
 * <p>This class follows the singleton pattern to ensure maps are constructed only once.
 *
 * <p>The file format of "pokemonInfo.txt" is expected to be as follows for each line: {@code name
 * imageUrl pokedex baseHp baseAttack baseDefense baseSpecialAttack baseSpecialDefense baseSpeed
 * finalHp finalAttack finalDefense finalSpecialAttack finalSpecialDefense finalSpeed challengeLevel
 * evolve target}
 */
public class PokemonStat {
    private static final Logger logger =
            LoggerFactory.getLogger(PokemonStat.class); // The logger for PokemonStat class.
    private static final String FILE_NOT_FOUND_ERROR = "File not found!";
    private static final String FILE_READING_ERROR = "Error reading file: {}";
    private static final String UNEXPECTED_ERROR = "Unexpected error: {}";
    private static final int SIX_BASE_STAT = 6;
    private static final int BASE_STATS_START_INDEX = 3;
    private static final int FINAL_STATS_START_INDEX = 9;
    private static final int CHALLENGE_LEVEL_INDEX = 15;
    private static final int EVOLVE_TARGET = 16;
    private static final int MAX_LEVEL = 100;
    private static final int POKEDEX_INDEX = 2;
    private static final int URL_INDEX = 1;
    private static final int SPECIES_NAME_INDEX = 0;

    private static Map<String, String>
            nameToPokedex; // Mapping of Pokemon names to Pokedex entries.
    private static Map<String, int[]> baseStat; // Mapping of Pokedex entries to base statistics.
    private static Map<String, int[]> finalStat; // Mapping of Pokedex entries to final statistics.
    private static Map<String, String> images; // Mapping of Pokedex entries to image URLs.
    private static Map<String, Integer>
            challengeLevel; // Mapping of Pokedex entries to challenge levels.
    private static Map<String, String> evolveMap; // Mapping of Pokedex entries to evolve targets.

    /** Private constructor to prevent instantiation of the PokemonStat class.. */
    private PokemonStat() {}

    /**
     * Retrieves the Pokedex entry for a given Pokemon name.
     *
     * @param name The name of the Pokemon.
     * @return The Pokedex entry for the given Pokemon name.
     */
    public static String getPokedex(String name) {
        if (PokemonStat.nameToPokedex == null) {
            PokemonStat.constructMaps();
        }

        return PokemonStat.nameToPokedex.get(name);
    }

    /**
     * Retrieves the Pokemon name entry for a given Pokedex.
     *
     * @param pokedex The Pokedex of the Pokemon.
     * @return The Pokemon name entry for the given Pokedex.
     */
    public static String getPokemonName(String pokedex) {
        if (PokemonStat.nameToPokedex == null) {
            PokemonStat.constructMaps();
        }
        for (Entry<String, String> entry : nameToPokedex.entrySet()) {
            if (entry.getValue().equals(pokedex)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException();
    }

    /**
     * Retrieves the base statistics for a given Pokedex entry.
     *
     * @param pokedex The Pokedex entry of the Pokemon.
     * @return The base statistics for the given Pokedex entry.
     */
    public static int[] generateBase(String pokedex) {
        if (PokemonStat.baseStat == null) {
            PokemonStat.constructMaps();
        }

        return PokemonStat.baseStat.get(pokedex);
    }

    /**
     * Calculates and retrieves the final statistics for a Pokemon at a given level.
     *
     * @param pokedex The Pokedex entry of the Pokemon.
     * @param level The level of the Pokemon.
     * @return The final statistics for the Pokemon at the given level.
     */
    public static int[] levelUp(String pokedex, int level) {
        if (PokemonStat.finalStat == null) {
            PokemonStat.constructMaps();
        }

        int[] info = new int[SIX_BASE_STAT];
        int[] pokeBase = PokemonStat.baseStat.get(pokedex);
        int[] pokeMax = PokemonStat.finalStat.get(pokedex);
        for (int i = 0; i < info.length; i++) {
            info[i] =
                    ((int) Math.round((pokeMax[i] - pokeBase[i]) * ((double) level / MAX_LEVEL)))
                            + pokeBase[i];
        }
        return info;
    }

    /**
     * Retrieves the URL(from Pokemon Database website) for the image of a Pokemon with the given
     * Pokedex entry.
     *
     * @param pokedex The Pokedex entry of the Pokemon.
     * @return The URL for the image of the Pokemon.
     */
    public static String getUrl(String pokedex) {
        if (PokemonStat.images == null) {
            PokemonStat.constructMaps();
        }

        return PokemonStat.images.get(pokedex);
    }

    /**
     * Retrieves the challenge level for a Pokemon with the given Pokedex entry.
     *
     * @param pokedex The Pokedex entry of the Pokemon.
     * @return The challenge level of the Pokemon.
     */
    public static int getChallengeLevel(String pokedex) {
        if (PokemonStat.images == null) {
            PokemonStat.constructMaps();
        }

        return PokemonStat.challengeLevel.get(pokedex);
    }

    /**
     * Retrieves the evolution level for a Pokemon with the given Pokedex entry.
     *
     * @param pokedex The Pokedex entry of the Pokemon.
     * @return The evolution level of the Pokemon.
     */
    public static int getEvolveLevel(String pokedex) {
        if (PokemonStat.evolveMap == null) {
            PokemonStat.constructMaps();
        }
        String evolveTarget = PokemonStat.evolveMap.get(pokedex);
        if (evolveTarget.equals("NA")) {
            return MAX_LEVEL + 1;
        }
        return PokemonStat.getChallengeLevel(evolveTarget);
    }

    /**
     * Constructs various maps for Pokemon statistics using data read from "pokemonInfo.txt" file.
     */
    private static void constructMaps() {
        PokemonStat.images = new HashMap<>();
        PokemonStat.nameToPokedex = new HashMap<>();
        PokemonStat.baseStat = new HashMap<>();
        PokemonStat.finalStat = new HashMap<>();
        PokemonStat.challengeLevel = new HashMap<>();
        PokemonStat.evolveMap = new HashMap<>();

        InputStream inputStream =
                LocationController.class.getClassLoader().getResourceAsStream("pokemonInfo.txt");
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                PokemonStat.images.put(line[POKEDEX_INDEX], line[URL_INDEX]);
                PokemonStat.nameToPokedex.put(line[SPECIES_NAME_INDEX], line[POKEDEX_INDEX]);
                int[] base = new int[SIX_BASE_STAT];
                for (int i = 0; i < SIX_BASE_STAT; i++) {
                    base[i] = Integer.parseInt(line[i + BASE_STATS_START_INDEX]);
                }
                PokemonStat.baseStat.put(line[POKEDEX_INDEX], base);

                int[] finale = new int[SIX_BASE_STAT];
                for (int i = 0; i < SIX_BASE_STAT; i++) {
                    finale[i] = Integer.parseInt(line[i + FINAL_STATS_START_INDEX]);
                }
                PokemonStat.finalStat.put(line[POKEDEX_INDEX], finale);
                PokemonStat.challengeLevel.put(
                        line[POKEDEX_INDEX], Integer.parseInt(line[CHALLENGE_LEVEL_INDEX]));
                PokemonStat.evolveMap.put(line[POKEDEX_INDEX], line[EVOLVE_TARGET]);
            }
        } catch (NullPointerException e) {
            logger.error(FILE_NOT_FOUND_ERROR, e);
        } catch (NumberFormatException | NoSuchElementException e) {
            logger.error(FILE_READING_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            logger.error(UNEXPECTED_ERROR, e.getMessage(), e);
        }
    }
}
