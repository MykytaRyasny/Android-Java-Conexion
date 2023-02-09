package program.BBDD;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class Login extends User {

    public Login(String message) {
        super(message);
    }

    /**
     * Trabaja con los datos transformandolos
     * en cadenas separadas para comprobar con la base de datos
     *
     * @param datos datos que separa en diferentes Strings
     */
    public void login(String datos) {
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
            //String salt = program.BBDD.Salt.obtenerSalt(datos);

            // Comprobar si el usuario existe en la base de datos
            if (result.next()) {
                // Si hay un .next() es que existe y compramos contraseñas
                String storedPassword = result.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) {
                    System.out.println("Usuario autenticado");
                } else {
                    System.out.println("Contraseña incorrecta");
                }
                // No existe por que .next() devuelve false
            } else {
                System.out.println("Usuario no encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error de conexión con la base de datos");
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
