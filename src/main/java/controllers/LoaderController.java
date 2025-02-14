package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
	    Task<Void> loadGamesTask = new Task<>() {
	        @Override
	        protected Void call() throws Exception {

	            List<Game> popularGames = gameFetchUtils.fetchGames("https://api.rawg.io/api/games?ordering=-added&page_size=12" + API_K);

	            List<Game> newGames = gameFetchUtils.fetchGames("https://api.rawg.io/api/games?dates=2024-10-01,2025-12-31&ordering=-released&page_size=12" + API_K);

	            List<Game> recommendedGames = gameFetchUtils.fetchGames("https://api.rawg.io/api/games?key=" + API_K + "&ordering=-rating&page_size=12");
	            
	            List<Game> clasics = gameFetchUtils.fetchGames("https://api.rawg.io/api/games?key=" + API_K + "&dates=1990-01-01,2010-12-31&page_size=12");

	            // Pasar los datos al controlador
	            Platform.runLater(() -> {
	                openDiscoverGamesGrid(popularGames, newGames, recommendedGames, clasics);
	            });

	            return null;
	        }
	    };

	    // Iniciar la tarea en un hilo
	    Thread thread = new Thread(loadGamesTask);
	    thread.setDaemon(true);
	    thread.start();
	}

   private void openDiscoverGamesGrid(List<Game> popularGames, List<Game> newGames, List<Game> recommendedGames, List<Game> clasics) {
      try {
         // Cargamos la nueva ventana
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DiscoverMain.fxml"));
         Pane newWindowRoot = loader.load();

         DiscoverController discoverController = loader.getController();

         discoverController.setLoaderGamesData(popularGames, newGames, recommendedGames, clasics);

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