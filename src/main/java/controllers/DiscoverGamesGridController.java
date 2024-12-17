package controllers;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Game;
import models.GameResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.GameFetchUtils;
import utils.GameGridBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscoverGamesGridController {

	@FXML
	private ScrollPane mainScrollPane;

	@FXML
	private VBox VBoxContainer;

	private OkHttpClient client = new OkHttpClient();

	public void initialize() {
		GameFetchUtils fetcher = new GameFetchUtils();
		try {
			// Llamadas a la API para tres categorías de juegos 
			List<Game> bestRatedGames = fetcher.fetchGames(
					"https://api.rawg.io/api/games?ordering=-rating&page_size=15&key=8b5a6229e22946f4a639842b405b094b");
			List<Game> popularGames = fetcher.fetchGames(
					"https://api.rawg.io/api/games?ordering=-added&page_size=15&key=8b5a6229e22946f4a639842b405b094b");
			List<Game> newGames = fetcher.fetchGames(
					"https://api.rawg.io/api/games?dates=2024-10-01,2025-12-31&ordering=-released&page_size=15&key=8b5a6229e22946f4a639842b405b094b");
			List<Game> ps5 = fetcher.fetchGames(
					"https://api.rawg.io/api/games?platforms=187&page_size=15&key=8b5a6229e22946f4a639842b405b094b");
					
			
			// Añadimos las secciones
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejor Valorados", bestRatedGames));
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Populares", popularGames));
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Nuevos", newGames));
			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejores Valorados 2001", ps5));
			
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
