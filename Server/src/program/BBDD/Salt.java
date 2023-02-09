package program.BBDD;

import java.sql.*;

public class Salt extends User {

    public Salt(String message) {
        super(message);
    }

    /**
     * Funcion para cifrar texto plano con hash conocido
     * para posteriormente comprarlo con los datos del cliente
     *
     * @param datos La cadena de la cual vamos a substraer el hash
     * @return Nos devuelve el hash con el que se ha cifrado la cotraseña en
     * formato de String
     */

    public static String obtenerSalt(String datos) {
        String[] parts = datos.split(":");
        String username = parts[1];
        String saltString = null;

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users_db.db");

            // Crear una consulta para obtener los datos del usuario
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE nickname = ?");
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            // Obtenemos los primeros 29 caracteres
            if (result.next()) {
                saltString = result.getString("password").substring(0, 29);
                System.out.println("Los primeros 29 caracteres de la contraseña son: " + saltString);
            } else {
                System.out.println("Usuario no encontrado");
            }
            return saltString;
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
        return null;
    }
}
