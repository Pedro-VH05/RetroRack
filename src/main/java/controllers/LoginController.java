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
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import utils.BBDDConnector;
import utils.EmailUtil;
import utils.PasswordUtil;

public class LoginController {
	
	// Queries
	private final static String GET_MAX_ID  = "SELECT MAX(id) AS max_id FROM Usuario";
	private final static String CHECK_EMAIL  = "SELECT COUNT(*) AS count FROM Usuario WHERE email = ?";
	private final static String INSERT_USER = "INSERT INTO Usuario (id, nombre_usuario, password, email, "
			+ "nombre_completo, fecha_nacimiento, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
	private DatePicker fechaNacimiento;

	@FXML
	private TextField nombreUsuario;

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField loginPasswordField;
	
	@FXML
	private TextField loginUsernameField;

	@FXML
	private TextField loginPasswordTextField;

	@FXML
	private TextField email;

	@FXML
	private TextField nombreCompleto;

	@FXML
	private StackPane closedEye;

	@FXML
	private StackPane openEye;

	@FXML
	void goToLogin(MouseEvent event) {
		animatePaneSwitch(registerPane, loginPane);
	}

	@FXML
	void goToRegister(MouseEvent event) {
		// Animar la transición entre panes (loginPane -> registerPane)
		animatePaneSwitch(loginPane, registerPane);
	}

	@FXML
	void showPassword(MouseEvent event) {
		// Copiar el texto del campo de contraseña al campo de texto
		loginPasswordTextField.setText(loginPasswordField.getText());

		// Ocultar campo de contraseña y mostrar el campo de texto
		loginPasswordField.setVisible(false);
		loginPasswordTextField.setVisible(true);

		openEye.setVisible(true);
		closedEye.setVisible(false);
		return;
	}

	@FXML
	void hidePassword(MouseEvent event) {
		// Copiar el texto del campo de texto al campo de contraseña
		loginPasswordField.setText(loginPasswordTextField.getText());

		// Ocultar campo de texto y mostrar el campo de contraseña
		loginPasswordTextField.setVisible(false);
		loginPasswordField.setVisible(true);

		openEye.setVisible(false);
		closedEye.setVisible(true);

		return;
	}
	

	@FXML
	private void addUser(MouseEvent event) {
		String nombreUsuario = ((TextField) registerPane.lookup("#nombreUsuario")).getText();
		String password = ((PasswordField) registerPane.lookup("#password")).getText();
		String email = ((TextField) registerPane.lookup("#email")).getText();
		String nombreCompleto = ((TextField) registerPane.lookup("#nombreCompleto")).getText();
		DatePicker datePicker = (DatePicker) registerPane.lookup("#fechaNacimiento");
		LocalDate fechaNacimiento = datePicker.getValue();

		if (nombreUsuario.isEmpty() || password.isEmpty() || email.isEmpty() || nombreCompleto.isEmpty()
				|| fechaNacimiento == null) {
			mostrarMensajeError("Todos los campos son obligatorios.");
			return;
		}

		boolean success = registerUser(nombreUsuario, password, email, nombreCompleto, fechaNacimiento);

		if (success) {
			mostrarMensajeExito("Usuario registrado correctamente");
			goToLogin(event);
			limpiarCampos();
		} else {
			mostrarMensajeError("Error al registrar el usuario");
		}
	}

	private void limpiarCampos() {
		// Limpiar campos de texto
		nombreUsuario.clear();
		email.clear();
		nombreCompleto.clear();
		password.clear();
		fechaNacimiento.setValue(null);
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
			LocalDate fechaNacimiento) {
		

		try (BBDDConnector connector = new BBDDConnector();
				Connection con = connector.conectar();
				PreparedStatement maxIdStmt = con.prepareStatement(GET_MAX_ID);
				PreparedStatement checkEmailStmt = con.prepareStatement(CHECK_EMAIL);
				PreparedStatement insertStmt = con.prepareStatement(INSERT_USER)) {

			con.setAutoCommit(false);

			// Verificar si el correo ya existe
			checkEmailStmt.setString(1, email);
			ResultSet rs = checkEmailStmt.executeQuery();
			if (rs.next() && rs.getInt("count") > 0) {
				mostrarMensajeError("El correo electrónico ya está registrado.");
				return false;
			}

			// Obtener el siguiente ID
			int nuevoId = 1;
			ResultSet rsMaxId = maxIdStmt.executeQuery();
			if (rsMaxId.next()) {
				nuevoId = rsMaxId.getInt("max_id") + 1;
			}

			String hashedPassword = PasswordUtil.hashPassword(password);
			// Insertar el usuario
			insertStmt.setInt(1, nuevoId);
			insertStmt.setString(2, nombreUsuario);
			insertStmt.setString(3, hashedPassword);
			insertStmt.setString(4, email);
			insertStmt.setString(5, nombreCompleto);
			insertStmt.setDate(6, Date.valueOf(fechaNacimiento));
			insertStmt.setDate(7, Date.valueOf(LocalDate.now()));

			if (insertStmt.executeUpdate() > 0) {
				// Enviar correo de bienvenida
				EmailUtil.enviarCorreoBienvenida(email, nombreUsuario);
				con.commit();
				return true;
			} else {
				con.rollback();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@FXML
	void logInUser(MouseEvent event) {
	    String userInput = ((TextField) loginPane.lookup("#loginUsernameField")).getText();
	    
	    // Obtenemos la contraseña del campo visible
	    String plainPassword;
	    if (loginPasswordField.isVisible()) {
	        plainPassword = loginPasswordField.getText();
	    } else {
	        plainPassword = loginPasswordTextField.getText();
	    }

	    if (userInput.isEmpty() || plainPassword.isEmpty()) {
	        mostrarMensajeError("El nombre de usuario/correo y la contraseña son obligatorios.");
	        return;
	    }

	    String query = "SELECT password FROM Usuario WHERE email = ? OR nombre_usuario = ?";

	    try (BBDDConnector connector = new BBDDConnector();
	         Connection con = connector.conectar();
	         PreparedStatement stmt = con.prepareStatement(query)) {

	        // Configurar los parámetros de la consulta
	        stmt.setString(1, userInput);
	        stmt.setString(2, userInput);

	        // Ejecutar la consulta
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            String hashedPassword = rs.getString("password");

	            // Verificar la contraseña
	            if (PasswordUtil.verifyPassword(plainPassword, hashedPassword)) {
	                mostrarMensajeExito("Iniciando Sesión.");
	                // Falta redirigir al usuario a la pantalla "home"
	               
	            } else {
	                mostrarMensajeError("La contraseña es incorrecta.");
	            }
	        } else {
	            mostrarMensajeError("El usuario o correo electrónico no existe.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        mostrarMensajeError("Ocurrió un error al intentar iniciar sesión.");
	    }
	}


	private void mostrarMensajeError(String mensaje) {
		Alert alerta = new Alert(Alert.AlertType.ERROR);
		alerta.getDialogPane().getStylesheets().add(getClass().getResource("../views/Alert.css").toExternalForm());
		alerta.setTitle("Error");
		alerta.setContentText(mensaje);
		alerta.setHeaderText(null);
		alerta.showAndWait();
	}

	private void mostrarMensajeExito(String mensaje) {
		Alert alerta = new Alert(Alert.AlertType.INFORMATION);
		alerta.getDialogPane().getStylesheets().add(getClass().getResource("../views/Alert.css").toExternalForm());
		alerta.setTitle("Éxito");
		alerta.setHeaderText(null);
		alerta.setContentText(mensaje);
		alerta.showAndWait();
	}

}
