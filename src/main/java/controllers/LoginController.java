package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class LoginController {

	@FXML
	private SVGPath arrowBackToLogin;

	@FXML
	private Button btnLogIn;

	@FXML
	private Button btnRegister;

	@FXML
	private Label btngoToRegister;

	@FXML
	private AnchorPane loginPane;

	@FXML
	private AnchorPane registerPane;

	@FXML
	void goToLogin(MouseEvent event) {
		animatePaneSwitch(registerPane, loginPane);
	}

	@FXML
	void goToRegister(MouseEvent event) {
		animatePaneSwitch(loginPane, registerPane);
	}

	// Metodo que añade una transición cuando se cambia entre el panel de login y el
	// de crear cuenta
	private void animatePaneSwitch(AnchorPane activePane, AnchorPane hidePane) {
		FadeTransition fadeOut = new FadeTransition(Duration.millis(300), activePane);
		// Fade Out
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setOnFinished(e -> {
			activePane.setVisible(false);
			hidePane.setVisible(true);

			// Fade in
			FadeTransition fadeIn = new FadeTransition(Duration.millis(300), hidePane);
			fadeIn.setFromValue(0.0);
			fadeIn.setToValue(1.0);
			fadeIn.play();
		});

		fadeOut.play();
	}
}
