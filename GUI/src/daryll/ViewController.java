/**
 * Module      : PRO
 * File        : ViewController.java
 * Date        : 31.03.2018
 *
 * Goal : This file contains all the method that are necessary to make the interaction
 *       between the GUI and the user work. Each event for each object from the GUI
 *       contains a method that will manage the action.
 *       
 *
 * Remarks : 
 *
 * @author Rashiti Labinot
 * @version 1.0
 */

package daryll;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class ViewController implements Initializable {
   

   /**
    * @brief This method is a handler that manage the click in a floor button. It will
    * show the free room from the stage.
    * @param event It corresponds to a mouse click for this case.
    * @see The buttons need to have an ID.
    */
   @FXML
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
   @FXML
   public void clean(Button b) {
      Scene scene = b.getScene(); // get the scene
      Pane pane = (Pane) scene.lookup("#PANE"); // get the pane who hostes the image
      pane.styleProperty().setValue(""); // delete image
   }
   
   /**
    * @brief Necessary to redefine but not necessary to use it.
    * @param url link text
    * @param rb Object ResourceBundle
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
   }   
   
}
