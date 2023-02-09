package program.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

import program.bbdd.*;
import program.claves.KeyGen;
import program.Main;

public class MultiConexion implements Runnable {

    // Creamos una variable User a la que posteriormente le asignaremos el numero segun el contador
    // de usuarios. Que mas que numero de usuario es numero de conexion
    private int user = Main.numConexion;
    // Declaramos las variables que vamos a usar en nuestras conexiones
    private Socket socket;
    // Creamos nuestro input y output con el que nos comunicaremos con el cliente
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * Constructor para generar cada hilo con su propio flujo
     *
     * @param socket Se le pasa el socket con la conexion establecida
     * @param in Se crea el flujo de entrada
     * @param out Se crea el flujo de salida
     */
    public MultiConexion(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    // Creamos el .start() del Thread por haber implementado runnable
    Thread t;

    public void start(){
        if(t == null){
            t = new Thread(this);
            t.start();
        }
    }

    /**
     * El hilo que engloba todo_ el codigo que se ejecuta cada vez que alguien se conecta
     * Obteniendo las clave publica y privada que ya se generaron con el inicio del server
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
        while (true) {
            try {
                byte [] mensajeCifrado =  (byte[])in.readObject();
                String mensajeCliente = KeyGen.descifrar(mensajeCifrado, clavePrivada);
                String[] menu = mensajeCliente.split(":");
                switch (menu[0]) {
                    case "register":
                        Register r = new Register(mensajeCliente);
                        try {
                            r.register(mensajeCliente);
                            out.writeObject(true);
                            out.flush();
                            System.out.println("El cliente se ha registrado");
                        } catch (program.errores.registerError e) {
                            out.writeObject(false);
                            out.flush();
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "login":
                        Login l = new Login(mensajeCliente);
                        try {
                            l.login(mensajeCliente);
                            out.writeObject(true);
                            out.flush();
                            System.out.println("El cliente se ha logueado");
                        } catch (program.errores.loginError e){
                            out.writeObject(false);
                            out.flush();
                            System.out.println(e.getMessage());
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
