package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import models.Game;
import models.Platform;
import models.PlatformWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameGridBuilder {

	private static final double IMAGE_WIDTH = 530;
	private static final double IMAGE_HEIGHT = 300;

	/**
	 * Crea la sección de juegos con un título y un ScrollPane horizontal.
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

			// Crear el ImageView para la imagen del juego
			ImageView imageView = new ImageView(new Image(game.getBackgroundImage()));
			imageView.setId("game-background-image");
			imageView.setFitWidth(IMAGE_WIDTH);
			imageView.setFitHeight(IMAGE_HEIGHT);
			imageView.setSmooth(true);
			imageView.setCache(true);
			imageView.preserveRatioProperty();

			// Clip para bordes redondeados
			Rectangle clip = new Rectangle(IMAGE_WIDTH, IMAGE_HEIGHT);
			clip.setArcWidth(20);
			clip.setArcHeight(20);
			imageView.setClip(clip);

			// Crear el SVGPath para el icono "Añadir"
			SVGPath addGameIcon = new SVGPath();
			addGameIcon.setContent(
					"M25 2.36c-5.8 0-11.6 2.21-16.02 6.62A22.68 22.68 0 0 0 25 47.66 22.68 22.68 0 0 0 41.02 8.99 22.58 22.58 0 0 0 25 2.35Zm0 2.33c5.2 0 10.4 1.99 14.38 5.93a20.42 20.42 0 0 1 0 28.76 20.3 20.3 0 0 1-28.72 0 20.38 20.38 0 0 1-.04-28.76A20.39 20.39 0 0 1 25 4.7Zm0 11.72c-.66 0-1.17.5-1.17 1.17v6.25h-6.25c-.67 0-1.17.5-1.17 1.17 0 .66.5 1.17 1.17 1.17h6.25v6.25c0 .67.5 1.17 1.17 1.17.66 0 1.17-.5 1.17-1.17v-6.25h6.25c.67 0 1.17-.5 1.17-1.17 0-.66-.5-1.17-1.17-1.17h-6.25v-6.25c0-.67-.5-1.17-1.17-1.17Z");
			addGameIcon.setFill(new Color(1.0, 1.0, 1.0, 1.0));
			addGameIcon.getStyleClass().add("add-icon");
			addGameIcon.setVisible(false);

			// Contenedor StackPane para superponer imagen y SVG
			StackPane imageContainer = new StackPane();
			imageContainer.getChildren().addAll(imageView, addGameIcon);
			StackPane.setAlignment(addGameIcon, Pos.BOTTOM_RIGHT);

			// Agregar margen al icono para separarlo del borde
			StackPane.setMargin(addGameIcon, new Insets(0, 25, 25, 0));

			// Manejo de eventos para bajar opacidad y mostrar el icono
			imageContainer.setOnMouseEntered(event -> {
				imageView.setOpacity(0.5);
				addGameIcon.setVisible(true);
			});

			imageContainer.setOnMouseExited(event -> {
				imageView.setOpacity(1.0);
				addGameIcon.setVisible(false);
			});

			// Crear un VBox para el título y los iconos de las plataformas
			VBox titleAndIconsContainer = new VBox(5);
			titleAndIconsContainer.setAlignment(Pos.CENTER_LEFT);

			// Crear el Label con el título del juego
			Label gameNameLabel = new Label(game.getName().toUpperCase());
			gameNameLabel.getStyleClass().add("titulo-juego");

			// Añadir el Label al VBox
			titleAndIconsContainer.getChildren().add(gameNameLabel);

			// Crear un HBox para los iconos de las plataformas
			HBox platformIconsContainer = new HBox(0);
			platformIconsContainer.setAlignment(Pos.CENTER_LEFT);

			Set<String> addedPlatforms = new HashSet<>();

			for (PlatformWrapper platformWrapper : game.getPlatforms()) {
				Platform platform = platformWrapper.getPlatform();
				String platformGroup = IconUtil.getPlatformGroup(platform.getName());

				if (!addedPlatforms.contains(platformGroup)) {
					ImageView platformIcon = IconUtil.getPlatformIcon(platform);
					if (platformIcon != null) {
						platformIconsContainer.getChildren().add(platformIcon);
						addedPlatforms.add(platformGroup);
					}
				}
			}

			titleAndIconsContainer.getChildren().add(platformIconsContainer);

			// Crear un contenedor principal para la imagen y el título
			VBox gameContainer = new VBox(0);
			gameContainer.setAlignment(Pos.CENTER);
			gameContainer.getChildren().addAll(imageContainer, titleAndIconsContainer);

			// Añadir el VBox al BorderPane
			gamePane.setCenter(gameContainer);

			// Añadir el BorderPane al HBox de juegos
			gamesRow.getChildren().add(gamePane);
		}

		// Crear el ScrollPane para esta fila
		ScrollPane scrollPane = new ScrollPane(gamesRow);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPannable(true);
		scrollPane.getStyleClass().add("rowScrollPane");

		// Añadir el título y el ScrollPane a la sección
		sectionContainer.getChildren().addAll(sectionLabel, scrollPane);

		return sectionContainer;
	}

	/**
	 * Filtra los juegos con URL de imagen válida.
	 *
	 * @param games Lista de juegos
	 * @return Lista de juegos con imágenes válidas
	 */
	private static List<Game> validateImgURL(List<Game> games) {
		return games.stream().filter(game -> game.getBackgroundImage() != null && !game.getBackgroundImage().isEmpty())
				.collect(Collectors.toList());
	}
}
