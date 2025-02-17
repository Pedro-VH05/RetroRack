package controllers;

import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import utils.BBDDUtils;
import utils.GameFetchUtils;
import utils.GameGridBuilder;

public class GameDetailsController {

   private static final double IMAGE_HEIGHT = 250;

   @FXML
   private BorderPane arrowBack;

   @FXML
   private Label lblTitle;

   @FXML
   private TextArea lblDescription;

   @FXML
   private Label lblGeneros;

   @FXML
   private Label lblPlataformas;

   @FXML
   private Label lblTiempoJuego;

   @FXML
   private Label lblMetacriticScore;

   @FXML
   private Label lblComunityScore;

   @FXML
   private HBox screenshotsHBox;

   @FXML
   private ScrollPane screenshotsScrollPane;

   @FXML
   private Button editReviewBtn;

   @FXML
   private Button doneReviewBtn;

   @FXML
   private TextArea reviewArea;

   @FXML
   private Slider noteSlider;

   @FXML
   private Label noteLabel;

   private Stage primaryStage;

   @FXML
   private Button addGameButton;

   @FXML
   private VBox libraryContainer; // Contenedor de la biblioteca

   private Game selectedGame;
   private BBDDUtils dbUtils = new BBDDUtils();
   public void setGameDetails(Game game) {
      this.selectedGame = game;
   }

   /**
    * Establece el Stage principal para poder volver a la ventana anterior.
    *
    * @param primaryStage El Stage principal de la aplicación.
    */
   public void setPrimaryStage(Stage primaryStage) {
      this.primaryStage = primaryStage;
      // Establecer el ícono de la aplicación
      primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/retroRack_logo.png")));
   }

   /**
    * Carga y muestra los detalles de un juego específico.
    *
    * @param gameId ID del juego del que los detalles se van a cargar.
    */
   public void setGameDetails(int gameId) {
      // Obtenemos los detalles del juego desde la API
      Game game = GameFetchUtils.getGameDetails(gameId);

      if (game != null) {
         // Actualizamos la interfaz con los detalles del juego
         setGameInfo(game);
         loadScreenshots(gameId);
      } else {
         System.err.println("No se pudo obtener la información del juego.");
      }
   }

   /**
    * Habilita la edición de la reseña personal del usuario.
    */
   @FXML
   void editPersonalReview() {
      editReview();
   }

   /**
    * Navega de vuelta a la pantalla de descubrimiento de juegos.
    */
   @FXML
   void goToDiscover() {
      goBack();
   }
   
   @FXML
   void addGameToLibrary() {
      addGame();
   }

   /**
    * Cierra la ventana actual y vuelve a la ventana principal.
    */
   private void goBack() {
      // Cerramos la ventana de detalles
      Stage stage = (Stage) arrowBack.getScene().getWindow();
      stage.close();

      // Volvemos a mostrar la ventana principal
      if (primaryStage != null) {
         primaryStage.show();
      }
   }

   /**
    * Actualiza la interfaz con la información del juego.
    *
    * @param game El juego del que los detalles se van a mostrar.
    */
   private void setGameInfo(Game game) {
      // Limpiamos la descripción para eliminar etiquetas HTML
      String cleanDescription = cleanDescriptionRaw(game.getDescription());

      // Actualizamos los campos de la interfaz
      lblTitle.setText(game.getName());
      lblDescription.setText(cleanDescription);

      // Convertimos la lista de géneros a una cadena separada por comas
      String generos = game.getGenres().stream().map(genre -> genre.getName()).collect(Collectors.joining(", "));
      lblGeneros.setText(generos);

      // Convertimos la lista de plataformas a una cadena separada por comas
      String plataformas = game.getPlatforms().stream().map(platform -> platform.getPlatform().getName())
            .collect(Collectors.joining(", "));
      lblPlataformas.setText(plataformas);

      // Mostramos el tiempo de juego en horas
      lblTiempoJuego.setText(game.getPlaytime() + " horas");

      // Mostramos la puntuación de Metacritic
      if (game.getMetacritic() != null) {
         lblMetacriticScore.setText("" + game.getMetacritic());
      } else {
         lblMetacriticScore.setText("-");
      }

      // Mostramos la puntuación de la comunidad (sobre 100 y truncando el decimal)
      if (game.getRating() != -1) {
         double communityRating = game.getRating() * 20; // Convertir a escala de 100
         int truncatedRating = (int) communityRating; // Truncar el decimal
         lblComunityScore.setText("" + truncatedRating);
      } else {
         lblComunityScore.setText("-");
      }
   }

