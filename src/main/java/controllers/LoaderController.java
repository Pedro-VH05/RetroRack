package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoaderController implements Initializable {

    @FXML
    private ImageView loadingGif;

    @FXML
    private Label userNameLabel;

    private Stage stage;

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
        loadingGif.setImage(gifImage);

        // Crear un Timeline para retrasar la apertura de la nueva ventana
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(5), event -> openNewWindow())
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void openNewWindow() {
        // Crear la nueva escena o ventana
        try {
            // Suponiendo que tienes un archivo FXML para la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DiscoverMain.fxml"));
            BorderPane newWindowRoot = loader.load();
            Scene newScene = new Scene(newWindowRoot);

            // Configurar y mostrar la nueva ventana
            stage = (Stage) loadingGif.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
