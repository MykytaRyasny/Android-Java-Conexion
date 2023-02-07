package com.example.android_java.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_java.server.Conexion
import com.example.android_java.server.Utiles
import com.example.iotproyect.R
import com.example.iotproyect.databinding.FragmentFirstBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    // Declaramos todos los elementos de FirstFragment
    private lateinit var bt_register: Button
    private lateinit var bt_entrar: Button
    private lateinit var con: Conexion
    private lateinit var et_nickname: EditText
    private lateinit var et_password: EditText
    private lateinit var et_ip: EditText

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Asociamos ese boton al de Registrarse para or al segundo Fragmen
        bt_register = view.findViewById(R.id.bt_register)
        bt_register.setOnClickListener { findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment) }

        // Identificamos ambos campos de texto para sacar los datos y mandarlos al servidor
        et_nickname = view.findViewById(R.id.et_nickname)
        et_password = view.findViewById(R.id.et_password)
        et_ip = view.findViewById(R.id.et_ip_login)

        // usamos el boto de entrar con lambda para mandar el nich y la pass
        bt_entrar = view.findViewById(R.id.bt_enter)
        bt_entrar.setOnClickListener {
            con = Conexion()
            if (Utiles.validarIP(et_ip.text.toString())) {
                GlobalScope.launch(Dispatchers.IO) {
                    con.login(
                        et_nickname.text.toString().lowercase(),
                        et_password.text.toString(),
                        et_ip.text.toString()
                    )
                }
            } else {
                Toast.makeText(requireActivity(), R.string.ip_mismatch, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}