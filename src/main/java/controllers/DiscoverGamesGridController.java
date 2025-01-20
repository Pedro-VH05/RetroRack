package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Game;
import utils.GameFetchUtils;
import utils.GameGridBuilder;
import java.util.List;

public class DiscoverGamesGridController {

	@FXML
	private ScrollPane mainScrollPane;

	@FXML
	private VBox VBoxContainer;
	
	private static final String API_K = "&key=d119a39f3ac64031a6ab6bb78b067da6";

	public void initialize() {
		GameFetchUtils fetcher = new GameFetchUtils();
		try {
			// Llamadas a la API para tres categorías de juegos 
			List<Game> bestRatedGames = fetcher.fetchGames(
					"https://api.rawg.io/api/games?ordering=-rating&page_size=15" + API_K);
			List<Game> popularGames = fetcher.fetchGames(
					"https://api.rawg.io/api/games?ordering=-added&page_size=15" + API_K);
			List<Game> newGames = fetcher.fetchGames(
					"https://api.rawg.io/api/games?dates=2024-10-01,2025-12-31&ordering=-released&page_size=15" + API_K);
			List<Game> b2001 = fetcher.fetchGames(
					"https://api.rawg.io/api/games?dates=2001-01-01,2001-12-31&ordering=-rating" + API_K);
					
			
			// Añadimos las secciones
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Populares", popularGames));
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Nuevos", newGames));
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejor Valorados", bestRatedGames));
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejores Valorados 2001", b2001));
			
			// Configuración del scroll principal
			mainScrollPane.setFitToWidth(true);
			mainScrollPane.setPannable(true);
			mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
			mainScrollPane.getStyleClass().add("mainScrollPane");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
