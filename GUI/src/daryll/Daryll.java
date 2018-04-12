/**
 * Module      : PRO
 * File        : Daryll.java
 * Date        : 31.03.2018
 *
 * Goal : This file contains the base of the application Daryll. This is where the
 *        application begin and launch his GUI.
 *
 * 
 * Remarks : -
 *
 * @author Rashiti Labinot
 * @version 1.0
 */

package daryll;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Daryll extends Application {
   
   /**
    * @brief Method that loads all the JavaFX ressources to run the programm.
    * @param stage
    * @throws Exception 
    */
   @Override
   public void start(Stage stage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("View/View.fxml"));
      Scene scene = new Scene(root);
 
      stage.setTitle("Daryll");
      stage.setScene(scene);

      /* Tests contraintes de redimensionnement */
      stage.setMinHeight(600);
      stage.setMinWidth(800);
      //stage.minWidthProperty().bind(scene.heightProperty().multiply(4).divide(3));
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
