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

    // Metodo conectar para conectar al servidor
    // Cuando hacemos login podemos seguir sufriendo el ataque de man in the middle en este diseño
    // Tendriamos que usar AES para cifrar el envio de datos y descifrarlo en el servidor
    // Podriamos usar el salt mismo como mensaje para cifrar y descifrar
    // TODO implementar AES
    suspend fun login(username: String, password: String, ip: String) {
        conectar(ip)
        val message = "login:$username:$password"
        println(message)
        //conectar(ip)
        output.writeObject(message)
        output.flush()
        println(input.readObject() as String)
    }

    /*fun obtenerSalt(username: String, ip: String): String {
        conectar(ip)
        println("Recuperando el salt")
        // Creamos flujo de entrda y salida para mandar usuario y recibir hash
        val message = "hash:$username"
        output.writeObject(message)
        output.flush()
        return input.readObject() as String
    }*/

    fun register(user: String, username: String, password: String, ip: String) {
        conectar(ip)
        try {
            println("Intentando registrarse")

            // Mandamos nombre, nombre de usuario y contraseña al servidor
            var salt = BCrypt.gensalt(12)
            // Encripto la contraseña para poder guardarla de foram segura en el servidor
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