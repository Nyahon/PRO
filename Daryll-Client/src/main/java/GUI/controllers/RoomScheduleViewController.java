/**
 * Module      : PRO
 * File        : RoomScheduleViewController.java
 * Date        : 12.04.2018
 * <p>
 * Goal : Controller managing the schedule from a room.
 * <p>
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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.ClassRoom;
import utils.ClassroomsByFloor;

import static controller.Controller.handleClientClassroomRequest;

/**
 * FXML Controller class
 *
 * @author Labinot Rashiti
 * @author Aurélien Siu
 */
public class RoomScheduleViewController implements Initializable {

    @FXML
    private TextField scheduleRoomTextField;
    @FXML
    private Button scheduleRoomButton;

    private MainViewController mainViewController;

    /**
     * Handler that will give the schedule of the room
     */
    public void giveRoomSchedule(Event event) {


        String classroomName = scheduleRoomTextField.getText().toUpperCase();

        mainViewController.guiLogger.printInfo("Bonjour");

        if (classroomName.isEmpty()) {
            return;
        }
        ClassRoom classroomRequested = new ClassRoom(classroomName);

        String floor = classroomRequested.getClassRoom().substring(0, 1);

        List<String> classroomFromFloor = ClassroomsByFloor.FLOORS_MAP.get(floor);

        if (classroomFromFloor != null) {
            for (int i = 0; i < classroomFromFloor.size(); ++i) {
                if (classroomFromFloor.get(i).equals(classroomName)) {
                    try {
                        handleClientClassroomRequest(classroomRequested);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Button button = (Button) event.getSource();
                    Scene scene = button.getScene();
                    ((Stage) scene.getWindow()).close();

                    break;
                }
            }

        }
    }

    public void setMainViewController(MainViewController mainViewController){
        this.mainViewController = mainViewController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
