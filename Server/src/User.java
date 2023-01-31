import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class User {
    private String nickname;
    private String name;
    private String password;

    private User(String name, String nicname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }
    public User(){}

    public void register(String message) {
        // Descomponemos el mensaje para obtener los valores de nickname, name y password
        String[] parts = message.split(":");
        this.name = parts[1];
        this.nickname = parts[2];
        this.password = parts[3];

        System.out.println(password);
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Conectamos a la base de datos que se encuentra en el mismo directorio que el servidor
            conn = DriverManager.getConnection("jdbc:sqlite:users_db.db");


            // Step 3: Define a SQL statement
            String sql = "INSERT INTO users (name, nickname, password) VALUES (?, ?, ?)";

            // Step 4: Execute the SQL statement
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.name);
            stmt.setString(2, this.nickname);
            stmt.setString(3, this.password);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Step 5: Close the database connection
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void login(String datos) {
        String[] parts = datos.split(":");
        String username = parts[0];
        String password = parts[1];

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
                String storedPassword = result.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) {
                    System.out.println("Usuario autenticado");
                } else {
                    System.out.println("Contraseña incorrecta");
                }
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
