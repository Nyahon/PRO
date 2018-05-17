package Daryll; /**
 * Module      : PRO
 * File        : Daryll.Daryll.java
 * Date of Creation        : 31.03.2018
 *
 * Goal : This file contains the base of the application Daryll.Daryll, this is where the
 *        application begin and launch his GUI.
 *
 * 
 * Remarks : -
 *
 * @author Rashiti Labinot
 * @version 1.0
 *
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class Daryll extends Application {

   /**
    * @brief Method that loads all the JavaFX ressources to run the programm.
    * @param stage
    * @throws Exception 
    */

   @Override
   public void start(Stage stage) throws Exception {

      BorderPane root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
      Scene scene = new Scene(root);
 
      stage.setTitle("Daryll");
      stage.setScene(scene);

      //stage.initStyle(StageStyle.);

      /* Constraints for resizing the window.*/
      stage.setMinHeight(768);
      stage.setMinWidth(1024);
      stage.maxHeightProperty().bind(scene.widthProperty().divide(16).multiply(9).add(2));
      stage.minHeightProperty().bind(scene.widthProperty().divide(16).multiply(9));

      BorderPane pane1 = (BorderPane) scene.lookup("#planCheseaux");
      BorderPane pane2 = (BorderPane) scene.lookup("#planStRoch");

      pane1.minWidthProperty().bind(scene.widthProperty().divide(2));

      pane1.maxWidthProperty().bind(scene.widthProperty());
      pane1.minHeightProperty().bind(scene.heightProperty().divide(2));

      pane1.maxHeightProperty().bind(scene.heightProperty());


      // Fix default size
      ImageView imgView1 = (ImageView) scene.lookup("#imageCheseaux");
      ImageView imgView2 = (ImageView) scene.lookup("#imageStRoch");

      // Fix default settings for imageViews
      imgView1.setPreserveRatio(true);
      imgView1.fitWidthProperty().bind(pane1.widthProperty().subtract(60));

      imgView2.setPreserveRatio(true);
      imgView2.fitWidthProperty().bind(pane2.widthProperty().subtract(100));

      LocalDate currentDate = LocalDate.now();
      DatePicker datePicker = (DatePicker) scene.lookup("#dateField");
      datePicker.valueProperty().setValue(currentDate);

      Spinner hourSpinner = (Spinner) scene.lookup("#hourSpinner");
      SpinnerValueFactory svfHour = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24);
      hourSpinner.setValueFactory(svfHour);

      Spinner minuteSpinner = (Spinner) scene.lookup("#minuteSpinner");
      SpinnerValueFactory svfMinute = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,60);
      minuteSpinner.setValueFactory(svfMinute);


      stage.show();

   }


   /**
    * @brief The programm begins here.
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      launch(args);

   }   
   
}
