import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String nickname;
    private String name;
    private String password;

    public User(String nickname, String name, String password) {
        this.nickname = nickname;
        this.name = name;
        this.password = password;
    }

    public void register() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Conectamos a la base de datos que se encuentra en el mismo directorio que el servidor
            conn = DriverManager.getConnection("jdbc:sqlite:users_db.db");

            // Step 2: Hash the password
            String hashedPassword = BCrypt.hashpw(this.password, BCrypt.gensalt());

            // Step 3: Define a SQL statement
            String sql = "INSERT INTO users (nickname, name, password) VALUES (?, ?, ?)";

            // Step 4: Execute the SQL statement
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.nickname);
            stmt.setString(2, this.name);
            stmt.setString(3, hashedPassword);
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
