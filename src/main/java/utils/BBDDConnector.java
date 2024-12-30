package utils;

import java.sql.Connection ;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para conectarnos a la BBDD
 */
public class BBDDConnector implements AutoCloseable {

   // Atributos
   private static final String URL = "jdbc:mysql://localhost:3306/";
   private static final String BBDD = "bdretrorack";
   private static final String PARAMETROS = "?serverTimezone=UTC";
   private static final String USUARIO = "root";
   private static final String CLAVE = "root";
   
   private Connection conexion;

   /**
    * Método para conectarnos a la BBDD
    * @return objeto de la clase Connection
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

}
