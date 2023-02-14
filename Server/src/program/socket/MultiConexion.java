package program.socket;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import program.bbdd.*;
import program.claves.KeyGen;
import program.Main;

public class MultiConexion implements Runnable {
  
  
  /**
   * El hilo que engloba todo_ el codigo que se ejecuta cada vez que alguien se conecta
   * Obteniendo las clave publica y privada que ya se generaron con el inicio del server
   * la funcion run() por si solo será nuestro menu para ir poniendo funcionalidades
   */
  public void run() {
    // Obtenemos las claves
    PublicKey clavePublica = Main.parDeClaves.getPublic();
    PrivateKey clavePrivada = Main.parDeClaves.getPrivate();
    try {
      // Mandamos la publica
      out.writeObject(clavePublica);
      out.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (!detener) {
      try {
        byte[] mensajeCifrado = (byte[]) in.readObject();
        String mensajeCliente = KeyGen.descifrar(mensajeCifrado, clavePrivada);
        String[] menu = mensajeCliente.split(":");
        switch (menu[0]) {
          case "register":
            register(mensajeCliente);
            break;
          case "login":
            login(mensajeCliente);
            break;
          case "desconectar":
            cerrarConexiones();
            break;
        }
      } catch (SocketException e) {
        try {
          log("conexion closed", this.socket);
          detenerHilo();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } catch (EOFException e) {
        try {
          log("Desconexion inesperada EOFException", this.socket);
          detenerHilo();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } catch (IOException e) {
        try {
          log("Desconexion inesperada IOException", this.socket);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  /**
   * Cerramos el socket a peticion de usuario para que no se produzca EOFException
   * Tanto Cliente como servidor cierran socket y los input y output
   * @throws IOException
   */
  private void cerrarConexiones() throws IOException {
    log("desconectado", this.socket);
    detenerHilo();
  }
  
  /**
   * Hacemos login
   * @param mensajeCliente es el mensaje que nos llega por input
   * @throws IOException la excepcion a capturar por escribir en el log
   */
  private void login(String mensajeCliente) throws IOException {
    Login l = new Login(mensajeCliente);
    try {
      l.login(mensajeCliente);
      out.writeObject(true);
      out.flush();
      log("login", this.socket);
    } catch (program.errores.loginError e) {
      out.writeObject(false);
      out.flush();
      log(e.getMessage(), this.socket);
      detenerHilo();
    }
  }
  
  /**
   * Constructor para generar cada hilo con su propio flujo
   *
   * @param socket Se le pasa el socket con la conexion establecida
   * @param in     Se crea el flujo de entrada
   * @param out    Se crea el flujo de salida
   */
  public MultiConexion(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
    this.socket = socket;
    this.in = in;
    this.out = out;
  }
  
  /**
   * Metodo syncronizado para escribir el log
   *
   * @param socket Se proporciona socket para que pueda obtener la IP y guardarla
   *               en el log
   * @param log    Se pasa como argumento el texto que se quiere escribir
   * @throws IOException importante que el archivo no este bloqueado
   *                     o se tenga permisos para abrir.
   */
  public synchronized void log(String log, Socket socket) throws IOException {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH.mm.ss");
    LocalDateTime now = LocalDateTime.now();
    String escribirFile = dtf.format(LocalDateTime.now()) + ":" + log + ":" + socket.getInetAddress().getHostAddress() + "\n";
    Path p = Path.of(Main.f.toURI());
    Files.writeString(p, escribirFile, StandardOpenOption.APPEND);
  }
  /**
   * Un flag para detener un Hilo y cerrar las conexiones
   * sin hacer un interrupt
   *
   * @throws IOException
   */
  public void detenerHilo() throws IOException {
    detener = true;
    in.close();
    out.close();
    System.out.println("Cerramos todas las conexiones");
  }
  
  /**
   * Metodo encargado de hacer login
   * @param mensajeCliente el mensaje que llega del cliente
   * @throws IOException se puede producir en el log
   */
  private void register(String mensajeCliente) throws IOException {
    Register r = new Register(mensajeCliente);
    try {
      r.register(mensajeCliente);
      out.writeObject(true);
      out.flush();
      log("registro", this.socket);
      detenerHilo();
    } catch (program.errores.registerError e) {
      out.writeObject(false);
      out.flush();
      log(e.getMessage(), this.socket);
      detenerHilo();
    }
  }
  
  // Creamos el .start() del Thread por haber implementado runnable
  Thread t;
  
  public void start() {
    if (t == null) {
      t = new Thread(this);
      t.start();
    }
  }
  // Creamos una variable User a la que posteriormente le asignaremos el numero segun el contador
  // de usuarios. Que mas que numero de usuario es numero de conexion
  private int user = Main.numConexion;
  // Declaramos las variables que vamos a usar en nuestras conexiones
  private Socket socket;
  // Creamos nuestro input y output con el que nos comunicaremos con el cliente
  private ObjectInputStream in;
  private ObjectOutputStream out;
  
  // Variable para detener el hilo de forma segura
  boolean detener = false;
}
