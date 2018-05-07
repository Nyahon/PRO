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
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Stage;

import Daryll.Plan.*;

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

      /* Constraints for resizing the window.*/
      stage.setMinHeight(600);
      stage.setMinWidth(800);
      stage.maxHeightProperty().bind(scene.widthProperty().divide(4).multiply(3).add(2));
      stage.minHeightProperty().bind(scene.widthProperty().divide(4).multiply(3));


      PlanLoader planLoader = new PlanLoader();
      planLoader.createPlanFromSVGFile("/Daryll/plans/Cheseaux/etageK-plan-base.svg");

      int width = 1400;
      int height = 800;

      // Select the Pane where the image will be displayed
      ImageView imgView1 = (ImageView) scene.lookup("#imageCheseaux");
      Image img1 = planLoader.getTranscodedImage(width,height);


      // Put the image inside an ImageView object
      imgView1.setImage(img1);


      Pane pane = (Pane) scene.lookup("#planCheseaux");

      // imgView settings
      imgView1.setPreserveRatio(true);
      imgView1.fitWidthProperty().bind(pane.widthProperty());

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
