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

import Daryll.SVGTools.BufferedImageTranscoder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Stage;

import Daryll.Plan.*;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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


      // ImageView where the converted SVG image will be
      ImageView imgView = new ImageView();

      PlanLoader planLoader = new PlanLoader();
      planLoader.createPlanFromSVGFile("/Daryll/etageK-plan-base.svg");


      // Select the Pane where the image will be displayed
      Pane pane = (Pane) scene.lookup("#planCheseaux");
      Image img = planLoader.getTranscodedImage(1400,800);


      // Put the image inside an ImageView object
      imgView.setImage(img);

      // imgView settings
      imgView.setPreserveRatio(true);
      imgView.fitWidthProperty().bind(pane.widthProperty());

      // Add the imageView
      pane.getChildren().add(imgView);

      
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
