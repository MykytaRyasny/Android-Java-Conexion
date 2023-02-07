package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import BBDD.*;

public class MultiConexion implements Runnable {

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
        while (true) {
            try {
                String mensajeCliente = (String) in.readObject();
                System.out.println("El mensaje del cliente: " + mensajeCliente);
                String[] menu = mensajeCliente.split(":");
                switch (menu[0]) {
                    case "hash":
                        Salt s = new Salt(mensajeCliente);
                        String salt = s.salt(mensajeCliente);
                        System.out.println(salt);
                        out.writeObject(salt);
                        out.flush();
                        break;
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
