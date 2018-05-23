package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A map that link a classroom and a number of free period remaining until the next classroom occupation
 *
 * @author Loïc Frueh
 */
public class FloorFreePeriodMap {

    /**
     * The map used to link the classroom with the number of remaining free period.
     * This map will later be used by the GUI to display rightful colors on the plans.
     */
    private Map<String, Integer> freeMap = new HashMap<>();

    /**
     * Constructs a FloorFreePeriodMap, it initialized all the map with the default DARK_GREEN ordinal value.
     * @param floor the floor whose classrooms is used as key for the map
     */
    public FloorFreePeriodMap(String floor) {
        for (String classroom : ClassroomsByFloor.FLOORS_MAP.get(floor)) {
            freeMap.put(classroom, DisplayConstants.COLORS_ROOMS.DARK_GREEN.ordinal());
        }
    }
    /**
     * Insert a new value at the specified key
     * @param classroom the key where insert the element (the classroom)
     * @param value the element to insert in the map (number of free periods remaining)
     */
    public void insert(String classroom, Integer value) {
        freeMap.replace(classroom, value);
    }

    /**
     * Insert a new value at the specified key
     * @return the map that will be used by the GUI for the display of the right colors
     */
    public Map<String, Integer> getFreeMap() {
        return new HashMap<>(freeMap);
    }


}
