import GUI.controllers.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import utils.ConfigLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * This file contains the base of the application Daryll, this is where the
 *        application begin and launch his GUI.
 */
public class Daryll extends Application {

   private static final Logger LOG = Logger.getLogger(ConfigLoader.class.getName());
   /**
    * Method that loads all the JavaFX ressources to run the programm.
    * @param stage the javafx stage
    * @throws IOException If an I/O error occurred
    */

   @Override
   public void start(Stage stage) throws IOException {

      FXMLLoader fxmlLoader = new FXMLLoader();
      BorderPane root = fxmlLoader.load(getClass().getResource("MainView.fxml"));

      MainViewController mainViewController = fxmlLoader.getController();
      Scene scene = new Scene(root);
 
      stage.setTitle("Daryll");
      stage.setScene(scene);

      /* Constraints for resizing the window.*/
      stage.setMinHeight(768);
      stage.setMinWidth(1024);
      stage.maxHeightProperty().bind(scene.widthProperty().divide(16).multiply(10).add(2));
      stage.minHeightProperty().bind(scene.widthProperty().divide(16).multiply(10));

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
      imgView2.fitWidthProperty().bind(pane2.widthProperty().subtract(150));

      LocalDateTime currentDateTime = LocalDateTime.now();
      DatePicker datePicker = (DatePicker) scene.lookup("#dateField");
      datePicker.valueProperty().setValue(currentDateTime.toLocalDate());

      stage.show();
   }

   /**
    * The programm begins here.
    * @param args the command line arguments
    */
   public static void main(String[] args) {

      if(args.length == 1){
         if(args[0].equals("--localhost") || args[0].equals("-l")){
            ConfigLoader.setServerAddress("localhost");
         } else if(args[0].equals("--help") || args[0].equals("-h")){
            // TODO smthing
         }
      }
      launch(args);
   }
}
