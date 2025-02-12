package controllers;

import java.awt.event.MouseEvent;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import utils.GameFetchUtils;

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
	


	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setGameDetails(int gameId) {
		// Obtener los detalles del juego desde la API
		Game game = GameFetchUtils.getGameDetails(gameId);

		if (game != null) {
			// Actualizar la interfaz con los detalles del juego
			setGameInfo(game);
			loadScreenshots(gameId);
		} else {
			System.err.println("No se pudo obtener la información del juego.");
		}
	}

    @FXML
    void editPersonalReview() {
        editReview();
    }



	@FXML
	void goToDiscover() {
		goBack();
	}

		
	private void goBack() {
		// Cerrar la ventana de detalles
		Stage stage = (Stage) arrowBack.getScene().getWindow();
		stage.close();

		// Volver a mostrar la ventana principal
		if (primaryStage != null) {
			primaryStage.show();
		}
	}

	private void setGameInfo(Game game) {
		// Limpiar la descripción
		String cleanDescription = cleanDescriptionRaw(game.getDescription());

		// Actualizar los campos de la interfaz
		lblTitle.setText(game.getName());
		lblDescription.setText(cleanDescription);

		// Convertir la lista de géneros a una cadena separada por comas
		String generos = game.getGeneros().stream().map(genre -> genre.getName()).collect(Collectors.joining(", "));
		lblGeneros.setText(generos);

		// Convertir la lista de plataformas a una cadena separada por comas
		String plataformas = game.getPlatforms().stream().map(platform -> platform.getPlatform().getName())
				.collect(Collectors.joining(", "));
		lblPlataformas.setText(plataformas);

		// Mostrar el tiempo de juego en horas
		lblTiempoJuego.setText(game.getPlaytime() + " horas");
	}

	private void loadScreenshots(int gameId) {
		// Obtener las capturas de pantalla desde la API
		List<String> screenshotUrls = GameFetchUtils.getGameScreenshots(gameId);

		if (screenshotUrls != null && !screenshotUrls.isEmpty()) {
			// Limpiar el HBox antes de agregar nuevas imágenes
			screenshotsHBox.getChildren().clear();

			// Agregar cada imagen al HBox
			for (String url : screenshotUrls) {
				// Crear la ImageView y cargar la imagen
				ImageView imageView = new ImageView(new Image(url));
				imageView.setFitHeight(IMAGE_HEIGHT); // Altura fija
				imageView.setPreserveRatio(true); // Mantener la proporción

				// Obtener el ancho real de la imagen después de escalarla
				double imageWidth = imageView.getBoundsInLocal().getWidth();

				// Crear el rectángulo de recorte con las dimensiones correctas
				Rectangle clip = new Rectangle(imageWidth, IMAGE_HEIGHT); // Ancho dinámico, altura fija
				clip.setArcWidth(20); // Bordes redondeados
				clip.setArcHeight(20); // Bordes redondeados
				imageView.setClip(clip);

				// Agregar la imagen al HBox
				screenshotsHBox.getChildren().add(imageView);
			}
		} else {
			System.out.println("No se encontraron capturas de pantalla.");
		}
	}

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
	
	private void editReview() {
		if (reviewArea.isDisabled()) {
            // Habilitar el TextArea y el Slider
            reviewArea.setDisable(false);
            noteSlider.setDisable(false);

            // Cambiar los botones
            editReviewBtn.setVisible(false);
            doneReviewBtn.setVisible(true);
        } else {
            // Deshabilitar el TextArea y el Slider
            reviewArea.setDisable(true);
            noteSlider.setDisable(true);

            // Cambiar los botones
            editReviewBtn.setVisible(true); 
            doneReviewBtn.setVisible(false);

            // Actualizar la nota en la Label
            int nota = (int) noteSlider.getValue();
            noteLabel.setText(String.valueOf(nota));
        }
	}

}