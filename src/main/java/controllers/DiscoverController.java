package controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Game;
import models.Platform;
import models.PlatformWrapper;
import utils.GameFetchUtils;
import utils.TransitionUtil;

public class DiscoverController {

    @FXML
    private Pane closeAppBtn;

    @FXML
    private Pane minimizeAppBtn;

    @FXML
    private DiscoverGamesGridController discoverGamesGridController;

    @FXML
    private TextField searchBar;

    @FXML
    private BorderPane restore;

    @FXML
    private BorderPane filterBtn;

    @FXML
    private AnchorPane filterPane;

    @FXML
    private Label cancelBtn;

    @FXML
    private Label applyFilters;

    private Stage stage;

    private GameFetchUtils gameFetcher = new GameFetchUtils();

    private TransitionUtil transition = new TransitionUtil();

    @FXML
    private void initialize() {
        if (closeAppBtn != null && closeAppBtn.getScene() != null) {
            stage = (Stage) closeAppBtn.getScene().getWindow();

            stage.iconifiedProperty().addListener((observable, wasMinimized, isNowMinimized) -> {
                if (!isNowMinimized) {
                    restaurarVentana(stage);
                }
            });
        }
        // Habilitar el botón de restaurar cuando se aplica un filtro o se realiza una búsqueda
        restore.setDisable(true);
    }

    @FXML
    void restoreGames() {
        if (discoverGamesGridController != null) {
            System.out.println("restaurando");
            discoverGamesGridController.restoreOriginalState();
            restore.setDisable(true); // Deshabilitar el botón de restaurar
        }
    }

    @FXML
    private void applyFilters(MouseEvent event) {
        System.out.println("applyFilters method called"); // Debug statement

        // Access the GridPane within the filterPane
        Node gridPaneNode = filterPane.getChildren().stream().filter(node -> node instanceof GridPane).findFirst()
                .orElse(null);

        if (gridPaneNode instanceof GridPane) {
            GridPane gridPane = (GridPane) gridPaneNode;

            // Collect the selected filters
            List<String> selectedFilters = gridPane.getChildren().stream().filter(node -> node instanceof CheckBox)
                    .map(node -> (CheckBox) node) // Convert to CheckBox
                    .filter(CheckBox::isSelected).map(CheckBox::getText).map(String::toLowerCase)
                    .collect(Collectors.toList());

            System.out.println("Filtros seleccionados: " + selectedFilters);

            // Apply the filters if any are selected
            if (!selectedFilters.isEmpty()) {
                filterGames(selectedFilters);
            }

            // Close the filter popup
            closePopup(event);
        } else {
            System.out.println("GridPane not found within filterPane.");
        }
    }

    private void filterGames(List<String> filters) {
        // Obtener todos los juegos
        List<Game> allGames = discoverGamesGridController.getAllGames();

        // Filtrar los juegos que coincidan con al menos uno de los filtros
        List<Game> filteredGames = allGames.stream().filter(game -> gameMatchesFilters(game, filters))
                .collect(Collectors.toList());

        System.out.println("Filtered Games Count: " + filteredGames.size());

        // Mostrar los resultados filtrados
        discoverGamesGridController.showFilteredResults(filteredGames);
        restore.setDisable(false);
    }

    /**
     * Verifica si un juego coincide con al menos uno de los filtros.
     *
     * @param game    El juego a verificar.
     * @param filters La lista de filtros (nombres de plataformas).
     * @return true si el juego coincide con al menos un filtro, false en caso contrario.
     */
    private boolean gameMatchesFilters(Game game, List<String> filters) {
        return game.getPlatforms().stream().map(PlatformWrapper::getPlatform).map(Platform::getName).map(String::trim)
                .map(String::toLowerCase).anyMatch(platformName -> filters.contains(platformName));
    }

    @FXML
    void openFilterPane() {
        transition.slideSwitch(null, filterPane, 0, 0, 200, true, 700);
    }

    public static void restaurarVentana(Stage stage) {
        if (stage != null) {
            Node root = stage.getScene().getRoot();
            root.setScaleX(1.0);
            root.setScaleY(1.0);
            root.setTranslateX(0.0);
            root.setTranslateY(0.0);
            root.setOpacity(1.0);

            stage.setOpacity(1.0);
        }
    }

    @FXML
    private void closeApp(MouseEvent event) {
        javafx.application.Platform.exit();
    }

    @FXML
    private void minimizeApp(MouseEvent event) {
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void closePopup(MouseEvent event) {
        // Get the node that triggered the event
        Node source = (Node) event.getSource();

        // Traverse up the parent hierarchy to find the AnchorPane
        Node parent = source.getParent();
        while (parent != null && !(parent instanceof AnchorPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof AnchorPane) {
            AnchorPane anchorPane = (AnchorPane) parent;
            transition.slideSwitch(null, anchorPane, 0, 0, 200, false, 700);
        }
    }

    @FXML
    private void searchGame() {
        String query = searchBar.getText().trim();
        if (!query.isEmpty()) {
            try {
                List<Game> searchResults = gameFetcher.searchGames(query);
                if (discoverGamesGridController != null) {
                    discoverGamesGridController.showSearchResults(searchResults);
                    restore.setDisable(false); // Habilitar el botón de restaurar
                }
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Error al realizar la búsqueda");
            }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Método para pasar los datos al controlador de DiscoverGamesGrid
     *
     * @param popularGames     - Llamada a una api
     * @param newGames         - Llamada a una api
     * @param recommendedGames - Llamada a una api
     * @param classics         - Llamada a una api
     */
    public void setLoaderGamesData(List<Game> popularGames, List<Game> newGames, List<Game> recommendedGames,
                                   List<Game> classics) {
        if (discoverGamesGridController != null) {
            discoverGamesGridController.setGamesData(popularGames, newGames, recommendedGames, classics);
        }
    }
}