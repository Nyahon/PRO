/**
 * Module      : PRO
 * File        : ViewController.java
 * Date        : 31.03.2018
 *
 * Goal : This file contains all the method that are necessary to make the interaction
 *       between the GUI and the user work, each event for each object from the GUI
 *       contains a method that will manage the action.
 *
 *
 * Remarks :
 *
 * @author Rashiti Labinot
 * @version 1.0
 */
package Daryll.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainViewController implements Initializable {

   /**
    * @brief This method is a handler that manage the click in a floor button. It
    * will show the free room from the stage.
    * @param event It corresponds to a mouse click for this case.
    * //@see idButton The buttons need to have an ID.
    */
   public void showFloor(Event event) {
      Button floorButton = (Button) event.getSource(); // get the button from the event
      String idButton = floorButton.getId(); // get the id of the button

      clean(floorButton);
      // switch case floor...
   }

   /**
    * @brief This method cleans the pane, it means that the image will not appear
    * because the CSS code is deleted.
    * @param b The necessary button floor to get the scene.
    */
   public void clean(Button b) {
      Scene scene = b.getScene(); // get the scene
      ImageView imgView = (ImageView) scene.lookup("#imageCheseaux"); // get the pane who hostes the image
      imgView.setVisible(false);
      //imgView.setDisable(true); // delete image

   }
   
   /**
    * @brief Launch the view for the room schedule.
    * @throws Exception 
    */
   public void scheduleRoom () throws Exception {
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
   }

}
