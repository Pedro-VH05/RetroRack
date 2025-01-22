package controllers;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Game;

public class DiscoverController {

    @FXML
    private Pane closeAppBtn;

    @FXML
    private Pane minimizeAppBtn;

    @FXML
    private DiscoverGamesGridController discoverGamesGridController; // Controlador del archivo incluido

    private Stage stage;

    @FXML
    private void initialize() {
        // Verificamos que closeAppBtn tiene una escena asignada antes de obtener el Stage
        if (closeAppBtn != null && closeAppBtn.getScene() != null) {
            stage = (Stage) closeAppBtn.getScene().getWindow();

            // Detectamos cuando el stage se minimiza
            stage.iconifiedProperty().addListener((observable, wasMinimized, isNowMinimized) -> {
                if (!isNowMinimized) {
                    // Si la ventana ya no está minimizada, restauramos las propiedades visuales
                    restaurarVentana(stage);
                }
            });
        }
    }

    public static void restaurarVentana(Stage stage) {
        if (stage != null) {
            Node root = stage.getScene().getRoot();
            root.setScaleX(1.0);
            root.setScaleY(1.0);
            root.setTranslateX(0.0);
            root.setTranslateY(0.0);
            root.setOpacity(1.0);
            
            stage.setOpacity(1.0);
        }
    }

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void minimizeApp(MouseEvent event) {
        // Obtener el Stage
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    // Método para pasar los datos al controlador de DiscoverGamesGrid
    public void setGamesData(List<Game> bestRatedGames, List<Game> popularGames, List<Game> newGames, List<Game> b2001) {
        if (discoverGamesGridController != null) {
            discoverGamesGridController.setGamesData(bestRatedGames, popularGames, newGames, b2001);
        }
    }
}