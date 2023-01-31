import BBDD.Login;
import BBDD.Register;
import BBDD.User;

import java.io.*;
import java.net.*;

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

                // Determinar si el usuario se registra o inicia la sesión
                // Procesar el mensaje con la base de datos que tenemos
                if (datos.startsWith("register:")) {
                    Register r = new Register(datos);
                    r.start();
                } else {
                    Login l = new Login(datos);
                    l.start();
                }

                // Enviar la respuesta al cliente
                // TODO implemetar un token
                output.writeObject("");
                output.flush();
                System.out.println("Respuesta enviada al cliente: ");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
