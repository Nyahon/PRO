package utils;


import javafx.scene.paint.Color;

public class DisplayConstants {
    public static final String COLOR_BEACON = "fill:";

    public enum COLORS_ROOMS {WHITE, LIGHT_GREEN, LIME_GREEN, DARK_GREEN, RED};

    public static final String[] COLOR_VALUES = {"#ffffff", "#99ff99", "#00ff00", "#009900"};

    public static int getColorIdFromFreePeriods(int freePeriods){
        if(freePeriods <= 3){
            return freePeriods;
        } else {
            return 3;
        }
    }
}
