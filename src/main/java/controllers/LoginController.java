package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import utils.BBDDUtils;
import utils.EmailUtil;
import utils.MensajesErrorUtil;
import utils.TransitionUtil;

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
   private PasswordField newPasswordField;

   @FXML
   private PasswordField newPasswordFieldConfirmation;

   @FXML
   private TextField newTxtPassword;

   @FXML
   private TextField newTxtPasswordConfirmation;

   @FXML
   private TextField email;

   @FXML
   private TextField nombreCompleto;

   @FXML
   private StackPane closedEye;

   @FXML
   private StackPane openEye;

   @FXML
   private StackPane newClosedEye;

   @FXML
   private StackPane newOpenEye;

   @FXML
   private StackPane newClosedEyeConfirmation;

   @FXML
   private StackPane newOpenEyeConfirmation;

   @FXML
   private Pane closeAppBtn;

   @FXML
   private AnchorPane recoveryPasswordPane;

   @FXML
   private AnchorPane verifyCodePane;

   @FXML
   private AnchorPane newPasswordPane;

   @FXML
   private Label recoverPasswordBtn;

   @FXML
   private Button btnSendCode;

   @FXML
   private Button btnVerifyCode;

   @FXML
   private TextField txtCorreoRecuperacion;

   @FXML
   private TextField txtCodigoRecuperacion;

   @FXML
   private Label emailErrorLabel;

   @FXML
   private Label passwordErrorLabel;

   @FXML
   private Label usernameErrorLabel;
   
   // Instancias
   private TransitionUtil transition = new TransitionUtil();
   private BBDDUtils bdUtil = new BBDDUtils();
   private MensajesErrorUtil mensaje = new MensajesErrorUtil();

   private String emailUsuario;

   @FXML
   void goToLogin(MouseEvent event) {
      transition.fadeSwitch(registerPane, loginPane, 300);
   }

   @FXML
   void goToRegister(MouseEvent event) {
      transition.fadeSwitch(loginPane, registerPane, 300);
   }

   @FXML
   void goToRecoverPassword() {
      transition.slideSwitch(null, recoveryPasswordPane, 0, 0, 200, true, 700);
   }

   @FXML
   private void closePopup(MouseEvent event) {
      // Obtenemos el botón que triggereo el evento
      Node source = (Node) event.getSource();
      // Cogemos el pane correcto
      AnchorPane pane = (AnchorPane) source.getParent();

      if (pane != null) {
         transition.slideSwitch(null, pane, 0, 0, 200, false, 700);
      }
   }

   @FXML
   void showPassword() {
      togglePasswordVisibility(loginPasswordTextField, loginPasswordField, openEye, closedEye);
   }

   @FXML
   void showNewPassword() {
      togglePasswordVisibility(newTxtPassword, newPasswordField, newOpenEye, newClosedEye);
   }

   @FXML
   void showNewPasswordConfirmation() {
      togglePasswordVisibility(newTxtPasswordConfirmation, newPasswordFieldConfirmation, newOpenEyeConfirmation,
            newClosedEyeConfirmation);
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
         mensaje.mostrarMensajeError("Todos los campos son obligatorios.");
         return;
      }

      String usernamePattern = "^[a-zA-Z0-9]+$"; // Solo letras y números
      if (!nombreUsuario.matches(usernamePattern)) {
         return;
      }

      // Validar formato del correo
      String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
      if (!email.matches(emailPattern)) {
         return;
      }

      // Validar formato de la contraseña
      String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
      if (!password.matches(passwordPattern)) {
         return;
      }

      boolean success = bdUtil.registerUser(nombreUsuario, password, email, nombreCompleto, fechaNacimiento);

      if (success) {
         mensaje.mostrarMensajeExito("Usuario registrado correctamente");
         goToLogin(event);
         limpiarCampos();
      } else {
         mensaje.mostrarMensajeError("Error al registrar el usuario");
      }
   }

   @FXML
   void loginUser(MouseEvent event) {
      String userInput = ((TextField) loginPane.lookup("#loginUsernameField")).getText();

      // Obtenemos la contraseña del campo visible
      String plainPassword;
      if (loginPasswordField.isVisible()) {
         plainPassword = loginPasswordField.getText();
      } else {
         plainPassword = loginPasswordTextField.getText();
      }

      if (userInput.isEmpty() || plainPassword.isEmpty()) {
         mensaje.mostrarMensajeError("El nombre de usuario/correo y la contraseña son obligatorios.");
         return;
      }

      if (bdUtil.verificaContrasenya(userInput, plainPassword)) {
         Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         // Cerramos la ventana actual
         currentStage.close();

         // Abrimos la nueva ventana
         openHome(userInput);
      }
   }

   @FXML
   void sendCode() {
      String destinatario = txtCorreoRecuperacion.getText();

      // Verificar si el correo está registrado
      if (bdUtil.isEmailRegistered(destinatario)) {
         emailUsuario = destinatario;
         String recoveryCode = generateRecoveryCode();
         bdUtil.updateRecoveryCode(destinatario, recoveryCode);
         EmailUtil.correoRecuperacionContrasenya(destinatario, recoveryCode);
         transition.fadeSwitch(recoveryPasswordPane, verifyCodePane, 300);
      } else {
         mensaje.mostrarMensajeError("El correo electrónico no está registrado.");
      }
   }

   @FXML
   void verifyCode() {
      System.out.println(emailUsuario);
      String code = txtCodigoRecuperacion.getText();
      if (bdUtil.verifyCodeFromDB(emailUsuario, code)) {
         System.out.println("Codigo Correcto");
         transition.fadeSwitch(verifyCodePane, newPasswordPane, 300);

      } else {
         mensaje.mostrarMensajeError("El código de recuperación es incorrecto, prueba otra vez");
      }
   }

   @FXML
   void changePassword() {
      // Obtenemos la contraseña del campo visible
      String plainPassword;
      if (newPasswordField.isVisible()) {
         plainPassword = newPasswordField.getText();
      } else {
         plainPassword = newTxtPassword.getText();
      }
      String plainPasswordConfirmation;
      if (newPasswordFieldConfirmation.isVisible()) {
         plainPasswordConfirmation = newPasswordFieldConfirmation.getText();
      } else {
         plainPasswordConfirmation = newTxtPasswordConfirmation.getText();
      }

      if (plainPassword.equals(plainPasswordConfirmation)) {
         System.out.println("Cambiando contraseñas...");
         bdUtil.updatePassword(emailUsuario, plainPassword);
         transition.fadeSwitch(newPasswordPane, null, 300);
      } else {
         mensaje.mostrarMensajeError("Las contraseñas no coinciden");
      }
   }

   // Genera un código aleatorio de 6 dígitos
   private String generateRecoveryCode() {
      return String.format("%06d", (int) (Math.random() * 1_000_000));
   }

   // Limpia los campos del registro
   private void limpiarCampos() {
      // Limpiar campos de texto
      nombreUsuario.clear();
      email.clear();
      nombreCompleto.clear();
      password.clear();
      fechaNacimiento.setValue(null);
   }

   private void togglePasswordVisibility(TextField textField, PasswordField passwordField, StackPane openEye,
         StackPane closedEye) {
      boolean isHiding = passwordField.isVisible();

      if (isHiding) {
         textField.setText(passwordField.getText());
         passwordField.setVisible(false);
         textField.setVisible(true);

         openEye.setVisible(true);
         closedEye.setVisible(false);

      } else {
         passwordField.setText(textField.getText());
         textField.setVisible(false);
         passwordField.setVisible(true);

         openEye.setVisible(false);
         closedEye.setVisible(true);
      }
   }

   // Abre la ventana Home de la aplicación
   private void openHome(String userName) {
      try {

         FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoadingScreen.fxml"));
         Parent root = loader.load();

         LoaderController loaderController = loader.getController();

         // Pasar el nombre del usuario al controlador
         loaderController.setUserName(userName);

         Scene scene = new Scene(root);

         Stage newStage = new Stage();
         newStage.setScene(scene);
         newStage.getIcons().add(new Image("/images/retroRack_logo.png"));
         newStage.initStyle(StageStyle.UNDECORATED);
         newStage.setTitle("Home");
         newStage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   public void initialize() {
      // Listener para validar el email al perder el foco
      registerPane.setOnMouseClicked(event -> {
         validatePasswordOnClickOutside();
         validateEmailOnClickOutside();
         validateUsernameOnClickOutside();
      });
   }

   private void validateUsernameOnClickOutside() {

      String usernameString = nombreUsuario.getText();
      if (!usernameString.isEmpty()) {
         String usernamePattern = "^[a-zA-Z0-9]{4,}$";
         if (!usernameString.matches(usernamePattern)) {
            nombreUsuario.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 30px;");
            usernameErrorLabel.setText("El nombre de usuario no tiene el formato correcto.");
            usernameErrorLabel.setVisible(true);
         } else {
            nombreUsuario.setStyle(""); // Restablece el estilo si es correcto
            usernameErrorLabel.setText("");
            usernameErrorLabel.setVisible(false);
         }
      }
   }

   private void validateEmailOnClickOutside() {

      String emailString = email.getText();
      if (!emailString.isEmpty()) {
         String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
         if (!emailString.matches(emailPattern)) {
            email.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 30px;");
            emailErrorLabel.setText("El correo no tiene el formato correcto.");
            emailErrorLabel.setVisible(true);
         } else {
            email.setStyle(""); // Restablece el estilo si es correcto
            emailErrorLabel.setText("");
            emailErrorLabel.setVisible(false);
         }
      }
   }

   private void validatePasswordOnClickOutside() {
      String passwordString = password.getText();
      if (!passwordString.isEmpty()) {
         // Expresión regular para validar mayúscula, minúscula, número y carácter
         // especial
         String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
         if (!passwordString.matches(passwordPattern)) {
            password.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 30px;");
            passwordErrorLabel.setText(
                  "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial.");
            passwordErrorLabel.setVisible(true);
         } else {
            password.setStyle(""); // Restablece el estilo si es correcto
            passwordErrorLabel.setText("");
            passwordErrorLabel.setVisible(false);
         }
      }
   }

}
