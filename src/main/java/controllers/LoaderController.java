package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoaderController implements Initializable {

	@FXML
	private ImageView loader;

	@FXML
	private Label userNameLabel;

	public void setUserName(String userName) {
		if (userNameLabel != null) {
			userNameLabel.setText("Bienvenido " + userName + "!");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Configuración inicial del GIF
		String gifPath = "/images/loading.gif";
		Image gifImage = new Image(getClass().getResourceAsStream(gifPath));
		loader.setImage(gifImage);
	}
}
