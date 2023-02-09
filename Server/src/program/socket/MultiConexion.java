package program.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

import program.BBDD.*;
import program.Claves.KeyGen;
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

    public MultiConexion(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    Thread t;

    public void start(){
        if(t == null){
            t = new Thread(this);
            t.start();
        }
    }
    public void run() {
        PublicKey clavePublica = Main.parDeClaves.getPublic();
        PrivateKey clavePrivada = Main.parDeClaves.getPrivate();
        try {
            out.writeObject(clavePublica);
            out.flush();
            System.out.println("Se ha mandado la clave publica: " + clavePublica);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                byte [] mensajeCifrado =  (byte[])in.readObject();
                System.out.println(mensajeCifrado);
                String mensajeCliente = KeyGen.descifrar(mensajeCifrado, clavePrivada);
                System.out.println("El mensaje del cliente: " + user + " " + mensajeCliente);
                String[] menu = mensajeCliente.split(":");
                switch (menu[0]) {
                    case "register":
                        Register r = new Register(mensajeCliente);
                        r.register(mensajeCliente);
                        break;
                    case "login":
                        Login l = new Login(mensajeCliente);
                        l.login(mensajeCliente);
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
