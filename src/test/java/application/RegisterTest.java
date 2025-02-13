/*package application;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.*;
import static org.testfx.matcher.control.LabeledMatchers.hasText;


import controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RegisterTest extends ApplicationTest {

   private LoginController controller;
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginWindow.fxml"));
      StackPane root = loader.load();
      controller = loader.getController();
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
   }
   
   
   @Test
   public void testRegisterUsuarioExito() {
      clickOn("#btngoToRegister");
      // Registra un usuario con datos correctos
      clickOn("#nombreUsuario").write("testuser");
      clickOn("#password").write("1234");
      clickOn("#email").write("test@test.com");
      clickOn("nombreCompleto").write("Test User");
      clickOn("fechaNacimiento").write("2024-12-30");
      clickOn("#btnRegister");
      
      // Verifica el mensaje de éxito
      verifyThat("#mensajeExito", hasText("Usuario registrado correctamente"));
   }
   
   @Test
   public void testRegisterUsuarioDatosVacios() {
       // Intenta registrar sin llenar todos los campos
       clickOn("#nombreUsuario").write("testuser");
       clickOn("#btnRegister");

       // Verifica el mensaje de error
       verifyThat("#mensajeError", hasText("Todos los campos son obligatorios."));
   }
   
   @Test
   public void testLoginUsuarioExito() {
      clickOn("#nombreUsuarioLogin").write("RetroGamer01");
      clickOn("#passwordLogin").write("pass1234");
      clickOn("#btnLogin");
      
      verifyThat("#mensajeExitoLogin", hasText("Inicio de sesión existoso"));
   }
}
*/