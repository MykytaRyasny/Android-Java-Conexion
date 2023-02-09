package program;

import program.claves.KeyGen;
import program.socket.MultiConexion;

import java.io.*;
import java.net.*;
import java.security.KeyPair;

public class Main {

    public static int numConexion = 0;
    public static KeyPair parDeClaves = KeyGen.generarClaves();

    public static void main(String[] args) {
        // Crear un socket en el puerto 5000
        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            System.out.println("Servidor iniciado en el puerto 5000");

            while (true){
                ++numConexion;
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                MultiConexion mc = new MultiConexion(socket, in, out);
                mc.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
