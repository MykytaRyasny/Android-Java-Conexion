package program;

import program.claves.KeyGen;
import program.socket.MultiConexion;

import java.io.*;
import java.net.*;
import java.security.KeyPair;

/**
 * Nuestra Clase Main
 */
public class Main {
  
  public static KeyPair parDeClaves = KeyGen.generarClaves();
  public static File f;
  
  public static void main(String[] args) {
    // Crear un socket en el puerto 5000
    try (ServerSocket serverSocket = new ServerSocket(5000)) {
      f = new File("./log.txt");
      if (!f.exists()) {
        f.createNewFile();
      }
      System.out.println("Servidor iniciado en el puerto 5000");
      
      while (true) {
        Socket socket = serverSocket.accept();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        MultiConexion mc = new MultiConexion(socket, in, out);
        mc.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
