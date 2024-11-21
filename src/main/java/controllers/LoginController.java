package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

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
    	loginPane.setVisible(true);
        registerPane.setVisible(false);
    }

    @FXML
    void goToRegister(MouseEvent event) {
    	registerPane.setVisible(true);
        loginPane.setVisible(false);
    }      

}
