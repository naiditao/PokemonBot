package edu.northeastern.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.controller.LocationController;
import edu.northeastern.cs5500.starterbot.model.area.Location;
import edu.northeastern.cs5500.starterbot.model.area.LocationType;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationControllerTest {

    private LocationController locationController;

    @BeforeEach
    void setUp() {
        GenericRepository<Location> repository = new InMemoryRepository<Location>();

        locationController = new LocationController(repository);
    }

    @SuppressWarnings("null")
    @Test
    void getLocationTest() {
        Location test = locationController.getLocation("Pallet_Town");
        assertEquals(test, locationController.getLocation(test.getId()));
        assertEquals("Pallet_Town", test.getName());
        assertEquals(LocationType.TOWN, test.getLocationType());
        assertTrue(test.getWildPokemon().isEmpty());

        Location test2 = locationController.getSpawningLocation();
        assertEquals(test2, locationController.getLocation("Fuchsia_City"));
    }

    @Test
    void getDestinationTest() {
        Location test = locationController.getLocation("Pallet_Town");
        Set<Location> test2 = locationController.getDestinations(test.getId());
        assertTrue(test2.contains(locationController.getLocation("Route_1")));
        assertTrue(test2.contains(locationController.getLocation("Route_21")));
    }

    @Test
    void RandomEncounterTest() {
        Map<String, Integer> encounterTable = new HashMap<>();
        encounterTable.put("Pokemon1", 5);
        encounterTable.put("Pokemon2", 3);
        encounterTable.put("Pokemon3", 2);

        String result = LocationController.randomEncounter(encounterTable);

        assertTrue(encounterTable.containsKey(result));

        Map<String, Integer> emptyTable = new HashMap<>();

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> LocationController.randomEncounter(emptyTable));

        assertEquals("encounterTable must not be empty", exception.getMessage());

        Map<String, Integer> zeroRatesTable = new HashMap<>();
        zeroRatesTable.put("Pokemon1", 0);
        zeroRatesTable.put("Pokemon2", 0);
        zeroRatesTable.put("Pokemon3", 0);

        IllegalStateException exception2 =
                assertThrows(
                        IllegalStateException.class,
                        () -> LocationController.randomEncounter(zeroRatesTable));

        assertEquals("encounterTable must not be empty", exception2.getMessage());
    }
}
