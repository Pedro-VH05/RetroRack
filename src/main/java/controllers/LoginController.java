package controllers;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginController {

	@FXML
	private Button btnLogin;

	@FXML
	void hoverEnter(MouseEvent event) {
		btnLogin.getStyleClass().add("btnLogin-hover");
	}

	@FXML
	void hoverExit(MouseEvent event) {
		btnLogin.getStyleClass().remove("btnLogin-hover");
	}

	@FXML
	void goToCrearCuenta(MouseEvent event) {
		// Obtener el Stage de la ventana actual
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			try {
				// Cargar el archivo FXML para la nueva ventana
				Parent root = FXMLLoader.load(getClass().getResource("/views/CrearCuenta.fxml"));

				// Crear la nueva escena
				Scene scene = new Scene(root);

				// Añadir el archivo CSS a la nueva escena
				scene.getStylesheets().add(getClass().getResource("/views/login_styles.css").toExternalForm());

				// Crear el nuevo Stage
				Stage newStage = new Stage(StageStyle.UNDECORATED);
				newStage.getIcons().add(new Image("/images/retroRack_logo.png"));
				newStage.setScene(scene);
				newStage.setTitle("Crear Cuenta");
				newStage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
	}

}
