package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import models.Game;

/**
 * Clase para hacer todo tipo de operaciones en la BBDD con SQLite.
 */
public class BBDDUtils implements AutoCloseable {

   // Queries
   private final static String CHECK_EMAIL = "SELECT COUNT(*) AS count FROM Usuario WHERE email = ?";
   private final static String INSERT_USER = "INSERT INTO Usuario (nombre_usuario, password, email, nombre_completo, fecha_nacimiento, fecha_registro) "
         + "VALUES (?, ?, ?, ?, ?, ?)";


   // Atributos
   private static final String URL = "jdbc:sqlite:retrorack.db"; // Archivo de la base de datos SQLite
   private Connection conexion;
   private MensajesErrorUtil mensajeError = new MensajesErrorUtil();

   /**
    * Método para conectarnos a la BBDD SQLite.
    * 
    * @return - Objeto de la clase Connection
    */
   public Connection conectar() {
      conexion = null;

      try {
         // Creamos la conexión con SQLite
         conexion = DriverManager.getConnection(URL);
         System.out.println("Conexion OK");
      } catch (SQLException e) {
         // Lanzamos error si falla la conexión
         System.out.println("Error en la conexion");
         e.printStackTrace();
      }

      return conexion;
   }

   @Override
   public void close() {
      if (conexion != null) {
         try {
            conexion.close();
            System.out.println("Conexion cerrada correctamente.");
         } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion");
            e.printStackTrace();
         }
      }
   }

   /**
    * Crea la tabla Usuario si no existe.
    */
   public void crearTablaSiNoExiste() {
      String createTableSQL = "CREATE TABLE IF NOT EXISTS Usuario (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nombre_usuario TEXT NOT NULL, " + "password TEXT NOT NULL, " + "email TEXT NOT NULL UNIQUE, "
            + "nombre_completo TEXT NOT NULL, " + "fecha_nacimiento TEXT NOT NULL, " + "fecha_registro TEXT NOT NULL, "
            + "cod_recuperacion TEXT)";

      try (Connection con = conectar(); Statement stmt = con.createStatement()) {
         stmt.execute(createTableSQL);
         System.out.println("Tabla creada o ya existente.");
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   /**
    * Registra un usuario en la BBDD.
    * 
    * @param nombreUsuario   - Nombre del usuario (Username)
    * @param password        - Contraseña del usuario
    * @param email           - Correo Electrónico del usuario
    * @param nombreCompleto  - Nombre completo del usuario
    * @param fechaNacimiento - Fecha de nacimiento del usuario
    * @return - True si el registro se ha realizado con éxito, false si no
    */
   public boolean registerUser(String nombreUsuario, String password, String email, String nombreCompleto,
         LocalDate fechaNacimiento) {

      try (BBDDUtils connector = new BBDDUtils();
            Connection con = connector.conectar();
            PreparedStatement checkEmailStmt = con.prepareStatement(CHECK_EMAIL);
            PreparedStatement insertStmt = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

         con.setAutoCommit(false);

         // Verificar si el correo ya existe
         checkEmailStmt.setString(1, email);
         ResultSet rs = checkEmailStmt.executeQuery();
         if (rs.next() && rs.getInt("count") > 0) {
            mensajeError.mostrarMensajeError("El correo electrónico ya está registrado.");
            return false;
         }

         String hashedPassword = PasswordUtil.hashPassword(password);
         // Insertar el usuario
         insertStmt.setString(1, nombreUsuario);
         insertStmt.setString(2, hashedPassword);
         insertStmt.setString(3, email);
         insertStmt.setString(4, nombreCompleto);
         insertStmt.setString(5, fechaNacimiento.toString()); // Fecha como String
         insertStmt.setString(6, LocalDate.now().toString()); // Fecha como String

         if (insertStmt.executeUpdate() > 0) {
            // Enviamos correo de bienvenida
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

   /**
    * Verifica que la contraseña para logear sea la misma que la que se encuentra
    * en la BBDD.
    * 
    * @param userInput     - Username o correo electrónico del usuario
    * @param plainPassword - Contraseña sin codificar
    * @return boolean True si el inicio de sesión es exitoso, falso si no
    */
   public boolean verificaContrasenya(String userInput, String plainPassword) {

      String query = "SELECT password FROM Usuario WHERE email = ? OR nombre_usuario = ?";

      try (BBDDUtils connector = new BBDDUtils();
            Connection con = connector.conectar();
            PreparedStatement stmt = con.prepareStatement(query)) {

         stmt.setString(1, userInput);
         stmt.setString(2, userInput);

         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            String hashedPassword = rs.getString("password");

            // Verificamos la contraseña
            if (PasswordUtil.verifyPassword(plainPassword, hashedPassword)) {
               return true; // Si la contraseña es correcta, retorno true
            } else {
               mensajeError.mostrarMensajeError("La contraseña es incorrecta.");
               return false;
            }
         } else {
            mensajeError.mostrarMensajeError("El usuario o correo electrónico no existe.");
            return false;
         }
      } catch (SQLException e) {
         e.printStackTrace();
         mensajeError.mostrarMensajeError("Ocurrió un error al intentar iniciar sesión.");
         return false;
      }
   }

   /**
    * Verifica que el email este en la BBDD
    * 
    * @param email - Email del usuario
    * @return True si el email existe en la BBDD, false si no
    */
   public boolean isEmailRegistered(String email) {
      String query = "SELECT COUNT(*) AS count FROM Usuario WHERE email = ?";

      try (BBDDUtils connector = new BBDDUtils();
            Connection con = connector.conectar();
            PreparedStatement stmt = con.prepareStatement(query)) {

         stmt.setString(1, email);
         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            return rs.getInt("count") > 0;
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

      return false;
   }

   /**
    * Inserta un codigo de recuperacion en la BBDD
    * 
    * @param email        - Email al que se va a mandar ese código
    * @param recoveryCode - Código de recuperacion
    * @return - True si no ocurre ningun error, false si ocurre alguno
    */
   public boolean updateRecoveryCode(String email, String recoveryCode) {
      String query = "UPDATE Usuario SET cod_recuperacion = ? WHERE email = ?";

      try (BBDDUtils connector = new BBDDUtils();
            Connection con = connector.conectar();
            PreparedStatement updateStmt = con.prepareStatement(query)) {

         con.setAutoCommit(false);

         updateStmt.setString(1, recoveryCode);
         updateStmt.setString(2, email);
         int rowsAffected = updateStmt.executeUpdate();

         if (rowsAffected > 0) {
            // Confirmamos los cambios
            con.commit();
            return true;
         } else {
            // Si no se actualizó ninguna fila, hacemos rollback
            con.rollback();
            return false;
         }
      } catch (SQLException e) {
         return false;
      }
   }

   /**
    * Verifica que el codigo que ha metido el usuario sea igual que el que le
    * corresponde en la BBDD
    * 
    * @param destinatario - Email del usuario al que se le ha mandado el código
    * @param code         - Código insertado por el usuario
    * @return - True si los códigos son iguales, false si no
    */
   public boolean verifyCodeFromDB(String destinatario, String code) {
      String query = "SELECT cod_recuperacion FROM usuario WHERE email = ? AND cod_recuperacion = ?";

      try (BBDDUtils connector = new BBDDUtils();
            Connection con = connector.conectar();
            PreparedStatement stmt = con.prepareStatement(query)) {

         stmt.setString(1, destinatario);
         stmt.setString(2, code);

         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               System.out.println("Código de recuperación verificado correctamente.");
               return true;
            } else {
               System.err.println("El código de recuperación es incorrecto.");
               return false;
            }
         }
      } catch (SQLException e) {
         System.err.println("Error al verificar el código de recuperación: " + e.getMessage());
         return false;
      }
   }

   /**
    * Actualiza la contraseña de un usuario
    * 
    * @param email       - Email del usuario
    * @param newPassword - Nueva contraseña del usuario
    * @return - True si se cambia con exito, false si no
    */
   public boolean updatePassword(String email, String newPassword) {
      String query = "UPDATE usuario SET password = ? WHERE email = ?";
      String hashedPassword = PasswordUtil.hashPassword(newPassword);

      try (BBDDUtils connector = new BBDDUtils();
            Connection con = connector.conectar();
            PreparedStatement stmt = con.prepareStatement(query)) {

         stmt.setString(1, hashedPassword);
         stmt.setString(2, email);

         int rowsUpdated = stmt.executeUpdate();
         return rowsUpdated > 0;
      } catch (SQLException e) {
         e.printStackTrace();
         return false;
      }
   }
   
   public boolean isGameInDatabase(int gameId) {
      String query = "SELECT id FROM Game WHERE id = ?";
      try (Connection conn = conectar();
           PreparedStatement pstmt = conn.prepareStatement(query)) {
          pstmt.setInt(1, gameId);
          ResultSet rs = pstmt.executeQuery();
          return rs.next();
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }

  public void insertGameIntoDatabase(Game game) {
      String query = "INSERT INTO Game (id, slug, name, releaseDate, tba, background_image, description_raw, rating, " +
                     "ratingTop, ratingsCount, reviewsCount, added, playtime, metacritic, esrbRating) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      try (Connection conn = conectar();
           PreparedStatement pstmt = conn.prepareStatement(query)) {
          pstmt.setInt(1, game.getId());
          pstmt.setString(2, game.getSlug());
          pstmt.setString(3, game.getName());
          pstmt.setString(4, game.getReleaseDate());
          pstmt.setBoolean(5, game.isTba());
          pstmt.setString(6, game.getBackgroundImage());
          pstmt.setString(7, game.getDescription());
          pstmt.setDouble(8, game.getRating());
          pstmt.setInt(9, game.getRatingTop());
          pstmt.setInt(10, game.getRatingsCount());
          pstmt.setInt(11, game.getReviewsCount());
          pstmt.setInt(12, game.getAdded());
          pstmt.setInt(13, game.getPlaytime());
          pstmt.setInt(14, game.getMetacritic());
          pstmt.setString(15, game.getEsrbRating());

          pstmt.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  public List<Game> getAllGamesFromDatabase() {
      List<Game> games = new ArrayList<>();
      String query = "SELECT * FROM Game";
      try (Connection conn = conectar();
           PreparedStatement pstmt = conn.prepareStatement(query);
           ResultSet rs = pstmt.executeQuery()) {

          while (rs.next()) {
              Game game = new Game(
                  rs.getInt("id"),
                  rs.getString("slug"),
                  rs.getString("name"),
                  rs.getString("releaseDate"),
                  rs.getBoolean("tba"),
                  rs.getString("background_image"),
                  rs.getString("description_raw"),
                  rs.getDouble("rating"),
                  rs.getInt("ratingTop"),
                  null, // ratings (se pueden cargar desde otra tabla si es necesario)
                  rs.getInt("ratingsCount"),
                  rs.getInt("reviewsCount"),
                  rs.getInt("added"),
                  rs.getInt("playtime"),
                  rs.getInt("metacritic"),
                  rs.getString("esrbRating"),
                  null, // platforms
                  null, // genres
                  null, // shops
                  null, // tags
                  null  // screenshots
              );
              games.add(game);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return games;
  }


}
