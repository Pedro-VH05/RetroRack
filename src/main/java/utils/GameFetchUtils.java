package utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

	public static Game getGameDetails(int gameId) {
	    try {
	        // Construir la URL de la API
	        String url = BASE_URL + "/" + gameId + "?key=" + API_KEY;

	        // Crear la solicitud HTTP
	        Request request = new Request.Builder().url(url).get().build();

	        // Ejecutar la solicitud
	        try (Response response = client.newCall(request).execute()) {
	            if (response.isSuccessful() && response.body() != null) {
	                // Convertir la respuesta JSON a un objeto Game
	            	Gson gson = new Gson();
	                return gson.fromJson(response.body().charStream(), Game.class);
	            } else {
	                throw new IOException("Error en la solicitud: " + response.code());
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Error al obtener detalles del juego: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
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
	
	/**
     * Obtiene las URLs de las capturas de pantalla de un juego.
     *
     * @param gameId ID del juego
     * @return Lista de URLs de las capturas de pantalla
     */
    public static List<String> getGameScreenshots(int gameId) {
        List<String> screenshotUrls = new ArrayList<>();
        String url = BASE_URL + "/" + gameId + "/screenshots?key=" + API_KEY;

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

                // Obtener el array de capturas de pantalla
                JsonArray screenshots = jsonObject.getAsJsonArray("results");
                for (int i = 0; i < screenshots.size(); i++) {
                    JsonObject screenshot = screenshots.get(i).getAsJsonObject();
                    String imageUrl = screenshot.get("image").getAsString();
                    screenshotUrls.add(imageUrl);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al obtener capturas de pantalla: " + e.getMessage());
            e.printStackTrace();
        }

        return screenshotUrls;
    }

}
