package com.example.android_java.server

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.concurrent.TimeUnit

// Clase con metodos para conectarse y desconectarse del servidor
class Conexion() {

    private lateinit var socket: Socket

    private lateinit var output: ObjectOutputStream
    private lateinit var input: ObjectInputStream

    private lateinit var salt: String

    // He sacado el metodo por que lo uso amenudo y englobo los input y output para el mismo socket
    fun conectar(ip: String) {
        socket = Socket(ip, 5000)
        output = ObjectOutputStream(socket.getOutputStream())
        input = ObjectInputStream(socket.getInputStream())
        println("Nos hemos conectado al servidor")
    }

    // Vamos a obtener el hash de nuestro usuario desde el servidor
    // Creamos una funcion que devuelve String (salt) como mensaje obtenido del servidor
    fun obtenerSalt(username: String, ip: String): String {
        conectar(ip)
        println("Recuperando el salt")
        // Creamos flujo de entrda y salida para mandar usuario y recibir hash
        val message = "hash:$username"
        output.writeObject(message)
        output.flush()
        // TODO borrar
        return input.readObject() as String
    }

    // Metodo conectar para conectar al servidor
    suspend fun login(username: String, password: String, ip: String) {
        salt = obtenerSalt(username, ip)
        println("Hola")
        // Mandamos nombre de usuario y contraseña al servidor
        val encryptedPassword = BCrypt.hashpw(password, salt)
        val message = "login:$username:$encryptedPassword"
        println(message)
        //conectar(ip)
        output.writeObject(message)
        output.flush()
        println(input.readObject() as String)
    }


    fun register(user: String, username: String, password: String, ip: String) {
        // Conectarse al servidor en la dirección IP "localhost" o la IP del servidor o la maquina donde se aloje el server y puerto 5000
        // Ahora la IP se pasa por el metodo desde un TextBox
        conectar(ip)
        try {
            println("Conectado al servidor")

            // Mandamos nombre, nombre de usuario y contraseña al servidor
            var salt = BCrypt.gensalt(12)
            val encryptedPassword = BCrypt.hashpw(password, salt)
            val message = "register:$user:$username:$encryptedPassword"
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
            println("No hay conexión para cerrar")
        }
    }
}