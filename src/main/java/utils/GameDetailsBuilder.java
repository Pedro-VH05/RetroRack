package utils;

import controllers.GameDetailsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameDetailsBuilder {

    public static void showGameInfo(Stage primaryStage, int gameId) {
        try {
            // Cargar la vista FXML
            FXMLLoader loader = new FXMLLoader(GameDetailsBuilder.class.getResource("/views/GameDetails.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtener el controlador y pasar la referencia a la ventana principal
            GameDetailsController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            // Pasar el ID del juego al controlador
            controller.setGameDetails(gameId);

            // Crear y mostrar la ventana
            Stage stage = new Stage();
            stage.setTitle("Detalles del juego");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al cargar la vista FXML: " + e.getMessage());
            System.exit(1);
        }
    }
}