package utils;

import com.google.gson.Gson;

import models.Game;
import models.GameResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase utilitaria para realizar solicitudes HTTP relacionadas con juegos.
 */
public class GameFetchUtils {

	private static final String API_KEY = "d119a39f3ac64031a6ab6bb78b067da6";
	private static final String BASE_URL = "https://api.rawg.io/api/games";

	private final Gson gson = new Gson();
	private static final OkHttpClient client = new OkHttpClient();

	/**
	 * Obtiene una lista de juegos desde una URL específica de la API.
	 *
	 * @param url URL de la API
	 * @return Lista de juegos
	 * @throws IOException Si ocurre un error en la solicitud
	 */
	public List<Game> fetchGames(String url) throws IOException {
		// Crear una solicitud HTTP
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			// Verificamos la respuesta
			if (!response.isSuccessful()) {
				throw new IOException("Error al obtener juegos: " + response);
			}

			Gson gson = new Gson();
			GameResponse gameResponse = gson.fromJson(response.body().charStream(), GameResponse.class);

			return gameResponse.getResults();
		}
	}

	/**
	 * Obtiene una lista de IDs desde una URL específica de la API.
	 *
	 * @param url     URL de la API que devuelve una lista de IDs
	 * @param idField Nombre del campo JSON que contiene los IDs
	 * @return Lista de IDs
	 * @throws IOException Si ocurre un error en la solicitud
	 */
	public static List<Integer> fetchGameIds(String url, String idField) throws IOException {
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Error al obtener IDs: " + response);
			}

			Gson gson = new Gson();
			// Parsear la respuesta en un mapa genérico
			var jsonResponse = gson.fromJson(response.body().charStream(), Map.class);
			@SuppressWarnings("unchecked")
			List<Double> idList = (List<Double>) jsonResponse.get(idField);

			// Convertir a enteros
			List<Integer> ids = new ArrayList<>();
			for (Double id : idList) {
				ids.add(id.intValue());
			}
			return ids;
		}
	}

	/**
	 * Obtiene los detalles de juegos a partir de una lista de IDs.
	 *
	 * @param gameIds Lista de IDs de juegos
	 * @return Lista de juegos
	 * @throws IOException Si ocurre un error en las solicitudes
	 */
	public static List<Game> fetchGamesFromIds(List<Integer> gameIds) throws IOException {
		List<Game> games = new ArrayList<>();

		for (Integer gameId : gameIds) {
			String url = "https://api.rawg.io/api/games/" + gameId + "?key=" + API_KEY;

			Request request = new Request.Builder().url(url).build();
			try (Response response = client.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					System.out.println("Fallo al obtener juego con ID: " + gameId);
					continue;
				}

				Gson gson = new Gson();
				Game game = gson.fromJson(response.body().charStream(), Game.class);
				games.add(game);
			}
		}

		return games;
	}

	/**
	 * Metodo para buscar juegos con una query
	 * 
	 * @param query
	 * @return
	 * @throws IOException
	 */
	public List<Game> searchGames(String query) throws IOException {
		String url = BASE_URL + "?search=" + query + "&key=" + API_KEY;

		Request request = new Request.Builder().url(url).get().build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				// Leer el cuerpo de la respuesta como String
				String responseBody = response.body().string();

				// Deserializar usando el modelo adecuado
				GameResponse gameResponse = gson.fromJson(responseBody, GameResponse.class);
				return gameResponse.getResults(); // Retornar la lista de juegos
			} else {
				throw new IOException("Error en la solicitud: " + response.code());
			}
		}
	}

}
