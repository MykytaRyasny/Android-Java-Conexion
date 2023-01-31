import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.*;
import java.sql.*;

public class Main {
    // Crear un socket en el puerto 5000
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            User u = new User();
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

                // Determinar si el usuario se registra o inicia la sesión
                // Procesar el mensaje con la base de datos que tenemos
                if (datos.startsWith("register:")) {
                    u.register(datos);
                } else {
                    u.login(datos);
                }

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
}
