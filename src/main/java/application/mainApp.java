package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class mainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(mainApp.class.getResource("/views/Login.fxml"));

			Pane ventana = (Pane) loader.load();

			// Hacer la ventana sin bordes
			primaryStage.initStyle(StageStyle.UNDECORATED);

			Scene scene = new Scene(ventana);

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
