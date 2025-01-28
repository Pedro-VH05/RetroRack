package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Game;
import utils.GameFetchUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoaderController implements Initializable {

    @FXML
    private ImageView loadingGif;

    @FXML
    private Label userNameLabel;

    private Stage stage;
    private GameFetchUtils gameFetchUtils = new GameFetchUtils();
    private static final String API_K = "&key=d119a39f3ac64031a6ab6bb78b067da6";

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

        // Iniciar la carga de los juegos en segundo plano
        loadAllGamesInBackground();
    }

    private void loadAllGamesInBackground() {
        // Creamos un Task para cargar los juegos en segundo plano
        Task<Void> loadGamesTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Realizamos las 4 llamadas a la API
                List<Game> bestRatedGames = gameFetchUtils.fetchGames(
                    "https://api.rawg.io/api/games?ordering=-rating&page_size=15" + API_K);
                List<Game> popularGames = gameFetchUtils.fetchGames(
                    "https://api.rawg.io/api/games?ordering=-added&page_size=15" + API_K);
                List<Game> newGames = gameFetchUtils.fetchGames(
                    "https://api.rawg.io/api/games?dates=2024-10-01,2025-12-31&ordering=-released&page_size=15" + API_K);
                List<Game> b2001 = gameFetchUtils.fetchGames(
                    "https://api.rawg.io/api/games?dates=2001-01-01,2001-12-31&ordering=-rating" + API_K);

                // Pasar los datos al DiscoverGamesGridController
                Platform.runLater(() -> {
                    openDiscoverGamesGrid(bestRatedGames, popularGames, newGames, b2001);
                });

                return null;
            }
        };

        // Si la tarea falla
        loadGamesTask.setOnFailed(event -> {
            System.out.println("Error al cargar los juegos: " + loadGamesTask.getException().getMessage());
            
        });

        // Iniciamos la tarea en un hilo
        Thread thread = new Thread(loadGamesTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void openDiscoverGamesGrid(List<Game> bestRatedGames, List<Game> popularGames, List<Game> newGames, List<Game> b2001) {
        try {
            // Cargamos la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DiscoverMain.fxml"));
            BorderPane newWindowRoot = loader.load();

            DiscoverController discoverController = loader.getController();

            discoverController.setGamesData(bestRatedGames, popularGames, newGames, b2001);

            Scene newScene = new Scene(newWindowRoot);
            newScene.getStylesheets().add(getClass().getResource("/views/DiscoverMainStyles.css").toExternalForm());
            stage = (Stage) loadingGif.getScene().getWindow();
            stage.getIcons().add(new Image("/images/retroRack_logo.png"));
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}