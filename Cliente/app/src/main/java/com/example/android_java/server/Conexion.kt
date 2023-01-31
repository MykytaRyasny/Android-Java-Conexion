package com.example.android_java.server

import org.mindrot.jbcrypt.BCrypt
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

// Clase con metodos para conectarse y desconectarse del servidor
class Conexion() {

    private lateinit var socket: Socket

    // Metodo conectar para conectar al servidor
    fun login(username: String, password: String, ip: String) {
        // Conectarse al servidor en la dirección IP "localhost" o la IP del servidor o la maquina donde se aloje el server y puerto 5000
        // Ahora la IP se pasa por el metodo desde un TextBox
        socket = Socket(ip, 5000)
        try {
            println("Conectado al servidor")

            // Crear flujos de entrada y salida
            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())
            // Leer el mensaje del servidor
            val serverMessage = input.readObject() as String
            val file = File("salt-$username.txt")
            val salt = file.readText()
            // Mandamos nombre de usuario y contraseña al servidor
            val encryptedPassword = BCrypt.hashpw(password, salt)
            val message = "$username:$encryptedPassword"
            output.writeObject(message)
            output.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun register(user: String, username: String, password: String, ip: String) {
        // Conectarse al servidor en la dirección IP "localhost" o la IP del servidor o la maquina donde se aloje el server y puerto 5000
        // Ahora la IP se pasa por el metodo desde un TextBox
        socket = Socket(ip, 5000)

        try {
            println("Conectado al servidor")

            // Crear flujos de entrada y salida
            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())

            // Mandamos nombre de usuario y contraseña al servidor
            var salt = BCrypt.gensalt(12)
            val encryptedPassword = BCrypt.hashpw(password, salt)
            val message = "register:$user:$username:$encryptedPassword"
            output.writeObject(message)
            output.flush()

            // Escribir el salt en un archivo de texto con el nombre de usuario correspondiente
            val file = File("salt-$username.txt")
            file.writeText(salt)

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
            println("No hay conexión para cerrar")
        }
    }
}