package utils;


import javafx.scene.paint.Color;

public class DisplayConstants {
    public static final String COLOR_BEACON = "fill:";

    public enum COLORS_ROOMS {WHITE, LIGHT_GREEN, LIME_GREEN, DARK_GREEN, RED};

    public static final String[] COLOR_VALUES = {"#ffffff", "#99ff99", "#00ff00", "#009900", "#ff0000"};

    public static final String FILE_CLASSROOM_TITLE = "Salle";
    public static final String FILE_FLOOR_TITLE = "Etage";

    public static int getColorIdFromFreePeriods(int freePeriods){
        if(freePeriods <= COLORS_ROOMS.DARK_GREEN.ordinal()){
            return freePeriods;
        } else {
            return COLORS_ROOMS.DARK_GREEN.ordinal();
        }
    }
}
