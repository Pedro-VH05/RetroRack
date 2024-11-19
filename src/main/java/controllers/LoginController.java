package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

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

}
