package com.example.iotproyect

import org.mindrot.jbcrypt.BCrypt;
import java.io.*
import java.net.*

// Clase con metodos para conectarse y desconectarse del servidor
class Conexion() {

    private lateinit var socket: Socket
    companion object{
        private lateinit var salt:String
    }
    // Metodo conectar para conectar al servidor
    fun login(username:String, password:String) {
        // Conectarse al servidor en la dirección IP "localhost" o la IP del servidor o la maquina donde se aloje el server y puerto 5000
        // TODO en vez de conectarse automatico, poner la IP???
        socket = Socket("192.168.100.254", 5000)
        try {
            println("Conectado al servidor")

            // Crear flujos de entrada y salida
            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())

            // Mandamos nombre de usuario y contraseña al servidor
            val encryptedPassword = BCrypt.hashpw(password, salt)
            val message = "$username:$encryptedPassword"
            println(message)
            output.writeObject(message)
            output.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun register(user:String, username:String, password:String) {
        // Conectarse al servidor en la dirección IP "localhost" o la IP del servidor o la maquina donde se aloje el server y puerto 5000
        // TODO en vez de conectarse automatico, poner la IP???
        //socket = Socket("192.168.100.254", 5000)
        socket = Socket("192.168.146.245", 5000)
        try {
            println("Conectado al servidor")

            // Crear flujos de entrada y salida
            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())

            // Mandamos nombre de usuario y contraseña al servidor
            salt = BCrypt.gensalt(12)
            val encryptedPassword = BCrypt.hashpw(password,salt)
            println("Salt:" + salt)
            val message = "register:$user:$username:$encryptedPassword"
            println(message)
            output.writeObject(message)
            output.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Metodo para cerrar la conexion, siempre y cuando primero se haya establecido
    fun desconectar() {
        if (::socket.isInitialized) {
            socket.close()
            println("Conexión cerrada")
        } else {
            println("No se ha establecido conexión")
        }
    }
}