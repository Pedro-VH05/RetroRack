package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

   @Override
   public void start(Stage primaryStage) {
      try {
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(MainApp.class.getResource("/views/LoginWindow.fxml"));

         // Check if the image resource is found
         String imagePath = "/images/retroRack_logo.png";
         if (MainApp.class.getResource(imagePath) != null) {
            primaryStage.getIcons().add(new Image(imagePath));
         } else {
            System.out.println("Image not found: " + imagePath);
         }

         Pane ventana = (Pane) loader.load();
         primaryStage.initStyle(StageStyle.UNDECORATED);

         Scene scene = new Scene(ventana);
         scene.getStylesheets().add(getClass().getResource("/views/LoginStyles.css").toExternalForm());
         primaryStage.setScene(scene);
         primaryStage.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
