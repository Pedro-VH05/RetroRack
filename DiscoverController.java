package controllers;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Game;
import utils.GameDataCache;
import utils.GameFetchUtils;

public class DiscoverController {

    @FXML
    private Pane closeAppBtn;

    @FXML
    private Pane minimizeAppBtn;

    @FXML
    private DiscoverGamesGridController discoverGamesGridController;

    @FXML
    private TextField searchBar;

    @FXML
    private BorderPane paneBiblioteca;

    @FXML
    private Label lblBiblioteca;

    @FXML
    private Label lblDescubrir;

    private Stage stage;

    private GameFetchUtils gameFetcher = new GameFetchUtils();
    private GameDataCache gameDataCache = GameDataCache.getInstance();

    @FXML
    private void initialize() {
        if (closeAppBtn != null && closeAppBtn.getScene() != null) {
            stage = (Stage) closeAppBtn.getScene().getWindow();

            stage.iconifiedProperty().addListener((observable, wasMinimized, isNowMinimized) -> {
                if (!isNowMinimized) {
                    restaurarVentana(stage);
                }
            });
        }

        // Agregar manejador de eventos al hacer clic en "Biblioteca"
        if (paneBiblioteca != null) {
            paneBiblioteca.setOnMouseClicked(event -> {
                try {
                    // Cargar el archivo Library.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Library.fxml"));
                    Parent root = loader.load();

                    // Obtener la escena actual y cambiarla
                    Stage stage = (Stage) paneBiblioteca.getScene().getWindow();
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/views/DiscoverMainStyles.css").toExternalForm());
                    stage.setScene(scene);

                    // Cambiar los estilos
                    lblBiblioteca.setStyle("-fx-text-fill: #F29441; -fx-effect: dropshadow(gaussian, rgba(248, 201, 160, 0.5), 10, 0.3, 0, 0);");
                    lblDescubrir.setStyle("-fx-text-fill: white; -fx-effect: none;");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // Cargar datos de juegos desde el caché si están disponibles
        if (gameDataCache.isDataCached()) {
            setGamesData(
                    gameDataCache.getBestRatedGames(),
                    gameDataCache.getPopularGames(),
                    gameDataCache.getNewGames(),
                    gameDataCache.getB2001()
            );
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
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void searchGame() {
        String query = searchBar.getText().trim();
        if (!query.isEmpty()) {
            try {
                List<Game> searchResults = gameFetcher.searchGames(query);
                if (discoverGamesGridController != null) {
                    discoverGamesGridController.showSearchResults(searchResults);
                }
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Error al realizar la búsqueda");
            }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setGamesData(List<Game> bestRatedGames, List<Game> popularGames, List<Game> newGames, List<Game> b2001) {
        if (discoverGamesGridController != null) {
            discoverGamesGridController.setGamesData(bestRatedGames, popularGames, newGames, b2001);
        }
    }
}
