package program.bbdd;

import org.mindrot.jbcrypt.BCrypt;
import program.errores.loginError;

import java.sql.*;

/**
 * Clase Login que extiende de user para
 * Permite crear un objeto de tipo user
 */
public class Login extends User {
  /**
   * Contructor de Login
   *
   * @param message Se le pasa el mensaje con el nombre de usuario y contraseña
   */
  public Login(String message) {
    super(message);
  }
  
  /**
   * Trabaja con los datos transformandolos
   * en cadenas separadas para comprobar con la base de datos
   *
   * @param datos datos que separa en diferentes Strings
   * @throws loginError excecpion que se lanza si Ocurre algun error de login
   *                    nombre de usuario o ccontraseña incorrecta
   */
  public void login(String datos) throws loginError {
    String[] parts = datos.split(":");
    String username = parts[1];
    String password = parts[2];
    
    // Conectarse a la base de datos SQLite
    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:users_db.db");
      
      // Crear una consulta para obtener los datos del usuario
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE nickname = ?");
      statement.setString(1, username);
      ResultSet result = statement.executeQuery();
      
      // Comprobar si el usuario existe en la base de datos
      if (result.next()) {
        // Si hay un .next() es que existe y compramos contraseñas
        String storedPassword = result.getString("password");
        if (BCrypt.checkpw(password, storedPassword)) {
        } else {
          throw new loginError("Contraseña incorrecta");
        }
        // No existe por que .next() devuelve false
      } else {
        throw new loginError("Usuario no encontrado");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        // Ignorar
      }
    }
  }
}
