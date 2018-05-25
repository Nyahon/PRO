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
 * @author Siu Aurélien
 * @version 1.0
 */
package GUI.controllers;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import GUI.model.TimeSpinner;
import GUI.svgTools.PlanLoader;
import GUI.svgTools.SVGToolBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import models.ClassRoom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.TimeSlot;
import controller.Controller;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import utils.DisplayConstants;

import static controller.Controller.closestClassroom;
import static utils.ClassroomsByFloor.FLOORS_MAP;
import static utils.ConfigLoader.PLAN_PATH;
import static utils.DisplayConstants.COLOR_VALUES;
import static utils.DisplayConstants.getColorIdFromFreePeriods;
import static utils.PeriodManager.PERIODS_END;
import static utils.PeriodManager.PERIODS_START;
import static utils.PeriodManager.currentOrNextPeriod;

public class MainViewController implements Initializable {

    private static final HashMap<String, ArrayList<String>> FLOORS = new HashMap<>();
    private static ArrayList<String> currentFloorPaths = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(MainViewController.class.getName());
    private static PlanLoader planLoader = null;
    private static int indexPlan = 0;
    private static final int planWidth = 1800;
    private static final int planHeight = 980;
    private static final SVGToolBox svgToolBox = new SVGToolBox();
    private static String position = "";

    private static String currentFloor = "G";

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
    private Spinner timeSpinner;
    @FXML
    private Label firstPeriod;
    @FXML
    private Label secondPeriod;
    @FXML
    private Label thirdPeriod;

    @FXML
    private Label guiConsole;
    @FXML
    private Circle circleGuiLogger;

    public static GuiLogger guiLogger;


