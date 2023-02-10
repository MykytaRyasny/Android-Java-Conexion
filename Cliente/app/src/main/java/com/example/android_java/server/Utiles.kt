package com.example.android_java.server

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.PublicKey
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
        fun encriptar(datos:String): ByteArray{
            Security.addProvider(BouncyCastleProvider())
            val cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC")
            cipher.init(Cipher.ENCRYPT_MODE, Conexion.clavePublicaServer)
            var test = cipher.doFinal(datos.toByteArray())
            println(test)
            return test
        }
    }


}