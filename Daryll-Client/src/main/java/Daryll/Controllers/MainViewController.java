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
package Daryll.Controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import Daryll.Plan.PlanLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.util.locale.provider.LocaleServiceProviderPool;

import javax.imageio.ImageIO;

public class MainViewController implements Initializable {

    private static final HashMap<String, ArrayList<String>> FLOORS = new HashMap<>();

    private static final Logger LOG = Logger.getLogger(MainViewController.class.getName());

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

      /*floorFileName.add("floor-J.svg"); Needs to be added in resources

      FLOORS.put("J", new ArrayList<>(floorFileName));
      floorFileName.clear();*/

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
     * @brief This method is a handler that manage the click in a floor button. It
     * will show the free room from the stage.
     * @param event It corresponds to a mouse click for this case.
     * //@see idButton The buttons need to have an ID.
     */
    public void showFloor(Event event) {

        Button b = (Button) event.getSource(); // get the button from the event

        Scene scene = b.getScene(); // get the scene
        String idButton = b.getId(); // get the id of the button

        Pane pane = null;
        ImageView imgView = null;
        Image img;
        // Regex expression to know which building is the floor
        if (idButton.matches("[A-K]")) {
            imgView = (ImageView) scene.lookup("#imageCheseaux"); // get the pane who hostes the image
            pane = (Pane) scene.lookup("#planCheseaux");
        } else if (idButton.matches("[R-U]")) {
            imgView = (ImageView) scene.lookup("#imageStRoch"); // get the pane who hostes the image
            pane = (Pane) scene.lookup("#planStRoch");
        }

        try {
            PlanLoader planLoader = new PlanLoader();
            planLoader.createPlanFromSVGFile("/Daryll/plans/" + FLOORS.get(idButton).get(0));

            int width = 1200;
            int height = 700;

            // Select the Pane where the image will be displayed
            img = planLoader.getTranscodedImage(width, height);

            // Put the image inside an ImageView object
            imgView.setImage(img);

        } catch (Exception e) {

            Image exceptionImg = new Image("/Daryll/plans/default-image.png");
            imgView.setImage(exceptionImg);

        }
        // imgView settings
        imgView.setPreserveRatio(true);
        imgView.fitWidthProperty().bind(pane.widthProperty());

        // switch case floor...
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
     * @brief This method cleans the pane, it means that the image will not appear
     * because the CSS code is deleted.
     * @param b The necessary button floor to get the scene.
     */
    public void setPlan(Button b) {
        Scene scene = b.getScene(); // get the scene
        String idButton = b.getId(); // get the id of the button
        //char charIdButton = idButton.charAt(0);

        ImageView imgView = null;
        Pane pane = null;
        // Regex expression to know which building is the floor
        if (idButton.matches("[A-K]")) {
            imgView = (ImageView) scene.lookup("#imageCheseaux"); // get the pane who hostes the image
            pane = (Pane) scene.lookup("#planCheseaux");
        } else if (idButton.matches("[R-U]")) {
            imgView = (ImageView) scene.lookup("#imageStRoch"); // get the pane who hostes the image
            pane = (Pane) scene.lookup("#planStRoch");
        }

        PlanLoader planLoader = new PlanLoader();
        planLoader.createPlanFromSVGFile("/Daryll/plans/" + FLOORS.get(idButton).get(0));

        int width = 1200;
        int height = 800;

        // Select the Pane where the image will be displayed
        Image img = planLoader.getTranscodedImage(width, height);

        // Put the image inside an ImageView object
        imgView.setImage(img);

        // imgView settings
        imgView.setPreserveRatio(true);
        imgView.fitWidthProperty().bind(pane.widthProperty());

    }

    /**
     * @brief Launch the view for the room schedule.
     * @throws Exception
     */
    public void scheduleRoom() throws Exception {
        // Initializing the FXML
        Parent root = FXMLLoader.load(getClass().getResource("/Daryll/RoomScheduleView.fxml"));
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
     * @brief This method is a handler that manage the click of the shortest room menu
     * button option. It will popup a little windows to enter the position and then it
     * will give the shortest free room from a given place.
     * @throws java.lang.Exception
     */
    public void shortestFreeRoom() throws Exception {
        // Initializing the FXML

        Parent root = FXMLLoader.load(getClass().getResource("/Daryll/ShortestRoomView.fxml"));
        Scene scene = new Scene(root);

        // Creating and launching the stage
        Stage stage = new Stage();
        stage.setTitle("Salle la plus proche");
        stage.setWidth(270);
        stage.setHeight(170);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @brief This method is a handler that manage the click of schedule rooms button
     * option. It will popup a little windows to enter some informations to get a
     * little planning of free rooms from timestamp.
     * @throws java.io.IOException
     */
    public void timeslot() throws IOException {
        // Initializing the FXML
        Parent root = FXMLLoader.load(getClass().getResource("/Daryll/TimeslotView.fxml"));
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
    public void about() {
        // Creating the stage
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("À propos");

        // Creating the textArea
        TextArea textArea = new TextArea("Text à remplir...");
        textArea.setEditable(false);

        // Show all
        Scene scene = new Scene(textArea);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @brief This method is a handler for the guide option in the menu. It will
     * display a popup with a text that contains the informations.
     */
    public void howToUse() {
        // Creating the stage
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Guide d'utilisation");

        // Creating the textArea
        TextArea textArea = new TextArea("Text à remplir...");
        textArea.setEditable(false);

        // Show all
        Scene scene = new Scene(textArea);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @brief Necessary to redefine but not necessary to use it.
     * @param url link text
     * @param rb Object ResourceBundle
     */
    //@Override
    public void initialize(URL url, ResourceBundle rb) {
        fillFloors();
    }

}
