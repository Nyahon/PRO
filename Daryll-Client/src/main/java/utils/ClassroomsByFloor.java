package utils;

import java.util.*;

/**
 * This class contains useful maps in order to easily get the different floor in each building and the different
 * classroom in each floor.
 *
 * @author Loïc Frueh
 */
public class ClassroomsByFloor {
    /**
     * The classroom list of the floor A
     */
    private static final List<String> A_CLASSROOMS =
            Arrays.asList("A01", "A05a", "A05b", "A07", "A09");
    /**
     * The classroom list of the floor B
     */
    private static final List<String> B_CLASSROOMS =
            Arrays.asList("B01a", "B03", "B05", "B06", "B08", "B08b", "B10", "B23", "B26", "B30", "B57", "B59", "B61", "B66b");
    /**
     * The classroom list of the floor C
     */
    private static final List<String> C_CLASSROOMS =
            Arrays.asList("C05", "C06b", "C23", "C33", "C37", "C38", "C41", "C54b", "C57", "C59");
    /**
     * The classroom list of the floor D
     */
    private static List<String> D_CLASSROOMS =
            Arrays.asList("D01", "D05", "D07", "D42", "D51a", "D55", "D57", "D58", "D61");
    /**
     * The classroom list of the floor F
     */
    private static List<String> F_CLASSROOMS =
            Arrays.asList("F01");
    /**
     * The classroom list of the floor G
     */
    private static List<String> G_CLASSROOMS =
            Arrays.asList("G01", "G02", "G03", "G04", "G05", "G06a", "G06b", "G07");
    /**
     * The classroom list of the floor H
     */
    private static List<String> H_CLASSROOMS =
            Arrays.asList("H01", "H02", "H03", "H04", "H05", "H06c", "H07");
    /**
     * The classroom list of the floor J
     */
    private static List<String> J_CLASSROOMS =
            Arrays.asList("J01", "J02", "J03", "J04", "J05", "J06", "J07");
    /**
     * The classroom list of the floor K
     */
    private static List<String> K_CLASSROOMS =
            Arrays.asList("K01", "K02", "K03", "K04");
    /**
     * The classroom list of the floor R
     */
    private static List<String> R_CLASSROOMS =
            Arrays.asList("R06", "R09", "R102");
    /**
     * The classroom list of the floor S
     */
    private static List<String> S_CLASSROOMS =
            Arrays.asList("S02", "S07", "S102a", "S102b", "S106", "S107", "S12", "S125a", "S129", "S131", "S133", "S135",
                    "S137", "S141", "S143", "S147", "S151", "S153", "S155", "S157");
    /**
     * The classroom list of the floor T
     */
    private static List<String> T_CLASSROOMS =
            Arrays.asList("T102", "T104", "T105", "T106", "T107", "T12", "T129", "T136a", "T139", "T141", "T143",
                    "T147", "T151", "T153", "T155", "T157", "T158");
    /**
     * The classroom list of the floor U
     */
    private static List<String> U_CLASSROOMS =
            Arrays.asList("U32", "U44");

    /**
     * The floor list of the Cheseaux building
     */
    public static final List<String> FLOORS_CHESEAUX =
            Arrays.asList("A", "B", "C", "D", "F", "G", "H", "J", "K");
    /**
     * The floor list of the St-Roch building
     */
    public static final List<String> FLOORS_ST_ROCH =
            Arrays.asList("R", "S", "T", "U");

    /**
     * The map that link a floor with its classrooms
     */
    public static final Map< String, List<String> > FLOORS_MAP;
    /**
     * The map that link a building with its floors
     */
    public static final Map<Integer, List<String> > BUILDING_MAP;
    static {
        Map<String, List<String> > floorMap = new HashMap<>();
        floorMap.put("A", A_CLASSROOMS);
        floorMap.put("B", B_CLASSROOMS);
        floorMap.put("C", C_CLASSROOMS);
        floorMap.put("D", D_CLASSROOMS);
        floorMap.put("F", F_CLASSROOMS);
        floorMap.put("G", G_CLASSROOMS);
        floorMap.put("H", H_CLASSROOMS);
        floorMap.put("J", J_CLASSROOMS);
        floorMap.put("K", K_CLASSROOMS);
        floorMap.put("R", R_CLASSROOMS);
        floorMap.put("S", S_CLASSROOMS);
        floorMap.put("T", T_CLASSROOMS);
        floorMap.put("U", U_CLASSROOMS);
        FLOORS_MAP = Collections.unmodifiableMap(floorMap);

        Map<Integer, List<String> > buildingMap = new HashMap<>();
        buildingMap.put(0, FLOORS_CHESEAUX);
        buildingMap.put(1, FLOORS_ST_ROCH);
        BUILDING_MAP = buildingMap;
    }

}
