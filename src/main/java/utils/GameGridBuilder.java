package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import models.Platform;
import models.PlatformWrapper;

public class GameGridBuilder {

    private static final double IMAGE_WIDTH = 530;
    private static final double IMAGE_HEIGHT = 300;

    // Variables para rastrear el arrastre
    private static double mousePressedX;
    private static double mousePressedY;
    private static boolean isDragging = false;

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

            // Configurar el evento MOUSE_PRESSED
            gamePane.setOnMousePressed(event -> {
                mousePressedX = event.getSceneX();
                mousePressedY = event.getSceneY();
                isDragging = false; // Reiniciar el estado de arrastre
            });

            // Configurar el evento MOUSE_DRAGGED
            gamePane.setOnMouseDragged(event -> {
                double deltaX = Math.abs(event.getSceneX() - mousePressedX);
                double deltaY = Math.abs(event.getSceneY() - mousePressedY);

                // Si el ratón se ha movido lo suficiente, es un arrastre
                if (deltaX > 5 || deltaY > 5) {
                    isDragging = true;
                }
            });

            // Configurar el evento MOUSE_RELEASED
            gamePane.setOnMouseReleased(event -> {
                if (!isDragging) {
                    // Si no fue un arrastre, abrir los detalles del juego
                    Stage primaryStage = (Stage) gamePane.getScene().getWindow();
                    primaryStage.hide();
                    GameDetailsBuilder.showGameInfo(primaryStage, game.getId());
                }
            });

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

            // Contenedor StackPane para la imagen
            StackPane imageContainer = new StackPane();
            imageContainer.getChildren().add(imageView);

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