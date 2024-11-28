package controllers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DescubrirController {

	@FXML
	private Pane closeAppBtn;

	@FXML
	private Pane minimizeAppBtn;

	@FXML
	private void initialize() {
		// Esperar que el stage se cargue completamente
		Platform.runLater(() -> {
			Stage stage = (Stage) closeAppBtn.getScene().getWindow();

			// Detectar cuando el stage se minimiza
			stage.iconifiedProperty().addListener((observable, wasMinimized, isNowMinimized) -> {
				if (!isNowMinimized) {
					// Si la ventana ya no está minimizada, restauramos las propiedades visuales
					restaurarVentana(stage);
				}
			});
		});
	}

	public static void restaurarVentana(Stage stage) {
		Node root = stage.getScene().getRoot();
		// Restaurar la opacidad y las propiedades visuales
		root.setScaleX(1.0);
		root.setScaleY(1.0);
		root.setTranslateX(0.0);
		root.setTranslateY(0.0);
		root.setOpacity(1.0);

		// Restaurar la opacidad del Stage (asegúrate de que el Stage no esté en blanco)
		stage.setOpacity(1.0);

	}

	@FXML
	private void closeApp(MouseEvent event) {
		Platform.exit();
	}

	@FXML
	private void minimizeApp(MouseEvent event) {
		// Obtener el Stage
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}

}
