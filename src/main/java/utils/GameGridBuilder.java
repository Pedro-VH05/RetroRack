package utils;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import models.Game;

import java.util.List;

public class GameGridBuilder {

	private static final int GAMES_PER_ROW = 3;
	private static final double IMAGE_WIDTH = 560;
	private static final double IMAGE_HEIGHT = 325;

	/**
	 * Construye un VBox para mostrar una lista de juegos.
	 *
	 * @param games Lista de juegos
	 * @return VBox con los elementos dispuestos
	 */
	public static VBox buildGameGrid(List<Game> games) {
		VBox vBoxContainer = new VBox();
		HBox currentHBox = new HBox(60);
		int count = 0;

		for (Game game : games) {
			// Crear el BorderPane para cada juego
			BorderPane gamePane = new BorderPane();

			// Crear el ImageView y configurar las dimensiones
			ImageView imageView = new ImageView(new Image(game.getBackgroundImage()));
			imageView.setFitWidth(IMAGE_WIDTH);
			imageView.setFitHeight(IMAGE_HEIGHT);

			// Clip para bordes redondeados
			Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
			clip.setArcWidth(20);
			clip.setArcHeight(20);
			imageView.setClip(clip);

			// Agregar el ImageView al BorderPane
			gamePane.setCenter(imageView);

			// Añadir el BorderPane al HBox
			currentHBox.getChildren().add(gamePane);
			count++;

			// Cuando completes una fila o llegues al final de los juegos
			if (count % GAMES_PER_ROW == 0 || count == games.size()) {

				VBox.setMargin(currentHBox, new Insets(0, 0, 200, 0)); // Margen estándar

				// Añadir el HBox actual al VBox
				vBoxContainer.getChildren().add(currentHBox);

				// Crear un nuevo HBox para la siguiente fila
				currentHBox = new HBox(60);
			}
		}

		return vBoxContainer;
	}
}
