package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Game;
import utils.GameGridBuilder;

import java.util.ArrayList;
import java.util.List;

public class DiscoverGamesGridController {

    private static final int GROUP_SIZE = 5;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox VBoxContainer;

    private List<Game> allGames = new ArrayList<>();
    private List<Game> originalGamesState = new ArrayList<>();
    private List<Game> currentDisplayedGames = new ArrayList<>();

    private List<Game> originalPopularGames = new ArrayList<>();
    private List<Game> originalNewGames = new ArrayList<>();
    private List<Game> originalRecommendedGames = new ArrayList<>();
    private List<Game> originalClassics = new ArrayList<>();

    private List<Game> preFilterState = new ArrayList<>();

    /**
     * Agrega los juegos a la página principal
     *
     * @param popularGames     - Lista de los juegos más populares
     * @param newGames         - Lista de juegos que van a salir
     * @param recommendedGames - Lista de juegos recomendados
     * @param classics         - Lista de juegos clásicos
     */
    public void setGamesData(List<Game> popularGames, List<Game> newGames, List<Game> recommendedGames,
                             List<Game> classics) {
        allGames.clear();
        allGames.addAll(popularGames);
        allGames.addAll(newGames);
        allGames.addAll(recommendedGames);
        allGames.addAll(classics);

        // Guardar el estado original de cada categoría
        originalPopularGames.clear();
        originalPopularGames.addAll(popularGames);

        originalNewGames.clear();
        originalNewGames.addAll(newGames);

        originalRecommendedGames.clear();
        originalRecommendedGames.addAll(recommendedGames);

        originalClassics.clear();
        originalClassics.addAll(classics);

        // Guardar el estado original de todos los juegos
        originalGamesState.clear();
        originalGamesState.addAll(allGames);

        // Mostrar todos los juegos inicialmente
        currentDisplayedGames.clear();
        currentDisplayedGames.addAll(allGames);

        // Set the data for each category
        setData(popularGames, newGames, recommendedGames, classics);
    }

    private void setData(List<Game> popularGames, List<Game> newGames, List<Game> recommendedGames,
                         List<Game> classics) {
        // Limpiamos el contenedor principal
        VBoxContainer.getChildren().clear();

        if (!popularGames.isEmpty()) {
            VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Populares", popularGames));
        }

        if (!newGames.isEmpty()) {
            VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Juegos Nuevos", newGames));
        }

        if (!recommendedGames.isEmpty()) {
            VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Recomendados", recommendedGames));
        }

        if (!classics.isEmpty()) {
            VBoxContainer.getChildren().add(GameGridBuilder.createGameSection("Clásicos", classics));
        }

        // Configuración del scroll
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setPannable(true);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScrollPane.getStyleClass().add("mainScrollPane");
    }

    public void showSearchResults(List<Game> searchResults) {
        // Guardar el estado actual antes de aplicar la búsqueda
        preFilterState.clear();
        preFilterState.addAll(currentDisplayedGames);

        // Mostrar los resultados de la búsqueda
        currentDisplayedGames.clear();
        currentDisplayedGames.addAll(searchResults);

        showSearchResult(searchResults);
    }

    public void showFilteredResults(List<Game> filteredGames) {
        // Guardar el estado actual antes de aplicar los filtros
        preFilterState.clear();
        preFilterState.addAll(currentDisplayedGames);

        // Mostrar los resultados filtrados
        currentDisplayedGames.clear();
        currentDisplayedGames.addAll(filteredGames);

        // Limpiar resultados anteriores y mostrar los juegos filtrados
        VBoxContainer.getChildren().clear();
        if (!filteredGames.isEmpty()) {
            for (int i = 0; i < filteredGames.size(); i += GROUP_SIZE) {
                List<Game> group = filteredGames.subList(i, Math.min(i + GROUP_SIZE, filteredGames.size()));
                VBox groupBox = new VBox();
                groupBox.getChildren().add(GameGridBuilder.createGameSection("Filtered Games", group));
                VBoxContainer.getChildren().add(groupBox);
            }
        } else {
            Label noResultsLabel = new Label("No games match the selected filters.");
            VBoxContainer.getChildren().add(noResultsLabel);
        }
    }

    public List<Game> getAllGames() {
        return currentDisplayedGames;
    }

    public void restoreOriginalState() {
        // Restaurar el estado anterior a la búsqueda o filtrado
        currentDisplayedGames.clear();
        currentDisplayedGames.addAll(preFilterState);
        setData(originalPopularGames, originalNewGames, originalRecommendedGames, originalClassics);
    }

    private void showSearchResult(List<Game> searchResults) {
        // Limpiamos los resultados anteriores
        VBoxContainer.getChildren().clear();
        if (!searchResults.isEmpty()) {
            // Dividir los resultados en grupos de 5
            for (int i = 0; i < searchResults.size(); i += GROUP_SIZE) {
                List<Game> group = searchResults.subList(i, Math.min(i + GROUP_SIZE, searchResults.size()));
                VBox groupBox = new VBox();
                groupBox.getChildren().add(GameGridBuilder.createGameSection("", group));
                VBoxContainer.getChildren().add(groupBox);
            }
        } else {
            Label noResultsLabel = new Label("No se encontraron juegos.");
            VBoxContainer.getChildren().add(noResultsLabel);
        }
    }
}