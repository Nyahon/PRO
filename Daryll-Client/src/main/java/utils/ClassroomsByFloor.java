package utils;

import java.util.*;

public class ClassroomsByFloor {
    private static final List<String> A_CLASSROOMS =
            Arrays.asList("A01", "A05a", "A05b", "A07", "A09");
    private static final List<String> B_CLASSROOMS =
            Arrays.asList("B01a", "B03", "B05", "B06", "B08", "B08b", "B10", "B23", "B26", "B30", "B57", "B59", "B61", "B66b");
    private static final List<String> C_CLASSROOMS =
            Arrays.asList("C05", "C06b", "C23", "C33", "C37", "C38", "C41", "C54b", "C57", "C59");
    private static List<String> D_CLASSROOMS =
            Arrays.asList("D01", "D05", "D07", "D42", "D51a", "D55", "D57", "D58", "D61");
    private static List<String> F_CLASSROOMS =
            Arrays.asList("F01");
    private static List<String> G_CLASSROOMS =
            Arrays.asList("G01", "G02", "G03", "G04", "G05", "G06a", "G06b");
    private static List<String> H_CLASSROOMS =
            Arrays.asList("H01", "H02", "H03", "H04", "H05", "H06c");
    private static List<String> J_CLASSROOMS =
            Arrays.asList("J01", "J02", "J03", "J04", "J05", "J06");
    private static List<String> K_CLASSROOMS =
            Arrays.asList("K01", "K02", "K03", "K04");

    public static final Map< String, List<String> > FloorsMap;
    static {
        Map<String, List<String> > map = new HashMap<>();
        map.put("A", A_CLASSROOMS);
        map.put("B", B_CLASSROOMS);
        map.put("C", C_CLASSROOMS);
        map.put("D", D_CLASSROOMS);
        map.put("F", F_CLASSROOMS);
        map.put("G", G_CLASSROOMS);
        map.put("H", H_CLASSROOMS);
        map.put("J", J_CLASSROOMS);
        map.put("K", K_CLASSROOMS);
        FloorsMap = Collections.unmodifiableMap(map);
    }
}
