package utils;

import java.util.HashMap;
import java.util.Map;

public class FloorFreePeriodMap {

private Map<String, Integer> freeMap = new HashMap<>();

public FloorFreePeriodMap(String floor) {
    for (String classroom : ClassroomsByFloor.FLOORS_MAP.get(floor)) {
        freeMap.put(classroom, 3);
    }
}

public void insert(String classroom, Integer value) {
    freeMap.replace(classroom, value);
}

public Map<String, Integer> getFreeMap() {
    return new HashMap<>(freeMap);
}


}
