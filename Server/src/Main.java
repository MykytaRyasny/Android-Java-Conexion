import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear un socket en el puerto 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor iniciado en el puerto 5000");

            while (true) {
                // Aceptar una conexión entrante del cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexión establecida con el cliente: " + clientSocket.getInetAddress().getHostAddress());

                // Crear flujos de entrada y salida
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());

                // Leer el mensaje enviado por el cliente
                String message = (String) input.readObject();
                System.out.println("Mensaje recibido del cliente: " + message);

                // Procesar el mensaje
                String response = processMessage(message);

                // Enviar la respuesta al cliente
                output.writeObject(response);
                output.flush();
                System.out.println("Respuesta enviada al cliente: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String processMessage(String message) {
        // Aquí puedes procesar el mensaje enviado por el cliente
        return "Mensaje recibido: " + message;
    }
}
