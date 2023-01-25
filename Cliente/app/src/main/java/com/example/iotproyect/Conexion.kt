package com.example.iotproyect

import java.io.*
import java.net.*

class Conexion {
    public fun conectar() {
        try {
            // Conectarse al servidor en la dirección IP "localhost" y puerto 5000
            val socket = Socket("192.168.100.24", 5000)
            println("Conectado al servidor")

            // Crear flujos de entrada y salida
            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())

            // Enviar un mensaje al servidor
            val message = "Hola Servidor"
            output.writeObject(message)
            output.flush()
            println("Mensaje enviado al servidor: $message")

            // Recibir la respuesta del servidor
            val response = input.readObject() as String
            println("Respuesta recibida del servidor: $response")

            // Cerrar la conexión
            socket.close()
            println("Conexión cerrada")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}