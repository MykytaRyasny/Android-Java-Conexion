import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.*;
import java.sql.*;

public class Main {
    // Crear un socket en el puerto 5000
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor iniciado en el puerto 5000");

            while (true) {
                // Aceptar una conexión entrante del cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexión establecida con el cliente: " + clientSocket.getInetAddress().getHostAddress());

                // Crear flujos de entrada y salida
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());

                // La información que manda el usuario
                String datos = (String) input.readObject();

                // Procesar el mensaje con la base de datos que tenemos
                login(datos);

                // Enviar la respuesta al cliente
                // TODO implemetar un token
                output.writeObject("a");
                output.flush();
                System.out.println("Respuesta enviada al cliente: ");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void login(String datos) {
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
                String storedHash = result.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
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
