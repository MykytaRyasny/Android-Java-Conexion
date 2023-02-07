package BBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends User {
    public Register(String message) {
        super(message);
    }

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
}
