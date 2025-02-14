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

	/**
	 * Inicializa el controlador. Configura el stage y el listener para restaurar la
	 * ventana cuando se desminimiza.
	 */
	@FXML
	private void initialize() {
		if (searchBar != null && searchBar.getScene() != null) {
			stage = (Stage) searchBar.getScene().getWindow();

			// Listener para restaurar la ventana cuando se desminimiza
			stage.iconifiedProperty().addListener((observable, wasMinimized, isNowMinimized) -> {
				if (!isNowMinimized) {
					restaurarVentana(stage);
				}
			});
		}

		restore.setDisable(true);
	}

	/**
	 * Restaura los juegos a su estado original.
	 */
	@FXML
	private void restoreGames() {
		if (discoverGamesGridController != null) {
			System.out.println("Restaurando juegos");
			discoverGamesGridController.restoreOriginalState();
			restore.setDisable(true); // Deshabilitar el botón de restaurar
		}
	}

	/**
	 * Aplica los filtros seleccionados y filtra los juegos.
	 *
	 * @param event El evento de mouse que desencadenó la acción.
	 */
	@FXML
	private void applyFilters(MouseEvent event) {
		System.out.println("Aplicando filtros");

		// Accedemos al GridPane dentro del filterPane
		Node gridPaneNode = filterPane.getChildren().stream().filter(node -> node instanceof GridPane).findFirst()
				.orElse(null);

		if (gridPaneNode instanceof GridPane) {
			GridPane gridPane = (GridPane) gridPaneNode;

			// Recopilamos los filtros seleccionados
			List<String> selectedFilters = gridPane.getChildren().stream().filter(node -> node instanceof CheckBox)
					.map(node -> (CheckBox) node).filter(CheckBox::isSelected).map(CheckBox::getText)
					.map(String::toLowerCase).collect(Collectors.toList());

			System.out.println("Filtros seleccionados: " + selectedFilters);

			// Aplicamos los filtros si hay alguno seleccionado
			if (!selectedFilters.isEmpty()) {
				filterGames(selectedFilters);
			}

			closePopup(event);
		} else {
			System.out.println("GridPane no encontrado dentro del filterPane.");
		}
	}

	/**
	 * Filtra los juegos según los filtros seleccionados.
	 *
	 * @param filters La lista de filtros seleccionados.
	 */
	private void filterGames(List<String> filters) {
		// Obtenemos todos los juegos
		List<Game> allGames = discoverGamesGridController.getAllGames();

		// Filtramos los juegos que coincidan con al menos uno de los filtros
		List<Game> filteredGames = allGames.stream().filter(game -> gameMatchesFilters(game, filters))
				.collect(Collectors.toList());

		System.out.println("Cantidad de juegos filtrados: " + filteredGames.size());

		// Mostramos por pantalla los resultados filtrados
		discoverGamesGridController.showFilteredResults(filteredGames);
		restore.setDisable(false);
	}

	/**
	 * Verifica si un juego coincide con al menos uno de los filtros.
	 *
	 * @param game    El juego a verificar.
	 * @param filters La lista de filtros (nombres de plataformas).
	 * @return true si el juego coincide con al menos un filtro, false en caso
	 *         contrario.
	 */
	private boolean gameMatchesFilters(Game game, List<String> filters) {
		return game.getPlatforms().stream().map(PlatformWrapper::getPlatform).map(Platform::getName).map(String::trim)
				.map(String::toLowerCase).anyMatch(platformName -> filters.contains(platformName));
	}

	/**
	 * Abre el panel de filtros
	 */
	@FXML
	void openFilterPane() {
		transition.slideSwitch(null, filterPane, 0, 0, 200, true, 700);
	}

	/**
	 * Restaura la ventana a su estado original.
	 *
	 * @param stage El stage que se va a restaurar.
	 */
	private static void restaurarVentana(Stage stage) {
		if (stage != null) {
			System.out.println("Restaurando ventana");
			Node root = stage.getScene().getRoot();
			root.setScaleX(1.0);
			root.setScaleY(1.0);
			root.setTranslateX(0.0);
			root.setTranslateY(0.0);
			root.setOpacity(1.0);

			stage.setOpacity(1.0);
		}
	}

	/**
	 * Cierra la aplicación.
	 *
	 * @param event El evento de mouse que desencadenó la acción.
	 */
	@FXML
	private void closeApp(MouseEvent event) {
		Stage stage = getStageFromNode((Node) event.getSource());
		if (stage != null) {
			javafx.application.Platform.exit();
		}
	}

	/**
	 * Minimiza la aplicación.
	 *
	 * @param event El evento de mouse que desencadenó la acción.
	 */
	@FXML
	private void minimizeApp(MouseEvent event) {
		Stage stage = getStageFromNode((Node) event.getSource());
		if (stage != null) {
			stage.setIconified(true);
		} else {
			System.out.println("Stage es null");
		}
	}

	/**
	 * Cierra el popup de filtros
	 *
	 * @param event El evento de mouse que desencadena la acción.
	 */
	@FXML
	private void closePopup(MouseEvent event) {
		// Obtener el nodo que desencadenó el evento
		Node source = (Node) event.getSource();

		// Recorrer la jerarquía de padres para encontrar el AnchorPane
		Node parent = source.getParent();
		while (parent != null && !(parent instanceof AnchorPane)) {
			parent = parent.getParent();
		}

		if (parent instanceof AnchorPane) {
			AnchorPane anchorPane = (AnchorPane) parent;
			transition.slideSwitch(null, anchorPane, 0, 0, 200, false, 700);
		}
	}

	/**
	 * Realiza una búsqueda de juegos basada en el texto ingresado en la barra de
	 * búsqueda.
	 */
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

	/**
	 * Muestra una alerta de error con el mensaje especificado.
	 *
	 * @param message El mensaje de error a mostrar.
	 */
	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Pasa los datos de los juegos al controlador de DiscoverGamesGrid.
	 *
	 * @param popularGames     La lista de juegos populares.
	 * @param newGames         La lista de juegos nuevos.
	 * @param recommendedGames La lista de juegos recomendados.
	 * @param classics         La lista de juegos clásicos.
	 */
	public void setLoaderGamesData(List<Game> popularGames, List<Game> newGames, List<Game> recommendedGames,
			List<Game> classics) {
		if (discoverGamesGridController != null) {
			discoverGamesGridController.setGamesData(popularGames, newGames, recommendedGames, classics);
		}
	}

	/**
	 * Obtiene el Stage desde un nodo.
	 *
	 * @param node El nodo desde el cual obtener el Stage.
	 * @return El Stage asociado al nodo.
	 */
	private Stage getStageFromNode(Node node) {
		return (Stage) node.getScene().getWindow();
	}
}