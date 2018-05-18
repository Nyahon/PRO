/**
 * Module      : PRO
 * File        : ShortestRoomViewController.java
 * Date        : 12.04.2018
 *
 * Goal : Controller managing the shortestRoomView.
 *
 * 
 * Remarks : -
 *
 * @author Rashiti Labinot
 * @version 1.0
 */
package GUI.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Labinot
 */
public class ShortestRoomViewController implements Initializable {
   
   private String currentRoom;
   
   /**
    * Set the room given by the user.
    * @param t textField required to get the scene
    */
   public void setCurrentRoom(TextField t) {
      Scene scene = t.getScene(); // get the scene
      TextField textField = (TextField) scene.lookup("#shortestRoomTextField"); // get the pane who hostes the image
      currentRoom = textField.getText();
   }
   
   
   /**
    * Give the shortest room from the TextLabel entry.
    */
   public void searchShortestRoom() {
      
   }
   
   
   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO
   }   
   
}
