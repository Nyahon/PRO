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
package GUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.ClassRoom;
import models.TimeSlot;

import static controller.Controller.handleClientClassroomRequest;

/**
 * FXML Controller class
 *
 * @author Labinot
 */
public class RoomScheduleViewController implements Initializable {

   @FXML
   private TextField scheduleRoomTextField;

   String classroomRequested = "";

   /**
    * Handler that will give the schedule of the room
    */
   public void giveRoomSchedule () {


      ClassRoom classRoomRequested = new ClassRoom(scheduleRoomTextField.getText());

      try {
         List<TimeSlot> listClass = handleClientClassroomRequest(classRoomRequested);
      } catch (IOException e){
         e.printStackTrace();
      }

      
   }
   
   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO
   }   
   
}
