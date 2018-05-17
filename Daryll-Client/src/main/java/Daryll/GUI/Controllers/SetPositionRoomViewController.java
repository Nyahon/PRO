/**
 * Module      : PRO
 * File        : RoomScheduleViewController.java
 * Date        : 12.04.2018
 *
 * Goal : Controller managing the schedule from a room.
 *
 * Remarks :
 *
 * @author Rashiti Labinot
 * @version 1.0
 */
package Daryll.GUI.Controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author Labinot
 */
public class SetPositionRoomViewController implements Initializable {


   @FXML
   private TextField positionRoomTextField;
   /**
    * Handler that will give the position of the room
    */
   public void giveRoomPosition(Event event) {

      MainViewController.updatePositionLabel(positionRoomTextField.getText());

   }
   
   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO
   }   
   
}
