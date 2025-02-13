package utils;

import javafx.scene.control.Alert;

public class MensajesErrorUtil {

   /**
    * 
    * @param mensaje
    */
   public void mostrarMensajeError(String mensaje) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.getDialogPane().getStylesheets().add(getClass().getResource("../views/Alert.css").toExternalForm());
      alerta.setTitle("Error");
      alerta.setContentText(mensaje);
      alerta.setHeaderText(null);
      alerta.showAndWait();
   }

   /**
    * 
    * @param mensaje
    */
   public void mostrarMensajeExito(String mensaje) {
      Alert alerta = new Alert(Alert.AlertType.INFORMATION);
      alerta.getDialogPane().getStylesheets().add(getClass().getResource("../views/Alert.css").toExternalForm());
      alerta.setTitle("Éxito");
      alerta.setHeaderText(null);
      alerta.setContentText(mensaje);
      alerta.showAndWait();
   }
}
