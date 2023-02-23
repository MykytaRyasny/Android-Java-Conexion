package com.example.android_java.server

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.DataOutputStream
import java.io.File
import java.io.InputStream
import java.security.Security
import java.util.regex.Pattern
import javax.crypto.Cipher


// Aqui voy a implementar un validador de IP y obtener la semilla de la pass
class Utiles {

  companion object {
    /**
     * Devuelve [Boolean] si [String] corresponde al pattern.
     */
    fun validarIP(ip: String): Boolean {
      val pattern =
        Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")
      return pattern.matcher(ip).matches()
    }

    /**
     * Devuelve [ByteArray] encriptado del [String].
     */
    fun encriptar(datos: String): ByteArray {
      Security.addProvider(BouncyCastleProvider())
      val cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC")
      cipher.init(Cipher.ENCRYPT_MODE, Conexion.clavePublicaServer)
      var test = cipher.doFinal(datos.toByteArray())
      println(test)
      return test
    }


    suspend fun mandarImagen(byteArrayList: MutableList<ByteArray>) {

      val out = Conexion.output
      val dout = DataOutputStream(out)

      var numero = byteArrayList.size
      var mensaje = "imagenes:${numero}"
      out.writeObject(encriptar(mensaje))
      out.flush()

      for (byteArray in byteArrayList) {
        dout.writeInt(byteArray.size)
        dout.write(byteArray)
        dout.flush()
      }
    }
  }

}