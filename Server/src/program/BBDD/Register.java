package program.BBDD;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends User {

    private String encriptedPass;

    public Register(String message) {
        super(message);
    }

    /**
     * Se conecta a la base de datos y guarda los datos previamente pasados
     * Los datos antes de guardarse se encriptan con BCrypt
     *
     * @param message el mensaje descifrado del usuario para dividirlo en
     *                Strings y guardarlo de forma encriptada en la BBDD
     */

    public void register(String message) {
        // Descomponemos el mensaje para obtener los valores de nickname, name y password
        String[] parts = message.split(":");
        this.name = parts[1];
        this.nickname = parts[2];
        this.password = parts[3];
        // Encriptamos la cotnraseña para guardarla en nuestra BBDD
        encriptedPass = BCrypt.hashpw(password, BCrypt.gensalt());

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Conectamos a la base de datos que se encuentra en el mismo directorio que el servidor
            conn = DriverManager.getConnection("jdbc:sqlite:users_db.db");


            // Creamos la consulta para introducir los valores
            String sql = "INSERT INTO users (name, nickname, password) VALUES (?, ?, ?)";

            // Le pasamos los parametros a nuestra consulta anteriormente creada
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.name);
            stmt.setString(2, this.nickname);
            stmt.setString(3, encriptedPass);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Importante cerrar todas las conexiones
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
