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
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Stage;

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


      /* The following lines are here to load and transcode SVG image to be able to display the image maps*/

      // ImageView where the converted SVG image will be
      ImageView imgView = new ImageView();
      // Transcoder used to convert the svg to an java FX image
      BufferedImageTranscoder trans = new BufferedImageTranscoder();
      WritableImage img = null;

      try {
         // Get InputStream from an SVG file
         InputStream svgInputStream = getClass().getResourceAsStream("/Daryll/etageK-plan-base.svg");
         TranscoderInput transcoderInput = new TranscoderInput(svgInputStream);
         try {

            // Set format of the transcoded image
            trans.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(1200));
            trans.addTranscodingHint(PNGTranscoder.KEY_HEIGHT,new Float(800));

            // Transcoding operation (it will produce a bufferedImage, stored in the BufferedImageTranscoder
            trans.transcode(transcoderInput, null);

            // Allow to retrieve a Java FX image from the bufferedImage)
            img = SwingFXUtils.toFXImage(trans.getBufferedImage(), null);

         } catch (TranscoderException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
         }
      }
      catch (Exception io) {
         Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, io);
      }

      /* END of transcoding */

      // Put the image inside an ImageView object
      imgView.setImage(img);

      // Select the Pane where the image will be displayed
      Pane pane = (Pane) scene.lookup("#planCheseaux");

      // imgView settings
      imgView.setPreserveRatio(true);
      imgView.fitWidthProperty().bind(pane.widthProperty());

      // Add the imageView
      pane.getChildren().add(imgView);

      /* Constraints for resizing the window.*/
      stage.setMinHeight(600);
      stage.setMinWidth(800);
      stage.maxHeightProperty().bind(scene.widthProperty().divide(4).multiply(3).add(2));
      stage.minHeightProperty().bind(scene.widthProperty().divide(4).multiply(3));

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
