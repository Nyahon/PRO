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

    public static final String[] COLOR_VALUES = {"#ffffff", "#99ff99", "#A0FF00", "#009900", "#ff0000"};

    /**
     * The title that will be write in the request file in front of all classrooms labels (ex. title: A02)
     */
    public static final String FILE_CLASSROOM_TITLE = "Salle";
    /**
     * The title that will be write in the request file in front of all floors labels (ex. title: G)
     */
    public static final String FILE_FLOOR_TITLE = "Etage";

    public static final String FILE_ALL_OCCUPIED_MESSAGE = "Cette salle est occupée pour toutes les tranches horaires demandées";

    public static final String LOG_WRONG_CLASSROOM = "Saisie incorrecte ou salle inconnue";

    public static final String LOG_SCHEDULE_FILE_CREATED = "Le fichier " + ConfigLoader.getOutputFilename() + " a été généré";

    public static final String LOG_SERVER_ERROR = "Erreur de connexion avec le serveur";
    public static final String MAIN_VIEW_FXML = "MainView.fxml";
    public static final String ROOM_SCHEDULE_FXML = "RoomScheduleView.fxml";
    public static final String TIMESLOT_FXML = "TimeslotView.fxml";
    public static final String ABOUT_FXML = "AboutView.fxml";

    public static int getColorIdFromFreePeriods(int freePeriods){
        if(freePeriods <= COLORS_ROOMS.DARK_GREEN.ordinal()){
            return freePeriods;
        } else {
            return COLORS_ROOMS.DARK_GREEN.ordinal();
        }
    }


}
