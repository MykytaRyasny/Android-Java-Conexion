package com.example.android_java.server

import java.util.regex.Pattern

// Aqui voy a implementar un validador de IP y obtener la semilla de la pass
class Utiles {
    companion object {
        fun validarIP(ip: String): Boolean {
            val pattern =
                Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")
            return pattern.matcher(ip).matches()
        }
    }
}