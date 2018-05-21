/**
 * Module      : PRO
 * File        : ViewController.java
 * Date        : 31.03.2018
 * <p>
 * Goal : This file contains all the method that are necessary to make the interaction
 * between the GUI and the user work, each event for each object from the GUI
 * contains a method that will manage the action.
 * <p>
 * <p>
 * Remarks :
 *
 * @author Rashiti Labinot
 * @version 1.0
 */
package GUI.controllers;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

import GUI.model.TimeSpinner;
import GUI.plan.PlanLoader;
import GUI.svgTools.SVGToolBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import models.AdvancedRequest;
import utils.ClassroomsByFloor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.TimeSlot;
import controller.Controller;
import utils.PeriodManager;

import static controller.Controller.handleClientClassroomRequest;

public class MainViewController implements Initializable {

    private static final HashMap<String, ArrayList<String>> FLOORS = new HashMap<>();
    private static ArrayList<String> currentFloorPaths = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(MainViewController.class.getName());
    private static PlanLoader planLoader = null;
    private static int indexPlan = 0;
    private static final int planWidth = 2100;
    private static final int planHeight = 980;
    private static final SVGToolBox svgToolBox = new SVGToolBox();

    private static String position = "";
    @FXML
    private ImageView imageCheseaux;
    @FXML
    private ImageView imageStRoch;
    @FXML
    private Button nextCheseaux;
    @FXML
    private Button previousCheseaux;
    @FXML
    private Button nextStRoch;
    @FXML
    private Button previousStRoch;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField currentRoomField;
    @FXML
    private Label currentRoomLabel;
    @FXML
    private HBox bottomUserInputHBox;
    @FXML
    private Button enterPositionButton;
    @FXML
    private Spinner hourSpinner;
    @FXML
    private Label guiConsole;
    @FXML
    Circle circleGuiLogger;

    private GuiLogger guiLogger;

