package controllers;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Game;
import utils.GameFetchUtils;

public class DiscoverController {

	@FXML
	private Pane closeAppBtn;

	@FXML
	private Pane minimizeAppBtn;

	@FXML
	private DiscoverGamesGridController discoverGamesGridController;

	@FXML
	private TextField searchBar;

	private Stage stage;

	private GameFetchUtils gameFetcher = new GameFetchUtils();

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
		String query = searchBar.getText().trim();
		if (!query.isEmpty()) {
			try {
				List<Game> searchResults = gameFetcher.searchGames(query);
				if (discoverGamesGridController != null) {
					discoverGamesGridController.showSearchResults(searchResults);
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
	 * @param bestRatedGames - Llamada a una api
	 * @param popularGames - Llamada a una api
	 * @param newGames - Llamada a una api
	 * @param b2001 - Llamada a una api
	 */
	public void setGamesData(List<Game> bestRatedGames, List<Game> popularGames, List<Game> newGames,
			List<Game> b2001) {
		if (discoverGamesGridController != null) {
			discoverGamesGridController.setGamesData(bestRatedGames, popularGames, newGames, b2001);
		}
	}
}
