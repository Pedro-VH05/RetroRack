package utils;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import models.Game;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase donde se crea el grid de juegos que se va a mostrar por pantalla
 */
public class GameGridBuilder {

	private static final double IMAGE_WIDTH = 560;
	private static final double IMAGE_HEIGHT = 300;

	/**
	 * Crea una sección de juegos con un título y un ScrollPane horizontal.
	 *
	 * @param title Título de la sección
	 * @param games Lista de juegos
	 * @return VBox con la sección
	 */
	public static VBox createGameSection(String title, List<Game> games) {
		VBox sectionContainer = new VBox();

		// Crear un título para la sección
		Label sectionLabel = new Label(title);
		sectionContainer.getStyleClass().add("categoryLabel");

		// Crear el HBox que contendrá los juegos
		HBox gamesRow = new HBox(60);
		gamesRow.getStyleClass().add("custom-hbox");

		List<Game> validGames = validateImgURL(games);

		// Agregar los juegos válidos al HBox
		for (Game game : validGames) {
			BorderPane gamePane = new BorderPane();

			// Crear el ImageView y configurar las dimensiones
			ImageView imageView = new ImageView(new Image(game.getBackgroundImage()));

			// Establecer el tamaño del ImageView para que todas las imágenes tengan el
			// mismo tamaño
			imageView.setFitWidth(IMAGE_WIDTH);
			imageView.setFitHeight(IMAGE_HEIGHT);
			imageView.setSmooth(true);
			imageView.setCache(true);

			// Clip para bordes redondeados
			Rectangle clip = new Rectangle(IMAGE_WIDTH, IMAGE_HEIGHT);
			clip.setArcWidth(20);
			clip.setArcHeight(20);
			imageView.setClip(clip);

			// Agregar el ImageView al BorderPane
			gamePane.setCenter(imageView);
			// Añadir el BorderPane al HBox
			gamesRow.getChildren().add(gamePane);

		}

		// Crear el ScrollPane para esta fila
		ScrollPane scrollPane = new ScrollPane(gamesRow);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setPannable(true);
		// CSS
		scrollPane.getStyleClass().add("rowScrollPane");

		// Añadir el título y el ScrollPane a la sección
		sectionContainer.getChildren().addAll(sectionLabel, scrollPane);

		return sectionContainer;
	}

	private static List<Game> validateImgURL(List<Game> games) {
		// Solo se muestran los juegos que tienen URL válida
		return games.stream().filter(game -> game.getBackgroundImage() != null && !game.getBackgroundImage().isEmpty())
				.collect(Collectors.toList());
	}

}