    /**
     * @brief This method is the first one to be called to fill the hash map with floors
     */
    private void fillFloors() {
        ArrayList<String> floorFileName = new ArrayList<>();

        // CHESEAUX FLOORS PART
        floorFileName.add("floor-A1.svg");
        floorFileName.add("floor-A2.svg");
        FLOORS.put("A", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-B1.svg");
        floorFileName.add("floor-B2.svg");
        floorFileName.add("floor-B3.svg");
        FLOORS.put("B", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-C1.svg");
        floorFileName.add("floor-C2.svg");
        floorFileName.add("floor-C3.svg");
        FLOORS.put("C", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-D1.svg");
        floorFileName.add("floor-D2.svg");
        FLOORS.put("D", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-G.svg");
        FLOORS.put("G", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-H.svg");
        FLOORS.put("H", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-J.svg"); //Needs to be added in resources

        FLOORS.put("J", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-K.svg");
        FLOORS.put("K", new ArrayList<>(floorFileName));
        floorFileName.clear();

        // ST-ROCH FLOORS PART
        floorFileName.add("floor-R.svg");
        FLOORS.put("R", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-S.svg");
        FLOORS.put("S", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-T.svg");
        FLOORS.put("T", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-U.svg");
        FLOORS.put("U", new ArrayList<>(floorFileName));
        floorFileName.clear();
    }

    /**
     * @param event It corresponds to a mouse click for this case.
     *              //@see idButton The buttons need to have an ID.
     * @brief This method is a handler that manage the click in a floor button. It
     * will show the free room from the stage.
     */
    public void showFloor(Event event) throws IOException{

        Button floorButton = (Button) event.getSource(); // get the button from the event
        String idButton = floorButton.getId(); // get the id of the button

        ImageView imgView = null;
        indexPlan = 0;
        currentFloorPaths = FLOORS.get(idButton);

        // Regex expression to know which building is the floor
        if (idButton.matches("[A-K]")) {
            imgView = imageCheseaux;
            previousCheseaux.setDisable(true);
            if(currentFloorPaths != null) {
                if (currentFloorPaths.size() == 1) {
                    nextCheseaux.setDisable(true);
                } else {
                    nextCheseaux.setDisable(false);
                }
            } else {
                nextCheseaux.setDisable(true);
            }
        } else if (idButton.matches("[R-U]")) {
            imgView = imageStRoch;
            previousStRoch.setDisable(true);
            if(currentFloorPaths != null) {
                if (currentFloorPaths.size() == 1) {
                    nextStRoch.setDisable(true);
                } else {
                    nextStRoch.setDisable(false);
                }
            } else {
                nextStRoch.setDisable(true);
            }
        }

        LocalDate localDate = dateField.getValue();

        LocalTime localTime = (LocalTime) hourSpinner.getValue();
        System.out.println(localTime);

        // Get first classroom from idButton representing a floor (we do not care here about specific classroom)
        String firstClassroom = ClassroomsByFloor.FloorsMap.get(idButton).get(0);

        int periodRequested = PeriodManager.currentOrNextPeriod(localTime);
        System.out.println(periodRequested);
        TimeSlot timeSlotToSend = new TimeSlot(firstClassroom, java.sql.Date.valueOf(localDate).getTime(), periodRequested);


        //List<TimeSlot> classroomResponse = handleClientClassroomRequest(timeSlotToSend);

        // Standard version
        Map<String, Integer> timeSlotReceived = Controller.handleClientFloorRequest(timeSlotToSend);

        /*AdvancedRequest advancedRequest = new AdvancedRequest(0, java.sql.Date.valueOf("2018-05-22"), 2, null, null);

        List<AdvancedRequest> advancedRequests = new ArrayList<>(Arrays.asList(advancedRequest));
        List<TimeSlot> timeSlotReceived = Controller.handleClientAdvancedRequest(advancedRequests);*/

        String firstSvgFloorPath = "/plans/" + currentFloorPaths.get(0);
        for(HashMap.Entry<String, Integer> classroom : timeSlotReceived.entrySet()){


            String classroomName = classroom.getKey();
            int freePeriods = classroom.getValue();
            //DisplayConstants.COLORS_ROOMS colors_rooms =  nextOccupiedPeriod - periodRequested;
            switch (freePeriods){
                case 0:
                    svgToolBox.updateSVG(firstSvgFloorPath, classroomName, "#ffffff");
                    break;
                case 1:
                    svgToolBox.updateSVG(firstSvgFloorPath, classroomName, "#99ff99");
                    break;
                case 2:
                    svgToolBox.updateSVG(firstSvgFloorPath, classroomName, "#00ff00");
                    break;
                case 3:
                    svgToolBox.updateSVG(firstSvgFloorPath, classroomName, "#009900");
                    break;
                default:
                    svgToolBox.updateSVG(firstSvgFloorPath, classroomName, "#009900");
                    break;
            }

        }

        try {
            //imgView.setImage(new Thread);
            planLoader = new PlanLoader(firstSvgFloorPath,imgView, planWidth, planHeight);
            new Thread(planLoader).start();
            System.gc();
           //loadSvgImage( "/plans/" + currentFloorPaths.get(0), imgView, planWidth, planHeight);

        } catch (Exception e) {

            Image exceptionImg = new Image("/plans/default-image.png");
            imgView.setImage(exceptionImg);
        }
    }


    /**
     * @brief This method is a handler to load an SVG file image into an ImageView
     *
     * @param path
     * @param imgView The transcoded image will be put inside the image
     * @param width
     * @param height
     *
     */
    public void loadSvgImage(String path, ImageView imgView, int width, int height){

        planLoader.openSVGFile(path);

        // Select the Pane where the image will be displayed
        Image img = planLoader.getTranscodedImage(width, height);

        // Put the image inside an ImageView object
        imgView.setImage(img);
        System.gc();
    }


    public void updatePosition(){

        position =  currentRoomField.getText();
        currentRoomField.clear();
        enterPositionButton.setText("Modifier");
        currentRoomLabel.setText(position);
        guiLogger.printInfo(position);

    }

    /**
     * @param event It corresponds to a mouse click for this case.
     *              //@see idButton The buttons need to have an ID.
     * @brief This method is a handler that manage the click in a floor button. It
     * will show the free room from the stage.
     */
    public void nextPlan(Event event) {

        Button b = (Button) event.getSource(); // get the button from the event

        String idButton = b.getId(); // get the id of the button

        // Test affichage console
        System.out.println(idButton);

        if (idButton.equals("nextCheseaux") || idButton.equals("previousCheseaux")){
            if (idButton.equals("nextCheseaux")) {
                ++indexPlan;
            } else {
                --indexPlan;
            }
            ImageView imgView = imageCheseaux; // get the pane who hostes the image

            if (currentFloorPaths.size() - 1 == indexPlan) {
                nextCheseaux.setDisable(true);
            } else {
                nextCheseaux.setDisable(false);
            }
            if(indexPlan == 0){
                previousCheseaux.setDisable(true);
            } else{
                previousCheseaux.setDisable(false);
            }
            try {
                //loadSvgImage( "/plans/" + currentFloorPaths.get(indexPlan), imgView, planWidth, planHeight);

                planLoader = new PlanLoader("/plans/" + currentFloorPaths.get(indexPlan),imgView, planWidth, planHeight);
                new Thread(planLoader).start();
            } catch (Exception e) {

                Image exceptionImg = new Image("/plans/default-image.png");
                imgView.setImage(exceptionImg);
            }

        }

    }

    /**
     * @brief This method cleans the pane, it means that the image will not appear
     * because the CSS code is deleted.
     * @param b The necessary button floor to get the scene.
     *
    public void clean(Button b) {
    Scene scene = b.getScene(); // get the scene
    ImageView imgView = (ImageView) scene.lookup("#imageCheseaux"); // get the pane who hostes the image
    imgView.setVisible(false);
    //imgView.setDisable(true); // delete image
    }*/


    /**
     * @throws Exception
     * @brief Launch the view for the room schedule.
     */
    public void scheduleRoom() throws Exception {
        // Initializing the FXML
        FXMLLoader fxLoader = new FXMLLoader();
        Parent root = fxLoader.load(getClass().getResource("/RoomScheduleView.fxml"));

        Scene scene = new Scene(root);

        // Creating and launching the stage
        Stage stage = new Stage();
        stage.setTitle("Horaire de la salle");
        stage.setWidth(270);
        stage.setHeight(170);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @throws java.lang.Exception
     * @brief This method is a handler that manage the click of the shortest room menu
     * button option. It will popup a little windows to enter the position and then it
     * will give the shortest free room from a given place.
     */
    public void shortestFreeRoom() throws Exception {
        // Initializing the FXML

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/ShortestRoomView.fxml"));

        TimeslotViewController timeslotViewController = fxmlLoader.getController();
        Scene scene = new Scene(root);

        // Creating and launching the stage
        Stage stage = new Stage();
        stage.setTitle("Salle la plus proche");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @throws java.io.IOException
     * @brief This method is a handler that manage the click of schedule rooms button
     * option. It will popup a little windows to enter some informations to get a
     * little planning of free rooms from timestamp.
     */
    public void timeslot() throws IOException {
        // Initializing the FXML
        Parent root = FXMLLoader.load(getClass().getResource("/TimeslotView.fxml"));
        Scene scene = new Scene(root);

        // Creating and launching the stage
        Stage stage = new Stage();
        stage.setTitle("Crénaux horaires");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @brief This method is a handler for the about option in the menu. It will
     * display a popup with a text that contains the informations.
     */
    public void about() throws IOException {
        // Creating the stage
        Parent root = FXMLLoader.load(getClass().getResource("/AboutView.fxml"));
        Scene scene = new Scene(root);

        // Launching the stage
        Stage stage = new Stage();
        stage.setTitle("À propos");
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @brief This method is a handler for the guide option in the menu. It will
     * redirect to a web page of your project in Github
     */
    public void howToUse() throws Exception {
        String url = "https://github.com/Nyahon/PRO";
        URI uri = new URI(url);
        Desktop.getDesktop().browse(uri);
    }

    public class GuiLogger {
        private Label console;

        private final Paint colorInfo = Color.LIMEGREEN;
        private final Paint colorError = Color.RED;

        public GuiLogger(Label console) {
            this.console = console;
        }

        public void printInfo(String valueOf) {
            Platform.runLater(() -> {
                circleGuiLogger.setFill(colorInfo);
                console.setTextFill(colorInfo);
                console.setText(valueOf);
            });
        }

        public void printError(String valueOf) {
            Platform.runLater(() -> {
                circleGuiLogger.setFill(colorError);
                console.setTextFill(colorError);
                console.setText(valueOf);
            });
        }

    }

    /**
     * @param url link text
     * @param rb  Object ResourceBundle
     * @brief Necessary to redefine but not necessary to use it.
     */
    //@Override
    public void initialize(URL url, ResourceBundle rb) {

        fillFloors();
        hourSpinner = new TimeSpinner();
        hourSpinner.setPrefWidth(90.0);
        bottomUserInputHBox.getChildren().set(5, hourSpinner);
        guiLogger = new GuiLogger(guiConsole);
    }

}
