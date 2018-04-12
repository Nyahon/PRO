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
package daryll.View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.plugin.javascript.navig.Anchor;

public class ViewController implements Initializable {

   /**
    * @brief This method is a handler that manage the click in a floor button. It
    * will show the free room from the stage.
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
    * @brief This method is a handler that manage the click of the shortest room menu
    * button option. It will popup a little windows to enter the position and then it
    * will give the shortest free room from a given place.
    */
   @FXML
   public void shortestFreeRoom() {
      // Creating the stage
      Stage stage = new Stage();
      stage.setResizable(false);

      // Creating the label
      Label label = new Label("Entrez la lettre de votre salle courante");

      // Creating the textfield
      TextField textField = new TextField("Lettre de la salle...");
      textField.setAlignment(Pos.CENTER);

      // Make an anonymous class with event handler for this button
      Button button = new Button("Chercher !");

      // Creating the anchorPane
      VBox vbox = new VBox();
      vbox.getChildren().add(label);
      vbox.getChildren().add(textField);
      vbox.getChildren().add(button);
      vbox.setAlignment(Pos.CENTER);
      vbox.isVisible();

      // Show all
      Scene scene = new Scene(vbox);
      stage.setScene(scene);
      stage.show();
   }

   /**
    * @brief This method is a handler that manage the click of schedule rooms button
    * option. It will popup a little windows to enter some informations to get a
    * little planning of free rooms from timestamp.
    */
   @FXML
   public void scheduleRooms() {
      // Creating the stage
      Stage stage = new Stage();
      stage.setResizable(false);

      // Creating the label
      Label labelDate = new Label("Entrez la date de début et la fin date de fin");

      // Creating the textfield
      TextField textFieldDate = new TextField("jj.mm.aa - jj.mm.aa");
      textFieldDate.setAlignment(Pos.CENTER);

      // Creating the label
      Label labelHour = new Label("Entrez l'heure");

      // Creating the textfield
      TextField textFieldHour = new TextField("hh:mm");
      textFieldHour.setAlignment(Pos.CENTER);

      // Make an anonymous class with event handler for this button
      Button button = new Button("Créer le plan !");

      // Creating the anchorPane
      VBox vbox = new VBox();
      vbox.getChildren().add(labelDate);
      vbox.getChildren().add(textFieldDate);
      vbox.getChildren().add(labelHour);
      vbox.getChildren().add(textFieldHour);
      vbox.getChildren().add(button);
      vbox.setAlignment(Pos.CENTER);
      vbox.isVisible();

      // Show all
      Scene scene = new Scene(vbox);
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
   @Override
   public void initialize(URL url, ResourceBundle rb) {
   }

}
