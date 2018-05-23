package utils;

/**
 * This class contains useful maps in order to easily get the different floor in each building and the different
 * classroom in each floor.
 *
 * @author Loïc Frueh
 * @author Aurélien Siu
 */
public class DisplayConstants {
    public static final String COLOR_BEACON = "fill:";

    public enum COLORS_ROOMS {WHITE, LIGHT_GREEN, LIME_GREEN, DARK_GREEN, RED};

    public static final String[] COLOR_VALUES = {"#ffffff", "#99ff99", "#00ff00", "#009900", "#ff0000"};

    /**
     * The title that will be write in the request file in front of all classrooms labels (ex. title: A02)
     */
    public static final String FILE_CLASSROOM_TITLE = "Salle";
    /**
     * The title that will be write in the request file in front of all floors labels (ex. title: G)
     */
    public static final String FILE_FLOOR_TITLE = "Etage";

    public static int getColorIdFromFreePeriods(int freePeriods){
        if(freePeriods <= COLORS_ROOMS.DARK_GREEN.ordinal()){
            return freePeriods;
        } else {
            return COLORS_ROOMS.DARK_GREEN.ordinal();
        }
    }
}
