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

         loader.setLocation(MainApp.class.getResource("/views/VentanaLogin.fxml"));
         primaryStage.getIcons().add(new Image("/images/retroRack_logo.png"));

         Pane ventana = (Pane) loader.load();
         // Hacer la ventana sin bordes  
         primaryStage.initStyle(StageStyle.UNDECORATED);

         Scene scene = new Scene(ventana);
         scene.getStylesheets().add(getClass().getResource("/views/DiscoverMainStyles.css").toExternalForm());
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