    /**
     * This method fill the hash map with floors (first one to be called in initialize method)
     */
    private void fillFloors() {
        ArrayList<String> floorFileName = new ArrayList<>();

        // CHESEAUX FLOORS PART
        floorFileName.add("floor-A1.svg");
        floorFileName.add("floor-A2.svg");
        FLOORS.put("A", new ArrayList<>(floorFileName));
        floorFileName.clear();

        floorFileName.add("floor-B1.svg");
        //floorFileName.add("floor-B2.svg");
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

        floorFileName.add("floor-J.svg");

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
     * This method is a handler that manage the click in a floor button. It
     * will show the free room from the stage.
     */
    public void newFloorEvent(Event event) throws IOException{

        Button floorButton = (Button) event.getSource(); // get the button from the event
        String idButton = floorButton.getId(); // get the id of the button

        currentFloor = idButton;
        showFloor(idButton, false);
    }


    /**
     * @param floor the floor
     * This method is a handler to show the floor on the GUI
     */
    public void showFloor(String floor, boolean currentClassroom) throws IOException{

        guiLogger.printInfo("Chargement du plan " + floor + " en cours");

        ImageView imgView = imageCheseaux;
        indexPlan = 0;
        currentFloorPaths = FLOORS.get(floor);

        String svgExternFloorPath = "";

        // Regex expression to know which building is the floor
        if (floor.matches("[A-D]") || floor.matches("[G-K]") ) {
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
        } else if (floor.matches("[R-U]")) {
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

        if(!floor.matches("[E-F]")) {
            LocalDate localDate = dateField.getValue();
            LocalTime localTime = LocalTime.parse(timeSpinner.getEditor().getText());

            int beginPeriod = currentOrNextPeriod(localTime);
            int endPeriod = beginPeriod;

            LocalTime beginTime = PERIODS_START.get(beginPeriod);
            firstPeriod.setText(beginTime.toString() + " - " + PERIODS_END.get(endPeriod));
            endPeriod = ++endPeriod % PERIODS_END.size();
            secondPeriod.setText(beginTime.toString() + " - " + PERIODS_END.get(endPeriod));
            endPeriod = ++endPeriod % PERIODS_END.size();
            thirdPeriod.setText(beginTime.toString() + " - " + PERIODS_END.get(endPeriod) + "+");

            // Get first classroom from idButton representing a floor
            String firstClassroom = FLOORS_MAP.get(floor).get(0);

            int periodRequested = currentOrNextPeriod(localTime);

            TimeSlot timeSlotToSend = new TimeSlot(firstClassroom, java.sql.Date.valueOf(localDate).getTime(), periodRequested);

            Map<String, Integer> timeSlotReceived = Controller.handleClientFloorRequest(timeSlotToSend);

            InputStream inputStreamSvg;
            // Getting the required files to modify from inside resource to extern.
            for (int i = 0; i < currentFloorPaths.size(); ++i) {
                inputStreamSvg = getClass().getResourceAsStream("/plans/" + currentFloorPaths.get(i));
                newTempSVGFile(inputStreamSvg, PLAN_PATH + "/tmpPlan" + (i + 1) + ".svg");
                inputStreamSvg.close();
            }
            // Apply color to every classroom in SVG

            //List<String> currentExternSvgPaths = new ArrayList<>();
            for (int i = 0; i < currentFloorPaths.size(); ++i) {

                svgExternFloorPath = PLAN_PATH + "/tmpPlan" + (i + 1) + ".svg";

                svgToolBox.updateSVG(svgExternFloorPath, timeSlotReceived);

            }

            svgExternFloorPath = PLAN_PATH + "/tmpPlan" + 1 + ".svg";
            try {
                planLoader = new PlanLoader(svgExternFloorPath,imgView, planWidth, planHeight, this, currentClassroom);
                new Thread(planLoader).start();
                System.gc();

            } catch (Exception e) {

                Image exceptionImg = new Image("/plans/default-image.png");
                imgView.setImage(exceptionImg);
            }
        } else{
            Image exceptionImg = new Image("/plans/default-image.png");
            imgView.setImage(exceptionImg);
        }

    }

    /**
     * Method to get current SVG at extern of the jar
     *
     * @param svgInternStream
     * @param svgExternPath
     */
    public void newTempSVGFile(InputStream svgInternStream, String svgExternPath){
        try{
            FileOutputStream svgDestFile = new FileOutputStream(new File(svgExternPath));

            IOUtils.copy(svgInternStream, svgDestFile);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Handle search button request
     */
    public void searchButtonHandle(){

        String currentRoom = currentRoomLabel.getText();
        ClassRoom classRoom = new ClassRoom(currentRoom);

        if(currentRoom != null && !currentRoom.isEmpty() && !currentRoom.contains("non définie")){

            LocalTime localTime = LocalTime.parse(timeSpinner.getEditor().getText());
            LocalDate localDate = dateField.getValue();
            ClassRoom closestClassroom = closestClassroom(classRoom, localTime, localDate);
            try {
                showFloor(closestClassroom.getClassRoom().substring(0, 1), true);
                guiLogger.printInfo("Votre salle la plus proche : " + closestClassroom.getClassRoom());
            } catch (IOException e){
                e.printStackTrace();
                guiLogger.printError("Erreur pour charger le plan demandé");
            }
        } else {
            try {
                showFloor(currentFloor, false);
            } catch (Exception e){
                e.printStackTrace();
                guiLogger.printError("Erreur pour charger le plan demandé");
            }
        }
    }

    /**
     * @brief This method is a update your current position.
     *
     * @param keyEvent
     *
     */
    public void updatePosition(KeyEvent keyEvent){

        // update position if key ENTER is pressed
        if(keyEvent.getCode() == KeyCode.ENTER) {
            position = currentRoomField.getText();
            if(!position.equals("")){

                String floor = position.substring(0,1).toUpperCase();
                position = position.replaceFirst(position.substring(0,1), floor.toUpperCase());
                List<String> classrooms = FLOORS_MAP.get(floor);
                if(classrooms != null) {
                    for(int i = 0; i < classrooms.size(); ++i) {
                        if(classrooms.get(i).equals(position)) {
                            currentRoomField.clear();
                            currentRoomLabel.setText(position);
                            return;
                        }
                    }
                }
            }
            guiLogger.printError("Saisie incorrecte ou salle inconnue");
        }
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

        /*
        * Method to check button in Cheseaux ImageView
         */
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
                planLoader = new PlanLoader(PLAN_PATH + "/tmpPlan" + (indexPlan + 1) + ".svg", imgView, planWidth, planHeight, this, false); // "/plans/" + currentFloorPaths.get(indexPlan)
                new Thread(planLoader).start();
            } catch (Exception e) {

                Image exceptionImg = new Image(PLAN_PATH + "/default-image.png");
                imgView.setImage(exceptionImg);
            }

        }

    }

    /**
     * @throws Exception
     * @brief Launch the view for the room schedule.
     */
    public void scheduleRoom() throws Exception {
        // Initializing the FXML
        FXMLLoader fxLoader = new FXMLLoader();
        Parent root = fxLoader.load(getClass().getResource("/RoomScheduleView.fxml"));

        RoomScheduleViewController roomScheduleViewController = new RoomScheduleViewController();
        roomScheduleViewController.setMainViewController(this);
        fxLoader.setController(roomScheduleViewController);

        Scene scene = new Scene(root);

        // Creating and launching the stage
        Stage stage = new Stage();
        stage.setTitle("Horaire d'une salle");
        stage.setWidth(270);
        stage.setHeight(170);
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
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/TimeslotView.fxml"));

        Scene scene = new Scene(root);

        // Creating and launching the stage
        Stage stage = new Stage();
        stage.setTitle("Recherche avancée");
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
     * @param url link text
     * @param rb  Object ResourceBundle
     * @brief Necessary to redefine but not necessary to use it.
     */
    //@Override
    public void initialize(URL url, ResourceBundle rb) {

        fillFloors();
        timeSpinner = new TimeSpinner();
        timeSpinner.setPrefWidth(90.0);
        bottomUserInputHBox.getChildren().set(7, timeSpinner);
        guiLogger = new GuiLogger(guiConsole, circleGuiLogger);
    }

}
