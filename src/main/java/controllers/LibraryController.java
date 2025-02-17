package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Game;
import utils.GameDataCache;
import utils.GameFetchUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibraryController {

   @FXML
   private BorderPane paneBiblioteca;

   @FXML
   private Label lblDescubrir;

   @FXML
   private BorderPane paneDescubrir;
   
   @FXML
   private VBox gamesContainer; // Contenedor para mostrar los juegos

   private Stage stage;
   private GameFetchUtils gameFetcher = new GameFetchUtils();
   private GameDataCache gameDataCache = GameDataCache.getInstance();

   @FXML
   private void initialize() {
      // Cargar los juegos de la base de datos
      List<Game> games = loadGamesFromDatabase();

      // Mostrar los juegos en la interfaz
      displayGames(games);
      // Agregar manejador de eventos al hacer clic en "Descubrir"
      paneBiblioteca.setOnMouseClicked(event -> {
         try {
            // Cargar el archivo DiscoverMain.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Library.fxml"));
            Parent root = loader.load();

            // Obtener la escena actual y cambiarla
            Stage stage = (Stage) paneBiblioteca.getScene().getWindow();
            stage.setScene(new Scene(root));

         } catch (IOException e) {
            e.printStackTrace();
         }
      });
   }

   @FXML
   private void goToDiscover(MouseEvent event) {
      try {
         // Cargar el archivo FXML
         URL fxmlResource = getClass().getResource("/views/DiscoverMain.fxml");
         if (fxmlResource == null) {
            System.err.println("Error: No se pudo encontrar el archivo FXML en la ruta especificada.");
         } else {
            FXMLLoader loader = new FXMLLoader(fxmlResource);
            Parent root = loader.load();

            // Obtener el controlador de DiscoverMain.fxml
            DiscoverController discoverController = loader.getController();

            // Si los datos de los juegos no están en caché, cargarlos desde la API
            if (!gameDataCache.isDataCached()) {
               List<Game> bestRatedGames = gameFetcher
                     .fetchGames(GameFetchUtils.BASE_URL + "?ordering=-rating&key=" + GameFetchUtils.API_KEY);
               List<Game> popularGames = gameFetcher
                     .fetchGames(GameFetchUtils.BASE_URL + "?ordering=-added&key=" + GameFetchUtils.API_KEY);
               List<Game> newGames = gameFetcher
                     .fetchGames(GameFetchUtils.BASE_URL + "?ordering=-released&key=" + GameFetchUtils.API_KEY);
               List<Game> b2001 = gameFetcher.fetchGames(GameFetchUtils.BASE_URL
                     + "?dates=2001-01-01,2001-12-31&ordering=-rating&key=" + GameFetchUtils.API_KEY);

               // Almacenar los datos en el caché
               gameDataCache.setGamesData(bestRatedGames, popularGames, newGames, b2001);
            }

            // Pasar los datos al controlador de DiscoverGamesGrid
            discoverController.setGamesData(gameDataCache.getBestRatedGames(), gameDataCache.getPopularGames(),
                  gameDataCache.getNewGames(), gameDataCache.getB2001());

            Scene scene = new Scene(root);

            // Cargar el archivo CSS
            URL cssResource = getClass().getResource("/views/DiscoverMainStyles.css");
            if (cssResource == null) {
               System.err.println("Error: No se pudo encontrar el archivo CSS en la ruta especificada.");
            } else {
               scene.getStylesheets().add(cssResource.toExternalForm());
            }

            Stage stage = (Stage) paneDescubrir.getScene().getWindow();
            stage.setScene(scene);
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public List<Game> loadGamesFromDatabase() {
      List<Game> games = new ArrayList<>();

      try (Connection conn = DriverManager.getConnection("jdbc:sqlite:retrorack.db")) {
          String sql = "SELECT * FROM Game";
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql);

          while (rs.next()) {
              Game game = new Game(
                  rs.getInt("id"),
                  rs.getString("slug"),
                  rs.getString("name"),
                  rs.getString("releaseDate"),
                  rs.getBoolean("tba"),
                  rs.getString("background_image"),
                  rs.getString("description_raw"),
                  rs.getDouble("rating"),
                  rs.getInt("ratingTop"),
                  null, // ratings (debes cargarlos desde otra tabla)
                  rs.getInt("ratingsCount"),
                  rs.getInt("reviewsCount"),
                  rs.getInt("added"),
                  rs.getInt("playtime"),
                  rs.getString("metacritic"),
                  rs.getString("esrbRating"),
                  null, // platforms (debes cargarlos desde otra tabla)
                  null, // genres (debes cargarlos desde otra tabla)
                  null, // shops (debes cargarlos desde otra tabla)
                  null, // tags (debes cargarlos desde otra tabla)
                  null  // screenshots (debes cargarlos desde otra tabla)
              );

              games.add(game);
          }
      } catch (Exception e) {
          System.err.println("Error al cargar los juegos desde la base de datos: " + e.getMessage());
          e.printStackTrace();
      }

      return games;
  }

  private void displayGames(List<Game> games) {
      // Limpiar el contenedor antes de agregar nuevos juegos
      gamesContainer.getChildren().clear();

      // Mostrar cada juego en la interfaz
      for (Game game : games) {
          HBox gameBox = new HBox();
          gameBox.getChildren().add(new Text(game.getName())); // Mostrar el nombre del juego
          gamesContainer.getChildren().add(gameBox);
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
      // Implementar la lógica de búsqueda si es necesario
   }
}
