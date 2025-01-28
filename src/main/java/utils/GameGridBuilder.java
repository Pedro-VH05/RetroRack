package utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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

			// Crear un VBox para el título y los iconos de las plataformas
			VBox titleAndIconsContainer = new VBox(5); // Espaciado entre título e iconos
			titleAndIconsContainer.setAlignment(Pos.CENTER_LEFT);

			// Crear el Label con el título del juego
			Label gameNameLabel = new Label(game.getName().toUpperCase());
			gameNameLabel.getStyleClass().add("titulo-juego");

			// Añadir el Label al VBox
			titleAndIconsContainer.getChildren().add(gameNameLabel);

			// Crear un HBox para los iconos de las plataformas
			HBox platformIconsContainer = new HBox(0); 
			platformIconsContainer.setAlignment(Pos.CENTER_LEFT);

			// Usar un Set para rastrear plataformas únicas
			Set<String> addedPlatforms = new HashSet<>();

			// Recorrer las plataformas del juego y agregar los iconos únicos
			for (PlatformWrapper platformWrapper : game.getPlatforms()) {
			    Platform platform = platformWrapper.getPlatform();

			    // Obtener el nombre principal de la plataforma (por ejemplo, "PlayStation" en lugar de "PlayStation 5")
			    String platformGroup = IconUtil.getPlatformGroup(platform.getName());

			    // Verificar si ya hemos añadido una plataforma de este grupo
			    if (!addedPlatforms.contains(platformGroup)) {
			        // Obtener el icono de la plataforma usando la clase IconUtil
			        ImageView platformIcon = IconUtil.getPlatformIcon(platform);

			        // Asegurar que no sea nulo antes de añadir
			        if (platformIcon != null) {
			            platformIconsContainer.getChildren().add(platformIcon);
			            addedPlatforms.add(platformGroup);
			        }
			    }
			}


			// Añadir el HBox de iconos al VBox principal
			titleAndIconsContainer.getChildren().add(platformIconsContainer);

			// Crear un contenedor principal para la imagen y el título
			VBox gameContainer = new VBox(0);
			gameContainer.setAlignment(Pos.CENTER);
			gameContainer.getChildren().addAll(imageView, titleAndIconsContainer);

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
