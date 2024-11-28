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

public class GamesGridController {

    @FXML
    private VBox VBoxContainer;
    
    @FXML
    private ScrollPane scrollPane;

    private OkHttpClient client = new OkHttpClient();

    @FXML
    public void initialize() {
        try {
            // Llamada a la API
            String url = "https://api.rawg.io/api/games?dates=2024-01-01,2024-12-31&ordering=-added&key=8b5a6229e22946f4a639842b405b094b";
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Parsear el JSON con Gson
                Gson gson = new Gson();
                GameResponse gameResponse = gson.fromJson(response.body().charStream(), GameResponse.class);

                // Crear la grilla de juegos usando GameGridBuilder
                List<Game> games = gameResponse.getResults();
                VBoxContainer.getChildren().add(GameGridBuilder.buildGameGrid(games));

            } catch (IOException e) {
                e.printStackTrace();
            }
            
            scrollPane.setContent(VBoxContainer);
            scrollPane.setPannable(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
