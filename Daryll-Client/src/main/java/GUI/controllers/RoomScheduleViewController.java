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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.ClassRoom;
import utils.ClassroomsByFloor;

import static controller.Controller.handleClientClassroomRequest;
import static utils.ClassroomsByFloor.isClassroomValid;
import static utils.DisplayConstants.LOG_SCHEDULE_FILE_CREATED;
import static utils.DisplayConstants.LOG_SERVER_ERROR;
import static utils.DisplayConstants.LOG_WRONG_CLASSROOM;

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

    public void handleKeyInput(KeyEvent keyEvent){
        if(((KeyEvent)keyEvent).getCode() == KeyCode.ENTER) {
            giveRoomSchedule(keyEvent);
        }
    }

    /**
     * Method to catch the search button event
     * @param event
     */
    public void handleSearchButton(Event event){
        giveRoomSchedule(event);
    }

    /**
     * Handler that will give the schedule of the room
     * @param event
     */
    public void giveRoomSchedule(Event event) {

        String classroomName = scheduleRoomTextField.getText();

        String floor = classroomName.substring(0, 1).toUpperCase();
        classroomName = classroomName.replaceFirst(classroomName.substring(0, 1), floor);
        if (isClassroomValid(classroomName)) {

            ClassRoom classroomRequested = new ClassRoom(classroomName);

            List<String> classroomFromFloor = ClassroomsByFloor.FLOORS_MAP.get(floor);

            if (classroomFromFloor != null) {
                for (int i = 0; i < classroomFromFloor.size(); ++i) {
                    if (classroomFromFloor.get(i).equals(classroomName)) {
                        try {
                            handleClientClassroomRequest(classroomRequested);
                        } catch (Exception e) {
                            MainViewController.guiLogger.printError(LOG_SERVER_ERROR);
                            return;
                        }
                        Control control =  (Control)event.getSource();
                        Scene scene = control.getScene();
                        ((Stage) scene.getWindow()).close();
                        mainViewController.guiLogger.printInfo(LOG_SCHEDULE_FILE_CREATED);
                        return;
                    }
                }

            }
        }
        mainViewController.guiLogger.printError(LOG_WRONG_CLASSROOM);
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
