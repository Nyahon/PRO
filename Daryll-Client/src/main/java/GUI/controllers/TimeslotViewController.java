/**
 * Module      : PRO
 * File        : TimeslotViewController.java
 * Date        : 12.04.2018
 * <p>
 * Goal : Controller managing the timeslot view.
 * <p>
 * <p>
 * Remarks : -
 *
 * @author Labinot Rashiti
 * @author Aurélien Siu
 * @version 1.0
 */
package GUI.controllers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import GUI.model.AdvancedRequestForm;
import controller.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.AdvancedRequest;
import utils.ConfigLoader;
import utils.PeriodManager;

/**
 * FXML Controller class
 *
 * @author Labinot
 * @author Aurélien
 */
public class TimeslotViewController implements Initializable {

    private static final int MAX_ADVANCED_REQUESTED_FORMS = 5;
    private static List<AdvancedRequestForm> advancedRequestForms = new ArrayList<>();
    @FXML
    private VBox requestsFormsBox;
    @FXML
    private Button searchButton;

    private MainViewController mainViewController;

    /**
     * EventHandler for the add button
     */
    private EventHandler addButtonClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            // Set properties of the second to last element
            int lastIndex = advancedRequestForms.size() - 1;
            AdvancedRequestForm lastForm = advancedRequestForms.get(lastIndex);
            lastForm.getAddButton().setDisable(true);
            lastForm.getRemoveButton().setDisable(true);

            // Creating new AdvancedRequestForm
            AdvancedRequestForm newForm = new AdvancedRequestForm();
            newForm.getAddButton().setOnMouseClicked(addButtonClicked);
            newForm.getRemoveButton().setOnMouseClicked(removeButtonClicked);
            newForm.setEndHourSpinner(LocalTime.of(23,59));

            if (lastIndex == MAX_ADVANCED_REQUESTED_FORMS) {
                newForm.getAddButton().setDisable(true);
            }

            advancedRequestForms.add(newForm);
            requestsFormsBox.getChildren().add(newForm.getGridPane());


        }
    };

    /**
     * EventHandler for the remove button
     */
    private EventHandler removeButtonClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            int beforelastIndex = advancedRequestForms.size() - 2;
            AdvancedRequestForm lastForm = advancedRequestForms.get(beforelastIndex);
            lastForm.getAddButton().setDisable(false);
            lastForm.getRemoveButton().setDisable(false);

            int lastIndex = advancedRequestForms.size();
            int lastIndexGraphic = requestsFormsBox.getChildren().size() - 1;

            AdvancedRequestForm formToRemove = advancedRequestForms.get(lastIndex - 1);
            requestsFormsBox.getChildren().remove(lastIndex);
            advancedRequestForms.remove(lastIndex - 1);
        }
    };

    /**
     * EventHandler for the search button
     */
    private EventHandler searchButtonClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

         /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {*/
            mainViewController.guiLogger.printInfo("Récupération des données en cours");
            List<AdvancedRequest> requests = new ArrayList<>();

            for (AdvancedRequestForm arf : advancedRequestForms) {

                int beginPeriod = PeriodManager.currentOrNextPeriod(arf.getBeginTime());
                int endPeriod = PeriodManager.currentOrNextPeriod(arf.getEndTime());

                String tmpFloor = arf.getFloorName();
                String floorName = null;

                if (!tmpFloor.equals("Tous")) {
                    floorName = tmpFloor;
                }

                String tmpClassroom = arf.getClassroomName();
                String classroomName = null;

                if (!tmpClassroom.equals("Tous")) {
                    classroomName = tmpClassroom;
                }

                AdvancedRequest advancedRequest = new AdvancedRequest(arf.getBuilding(), Date.valueOf(arf.getBeginDate()), Date.valueOf(arf.getEndDate()), beginPeriod, endPeriod, floorName, classroomName);
                requests.add(advancedRequest);
            }
            try {
                Controller.handleClientAdvancedRequest(requests);
            } catch (IOException e) {
                e.printStackTrace();
                mainViewController.guiLogger.printInfo("Erreur lors de la récupération des données");
            }

            mainViewController.guiLogger.printInfo("Le fichier " + ConfigLoader.getOutputFilename() + " a été généré");
        }
    };


    /**
     * Initializes the controller class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        AdvancedRequestForm advancedRequestForm = new AdvancedRequestForm();
        advancedRequestForm.getRemoveButton().setVisible(false);
        advancedRequestForm.getAddButton().setOnMouseClicked(addButtonClicked);

        advancedRequestForm.setEndHourSpinner(LocalTime.of(23,59));
        advancedRequestForms.add(advancedRequestForm);

        requestsFormsBox.getChildren().add(advancedRequestForm.getGridPane());

        searchButton.setOnMouseClicked(searchButtonClicked);
    }


    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

}