   /**
    * Carga y muestra las capturas de pantalla del juego.
    *
    * @param gameId ID del juego cuyas capturas de pantalla se van a cargar.
    */
   private void loadScreenshots(int gameId) {
      // Obtenemos las capturas de pantalla desde la API
      List<String> screenshotUrls = GameFetchUtils.getGameScreenshots(gameId);

      if (screenshotUrls != null && !screenshotUrls.isEmpty()) {
         // Limpiamos el HBox antes de agregar nuevas imágenes
         screenshotsHBox.getChildren().clear();

         // Agregamos cada imagen al HBox
         for (String url : screenshotUrls) {
            // Creamos la ImageView y cargamos la imagen
            ImageView imageView = new ImageView(new Image(url));
            imageView.setFitHeight(IMAGE_HEIGHT); // Altura fija
            imageView.setPreserveRatio(true); // Mantenemos la proporción

            // Obtenemos el ancho real de la imagen después de escalarla
            double imageWidth = imageView.getBoundsInLocal().getWidth();

            // Creamos el rectángulo de recorte con las dimensiones correctas
            Rectangle clip = new Rectangle(imageWidth, IMAGE_HEIGHT); // Ancho dinámico, altura fija
            clip.setArcWidth(20); // Bordes redondeados
            clip.setArcHeight(20); // Bordes redondeados
            imageView.setClip(clip);

            // Agregamos la imagen al HBox
            screenshotsHBox.getChildren().add(imageView);
         }
      } else {
         System.out.println("No se encontraron capturas de pantalla.");
      }
   }

   /**
    * Limpia la descripción del juego eliminando etiquetas HTML y contenido
    * innecesario.
    *
    * @param descriptionRaw La descripción cruda del juego.
    * @return La descripción limpia y lista para mostrar.
    */
   private static String cleanDescriptionRaw(String descriptionRaw) {
      // Si no hay descripción, devolvemos una cadena vacía
      if (descriptionRaw == null || descriptionRaw.isEmpty()) {
         return "";
      }

      // Dividimos el texto en partes
      String[] parts = descriptionRaw.split("\n\n");

      // Tomamos la primera parte
      String englishDescription = parts.length > 0 ? parts[0] : descriptionRaw;

      // Eliminamos saltos de línea y espacios adicionales
      englishDescription = englishDescription.replace("\n", " ").trim();

      return englishDescription;
   }

   /**
    * Habilita o deshabilita la edición de la reseña personal del usuario.
    */
   private void editReview() {
      if (reviewArea.isDisabled()) {
         // Habilitamos el TextArea y el Slider
         reviewArea.setDisable(false);
         noteSlider.setDisable(false);

         // Cambiamos los botones
         editReviewBtn.setVisible(false);
         doneReviewBtn.setVisible(true);
      } else {
         // Deshabilitamos el TextArea y el Slider
         reviewArea.setDisable(true);
         noteSlider.setDisable(true);

         // Cambiamos los botones
         editReviewBtn.setVisible(true);
         doneReviewBtn.setVisible(false);

         // Actualizamos la nota en la Label
         int nota = (int) noteSlider.getValue();
         noteLabel.setText(String.valueOf(nota));
      }
   }

   private void addGame() {
       if (selectedGame != null && !dbUtils.isGameInDatabase(selectedGame.getId())) {
           dbUtils.insertGameIntoDatabase(selectedGame);
           refreshLibrary(); // Actualiza la biblioteca
           System.out.println("Juego añadido correctamente");
       }
   }

   private void refreshLibrary() {
       libraryContainer.getChildren().clear();
       List<Game> games = dbUtils.getAllGamesFromDatabase();
       if (!games.isEmpty()) {
           libraryContainer.getChildren().add(GameGridBuilder.createGameSection("Mi Biblioteca", games));
       }
   }
}