package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Game;
import utils.GameGridBuilder;

import java.util.List;

public class DiscoverGamesGridController {

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
}