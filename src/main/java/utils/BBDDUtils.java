package utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Clase para hacer todo tipo de operaciones en la BBDD
 */
public class BBDDUtils implements AutoCloseable {

	// Queries
	private final static String GET_MAX_ID = "SELECT MAX(id) AS max_id FROM Usuario";
	private final static String CHECK_EMAIL = "SELECT COUNT(*) AS count FROM Usuario WHERE email = ?";
	private final static String INSERT_USER = "INSERT INTO Usuario (id, nombre_usuario, password, email, "
			+ "nombre_completo, fecha_nacimiento, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";

	// Atributos
	private static final String URL = "jdbc:mysql://localhost:3306/";
	private static final String BBDD = "bdretrorack";
	private static final String PARAMETROS = "?serverTimezone=UTC";
	private static final String USUARIO = "root";
	private static final String CLAVE = "1234";

	private MensajesErrorUtil mensajeError = new MensajesErrorUtil();
	private Connection conexion;

	/**
	 * Método para conectarnos a la BBDD
	 * 
	 * @return - Objeto de la clase Connection
	 */
	public Connection conectar() {
		conexion = null;

		try {
			// Creamos la conexión con los datos introducidos anteriormente
			conexion = DriverManager.getConnection(URL + BBDD + PARAMETROS, USUARIO, CLAVE);
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
	 * Registra un usuario en la BBDD
	 * 
	 * @param nombreUsuario   - Nombre del usuario (Username)
	 * @param password        - Contaseña del usuario
	 * @param email           - Correo Electrónico del usuario
	 * @param nombreCompleto  - Nombre completo del usuario
	 * @param fechaNacimiento - Fecha de nacimiento del usuario
	 * @return True si el registro se ha realizado con éxito, false si no
	 */
	public boolean registerUser(String nombreUsuario, String password, String email, String nombreCompleto,
			LocalDate fechaNacimiento) {

		try (BBDDUtils connector = new BBDDUtils();
				Connection con = connector.conectar();
				PreparedStatement maxIdStmt = con.prepareStatement(GET_MAX_ID);
				PreparedStatement checkEmailStmt = con.prepareStatement(CHECK_EMAIL);
				PreparedStatement insertStmt = con.prepareStatement(INSERT_USER)) {

			con.setAutoCommit(false);

			// Verificar si el correo ya existe
			checkEmailStmt.setString(1, email);
			ResultSet rs = checkEmailStmt.executeQuery();
			if (rs.next() && rs.getInt("count") > 0) {
				mensajeError.mostrarMensajeError("El correo electrónico ya está registrado.");
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

	/**
	 * Verifica que la contraseña para logear sea la misma que la que se encuentra
	 * en la BBDD
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
	 * @return True si no ocurre ningun error, false si ocurre alguno
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
	 * @param code  - Código insertado por el usuario
	 * @return True si los códigos son iguales, false si no
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
	 * 
	 * @param email
	 * @param newPassword
	 * @return
	 */
	public boolean updatePassword(String email, String newPassword) {
	    String query = "UPDATE usuario SET password = ? WHERE email = ?";
	    String hashedPassword = PasswordUtil.hashPassword(newPassword);

	    try (BBDDUtils connector = new BBDDUtils();
	         Connection con = connector.conectar();
	         PreparedStatement stmt = con.prepareStatement(query)) {
	        
	        stmt.setString(1, hashedPassword); // Establece la contraseña hasheada
	        stmt.setString(2, email);          // Establece el email del usuario

	        int rowsUpdated = stmt.executeUpdate();
	        return rowsUpdated > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


}
