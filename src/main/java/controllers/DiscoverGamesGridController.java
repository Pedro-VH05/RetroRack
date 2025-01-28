package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Game;
import utils.GameGridBuilder;

import java.util.List;

public class DiscoverGamesGridController {

	private static final int GROUP_SIZE = 5;

	@FXML
	private ScrollPane mainScrollPane;

	@FXML
	private VBox VBoxContainer;

	/**
	 * Agrega los juegos a la página principal
	 * 
	 * @param bestRatedGames - Lista de los mejores juegos
	 * @param popularGames   - Lista de los juegos mas populares
	 * @param newGames       - Lista de juegos que van a salir
	 * @param b2001          - Mejores juegos de 2001
	 */
	public void setGamesData(List<Game> bestRatedGames, List<Game> popularGames, List<Game> newGames,
			List<Game> b2001) {
		VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Populares", popularGames));
		VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Nuevos", newGames));
		VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejor Valorados", bestRatedGames));
		VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Mejores Valorados 2001", b2001));

		// Configuración del scroll
		mainScrollPane.setFitToWidth(true);
		mainScrollPane.setPannable(true);
		mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		mainScrollPane.getStyleClass().add("mainScrollPane");
	}

	public void showSearchResults(List<Game> searchResults) {
		// Limpiamos los resultados anteriores
		VBoxContainer.getChildren().clear();
		if (!searchResults.isEmpty()) {
			// Dividir los resultados en grupos de 5
			for (int i = 0; i < searchResults.size(); i += GROUP_SIZE) {
				// Creamos otra listo con los próximos 5 juegos o los restantes
				List<Game> group = searchResults.subList(i, Math.min(i + GROUP_SIZE, searchResults.size()));
				// Creamos un nuevo VBox para el grupo
				VBox groupBox = new VBox();
				groupBox.getChildren().add(GameGridBuilder.createGameSection("", group));

				// Añadimos el VBox del grupo al contenedor principal
				VBoxContainer.getChildren().add(groupBox);
			}
		} else {
			// Mostramos un mensaje si no hay resultados
			Label noResultsLabel = new Label("No se encontraron juegos.");
			VBoxContainer.getChildren().add(noResultsLabel);
		}
	}

}