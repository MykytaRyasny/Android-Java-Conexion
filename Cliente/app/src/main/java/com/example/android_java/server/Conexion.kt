package com.example.android_java.server

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.security.PublicKey
import javax.crypto.Cipher
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

// Clase con metodos para conectarse y desconectarse del servidor
class Conexion() {

    private lateinit var socket: Socket
    private lateinit var clavePublicaServer:PublicKey

    private lateinit var output: ObjectOutputStream
    private lateinit var input: ObjectInputStream

    // He sacado el metodo por que lo uso amenudo y englobo los input y output para el mismo socket
    fun conectar(ip: String){
        socket = Socket(ip, 5000)
        output = ObjectOutputStream(socket.getOutputStream())
        input = ObjectInputStream(socket.getInputStream())
        println("Nos hemos conectado al servidor")
        clavePublicaServer = input.readObject() as PublicKey
        println("SE ha recibido la clave: " + clavePublicaServer)
    }

    fun encriptar(datos:String): ByteArray{
        Security.addProvider(BouncyCastleProvider())
        val cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC")
        cipher.init(Cipher.ENCRYPT_MODE, clavePublicaServer)
        var test = cipher.doFinal(datos.toByteArray())
        println(test)
        return test
    }

    // Metodo conectar para conectar al servidor
    suspend fun login(username: String, password: String, ip: String) {
        conectar(ip)
        val mensaje = "login:$username:$password"
        var mensajeEncriptado = encriptar(mensaje)
        output.writeObject(mensajeEncriptado)
        output.flush()
        println(input.readObject() as String)
    }

    fun register(user: String, username: String, password: String, ip: String) {
        conectar(ip)
        try {
            println("Intentando registrarse")
            // Mandamos nombre, nombre de usuario y contraseña al servidor encriptados
            // Encripto todo_ el mensaje con RSA
            var mensaje = "register:$user:$username:$password"
            var mensajeEncriptado = encriptar(mensaje)
            output.writeObject(mensajeEncriptado)
            output.flush()
            println("Mensaje mandado")
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