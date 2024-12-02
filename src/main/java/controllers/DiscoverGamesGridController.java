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
import utils.GameGridBuilder;
import java.io.IOException;
import java.util.List;

public class DiscoverGamesGridController {

	@FXML
	private ScrollPane mainScrollPane;

	@FXML
	private VBox VBoxContainer;

	private OkHttpClient client = new OkHttpClient();

	public void initialize() {
		try {
//			// Llamadas a la API para tres categorías de juegos
//			List<Game> bestRatedGames = fetchGames(
//					"https://api.rawg.io/api/games?ordering=-rating&key=8b5a6229e22946f4a639842b405b094b");
//			List<Game> popularGames = fetchGames(
//					"https://api.rawg.io/api/games?ordering=-added&key=8b5a6229e22946f4a639842b405b094b");
//			List<Game> newGames = fetchGames(
//					"https://api.rawg.io/api/games?dates=2024-10-01,2025-12-31&ordering=-released&key=8b5a6229e22946f4a639842b405b094b");
			List<Game> ps5 = fetchGames(
					"https://api.rawg.io/api/games?platforms=187&key=8b5a6229e22946f4a639842b405b094b");

			// Añadimos las secciones
//			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejor Valorados", bestRatedGames));
//			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Populares", popularGames));
//			VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Nuevos", newGames));
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

	/**
	 * Realiza una solicitud HTTP para obtener una lista de juegos.
	 *
	 * @param url URL de la API
	 * @return Lista de juegos
	 * @throws IOException Si ocurre un error en la solicitud
	 */
	private List<Game> fetchGames(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}

			Gson gson = new Gson();
			GameResponse gameResponse = gson.fromJson(response.body().charStream(), GameResponse.class);
			return gameResponse.getResults();
		}
	}
}
