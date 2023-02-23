package com.example.android_java.server

import android.widget.Toast
import com.example.iotproyect.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException
import java.security.PublicKey

// Clase con metodos para conectarse y desconectarse del servidor
class Conexion() {
  companion object {
    lateinit var clavePublicaServer: PublicKey
    lateinit var output: ObjectOutputStream
    lateinit var input: ObjectInputStream
  }

  private lateinit var socket: Socket

  /**
   * Se intenta conectar a la ip [String] dado
   * Se crean canales de comunicacion InputStream y OutputStream
   * Tambien se obtiene la clave publica
   */
  @Throws(Exception::class)
  fun conectar(ip: String) {
      socket = Socket(ip, 5000)
      output = ObjectOutputStream(socket.getOutputStream())
      input = ObjectInputStream(socket.getInputStream())
      clavePublicaServer = input.readObject() as PublicKey
  }

  /**
   * Se hace login en el servidor, se pasa
   * username [String] El nombre de usuario
   * password [String] La contraseña del usuario
   * ip [String] La IP del servidor
   * Los datos al enviarse se cifran
   * Devuelve [Boolean] si el login ha sido exitoso o no
   */
  fun login(username: String, password: String, ip: String): Boolean {
    conectar(ip)
    val mensaje = "login:$username:$password"
    val mensajeEncriptado = Utiles.encriptar(mensaje)
    output.writeObject(mensajeEncriptado)
    output.flush()
    return input.readObject() as Boolean
  }

  /**
   * Se hace register en el servidor, se pasa
   * user [String] Nombre propio
   * username [String] El nombre de usuario
   * password [String] La contraseña del usuario
   * ip [String] La IP del servidor
   * Los datos al enviarse se cifran
   * Devuelve [Boolean] si el registro ha sido exitoso o no
   */
  fun register(user: String, username: String, password: String, ip: String): Boolean {
    conectar(ip)
    try {
      val mensaje = "register:$user:$username:$password"
      val mensajeEncriptado = Utiles.encriptar(mensaje)
      output.writeObject(mensajeEncriptado)
      output.flush()
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return input.readObject() as Boolean
  }

  /**
   * Cerramos la conexion comprobando que esta inicializada
   */
  fun desconectar() {
    if (!socket.isClosed) {
      val mensaje = "desconectar"
      output.writeObject(Utiles.encriptar(mensaje))
      output.flush()
      output.close()
      input.close()
      socket.close()
    } else {
      //Ignore
    }
  }
}