/**
 * Module      : PRO
 * File        : TimeslotViewController.java
 * Date        : 12.04.2018
 *
 * Goal : Controller managing the timeslot view.
 *
 * 
 * Remarks : -
 *
 * @author Rashiti Labinot
 * @version 1.0
 */
package GUI.controllers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.sql.Date;
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
import utils.PeriodManager;

/**
 * FXML Controller class
 *
 * @author Labinot
 * @author Aurélien
 */
public class TimeslotViewController implements Initializable {

   private static final int MAX_ADVANCED_REQUESTED_FORMS = 7;
   @FXML
   private VBox requestsFormsBox;

   @FXML
   private Button searchButton;

   private MainViewController mainViewController;

   private static List<AdvancedRequestForm> advancedRequestForms = new ArrayList<>();


   private EventHandler addButtonClicked = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
         int lastIndex = advancedRequestForms.size() - 1;
         if(lastIndex < MAX_ADVANCED_REQUESTED_FORMS) {
            AdvancedRequestForm lastForm = advancedRequestForms.get(lastIndex);
            System.out.println(lastForm.getAddButton().getText());
            lastForm.getAddButton().setDisable(true);
            lastForm.getRemoveButton().setDisable(true);

            AdvancedRequestForm newForm = new AdvancedRequestForm();
            newForm.getAddButton().setOnMouseClicked(addButtonClicked);
            newForm.getRemoveButton().setOnMouseClicked(removeButtonClicked);
            advancedRequestForms.add(newForm);
            requestsFormsBox.getChildren().add(newForm.getGridPane());
         }
      }
   };

   private EventHandler removeButtonClicked = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {

         int beforelastIndex = advancedRequestForms.size() - 2;
         AdvancedRequestForm lastForm = advancedRequestForms.get(beforelastIndex);
         lastForm.getAddButton().setDisable(false);
         lastForm.getRemoveButton().setDisable(false);

         int lastIndex = advancedRequestForms.size();
         int lastIndexGraphic = requestsFormsBox.getChildren().size() - 1;

         System.out.println("last list : " + lastIndex + "last graphic: " + lastIndexGraphic);
         AdvancedRequestForm formToRemove = advancedRequestForms.get(lastIndex - 1);
         requestsFormsBox.getChildren().remove(lastIndex);
         advancedRequestForms.remove(lastIndex - 1);
      }
   };

   private EventHandler searchButtonClicked = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {

         /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {*/
               System.out.println("REQUETE");
               List<AdvancedRequest> requests = new ArrayList<>();

               for(AdvancedRequestForm arf : advancedRequestForms) {

                  int beginPeriod = PeriodManager.currentOrNextPeriod(arf.getBeginTime());
                  int endPeriod = PeriodManager.currentOrNextPeriod(arf.getEndTime());
                  //AdvancedRequest advancedRequest = new AdvancedRequest(arf.getBuilding(), Date.valueOf(arf.getBeginDate()), Date.valueOf(arf.getEndDate()),beginPeriod,null,null);

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

                  System.out.println();
                  System.out.println("Building: " + arf.getBuilding() + " datebegin: " + Date.valueOf(arf.getBeginDate())
                          + "dateEnd: " + Date.valueOf(arf.getEndDate()) + " periodBegin : " + beginPeriod + " floor: " + floorName + " classroom : " + classroomName);
                  AdvancedRequest advancedRequest = new AdvancedRequest(arf.getBuilding(), Date.valueOf(arf.getBeginDate()), Date.valueOf(arf.getEndDate()), beginPeriod, endPeriod, floorName, classroomName);
                  requests.add(advancedRequest);
               }
                  try {
                     Controller.handleClientAdvancedRequest(requests);
                  } catch (IOException e){
                     e.printStackTrace();
                  }


               //mainViewController.guiLogger.printInfo("Advance request Done");

         //   }
         //});


      }
   };

   /**
    * Give a schedule for each day with the free rooms.
    */
   public void giveRoomSchedule() {
      
   }
   
   /**
    * Initializes the controller class
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

      AdvancedRequestForm advancedRequestForm = new AdvancedRequestForm();
      advancedRequestForm.getRemoveButton().setVisible(false);
      advancedRequestForm.getAddButton().setOnMouseClicked(addButtonClicked);

      advancedRequestForms.add(advancedRequestForm);

      requestsFormsBox.getChildren().add(advancedRequestForm.getGridPane());

      searchButton.setOnMouseClicked(searchButtonClicked);
   }

   public void setMainViewController(MainViewController mainViewController){
      this.mainViewController = mainViewController;
   }
   
}
