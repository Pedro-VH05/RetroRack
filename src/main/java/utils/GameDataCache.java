package utils;

import java.util.List;
import models.Game;

public class GameDataCache {

    // Instancia única de la clase
    private static GameDataCache instance;

    // Datos en caché
    private List<Game> bestRatedGames;
    private List<Game> popularGames;
    private List<Game> newGames;
    private List<Game> b2001;
    private List<Game> classics;
    private boolean dataCached = false;

    // Constructor privado para evitar la creación de instancias
    private GameDataCache() {}

    // Método para obtener la instancia única
    public static GameDataCache getInstance() {
        if (instance == null) {
            instance = new GameDataCache();
        }
        return instance;
    }

    // Métodos para establecer los datos en caché
    public void setGamesData(List<Game> bestRatedGames, List<Game> popularGames, List<Game> newGames, List<Game> b2001) {
        this.bestRatedGames = bestRatedGames;
        this.popularGames = popularGames;
        this.newGames = newGames;
        this.b2001 = b2001;
        this.dataCached = true;
    }

    // Métodos para obtener los datos en caché
    public List<Game> getBestRatedGames() {
        return bestRatedGames;
    }

    public List<Game> getPopularGames() {
        return popularGames;
    }

    public List<Game> getNewGames() {
        return newGames;
    }
    
    public List<Game> getClassics() {
       return classics;
   }


    public List<Game> getB2001() {
        return b2001;
    }

    // Método para verificar si los datos están en caché
    public boolean isDataCached() {
        return dataCached;
    }

    // Método para limpiar la caché
    public void clearCache() {
        this.bestRatedGames = null;
        this.popularGames = null;
        this.newGames = null;
        this.b2001 = null;
        this.dataCached = false;
    }
}