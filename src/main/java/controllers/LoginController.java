package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import utils.BBDDConnector;

public class LoginController {

   @FXML
   private SVGPath arrowBackToLogin;

   @FXML
   private Button btnLogIn;

   @FXML
   private Button btnRegister;

   @FXML
   private Label btngoToRegister;

   @FXML
   private AnchorPane loginPane;

   @FXML
   private AnchorPane registerPane;

   @FXML
   private TextField fechaNacimiento;

   @FXML
   private TextField nombreUsuario;

   @FXML
   private PasswordField password;

   @FXML
   private TextField email;

   @FXML
   private TextField nombreCompleto;

   @FXML
   void goToLogin(MouseEvent event) {
      animatePaneSwitch(registerPane, loginPane);
   }

   @FXML
   void goToRegister(MouseEvent event) {
      animatePaneSwitch(loginPane, registerPane);
   }

   @FXML
   void addUser(MouseEvent event) {
      String nombreUsuario = ((TextField) registerPane.lookup("#nombreUsuario")).getText();
      String password = ((PasswordField) registerPane.lookup("#password")).getText();
      String email = ((TextField) registerPane.lookup("#email")).getText();
      String nombreCompleto = ((TextField) registerPane.lookup("#nombreCompleto")).getText();
      String fechaNacimiento = ((TextField) registerPane.lookup("#fechaNacimiento")).getText();

      if (nombreUsuario.isEmpty() || password.isEmpty() || email.isEmpty() || 
            nombreCompleto.isEmpty() || fechaNacimiento.isEmpty()) {
            mostrarMensajeError("Todos los campos son obligatorios.");
            return;
        }

      boolean success = registerUser(nombreUsuario, password, email, nombreCompleto,
            fechaNacimiento);

      if (success) {
         mostrarMensajeExito("Usuario registrado correctamente");
         goToLogin(event);
      } else {
         mostrarMensajeError("Error al registrar el usuario");
      }
   }

   private void animatePaneSwitch(AnchorPane activePane, AnchorPane hidePane) {
      FadeTransition fadeOut = new FadeTransition(Duration.millis(300), activePane);
      fadeOut.setFromValue(1.0);
      fadeOut.setToValue(0.0);
      fadeOut.setOnFinished(e -> {
         activePane.setVisible(false);
         hidePane.setVisible(true);

         FadeTransition fadeIn = new FadeTransition(Duration.millis(300), hidePane);
         fadeIn.setFromValue(0.0);
         fadeIn.setToValue(1.0);
         fadeIn.play();
      });

      fadeOut.play();
   }

   private boolean registerUser(String nombreUsuario, String password, String email, String nombreCompleto,
         String fechaNacimiento) {
      String getMaxIdQuery = "SELECT MAX(id) AS max_id FROM Usuario";
      String insertUsuarioQuery = "INSERT INTO Usuario (id, nombre_usuario, password, email, "
            + "nombre_completo, fecha_nacimiento, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";

      try (BBDDConnector connector = new BBDDConnector(); Connection con = connector.conectar()) {
         int nuevoId = 1;
         try (PreparedStatement maxIdStmt = con.prepareStatement(getMaxIdQuery)) {
            ResultSet rs = maxIdStmt.executeQuery();
            if (rs.next()) {
               int id = rs.getInt("max_id");
               nuevoId = id + 1;
            }
         }

         try (PreparedStatement insertStmt = con.prepareStatement(insertUsuarioQuery)) {
            insertStmt.setString(1, String.valueOf(nuevoId)); // ID
            insertStmt.setString(2, nombreUsuario); // Nombre de usuario
            insertStmt.setString(3, password); // Contraseña
            insertStmt.setString(4, email); // Email
            insertStmt.setString(5, nombreCompleto); // Nombre completo

            insertStmt.setDate(6, java.sql.Date.valueOf(fechaNacimiento)); // Fecha de nacimiento
            insertStmt.setDate(7, java.sql.Date.valueOf(LocalDate.now())); // Fecha de registro

            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0;
         }

      } catch (SQLException e) {
         e.printStackTrace();
         return false;
      }
   }
   
   private void mostrarMensajeError(String mensaje) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.setTitle("Error");
      alerta.setContentText(mensaje);
      alerta.showAndWait();
  }

  private void mostrarMensajeExito(String mensaje) {
      Alert alerta = new Alert(Alert.AlertType.INFORMATION);
      alerta.setTitle("Éxito");
      alerta.setContentText(mensaje);
      alerta.showAndWait();
  }

}
