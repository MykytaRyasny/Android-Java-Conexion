package com.example.android_java.ui

import android.app.ProgressDialog
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
import com.example.iotproyect.databinding.FragmentSecondBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

  // Declaramos todos los elementos de nuestro Fragment
  private lateinit var et_Name: EditText
  private lateinit var et_Nickname: EditText
  private lateinit var et_Password: EditText
  private lateinit var bt_Aceptar: Button
  private lateinit var bt_Cancelar: Button
  private var _binding: FragmentSecondBinding? = null
  private lateinit var con: Conexion
  private lateinit var et_repeatPassword: EditText
  private lateinit var et_ip: EditText

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentSecondBinding.inflate(inflater, container, false)
    return binding.root

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    et_Name = view.findViewById(R.id.et_Name)
    et_Nickname = view.findViewById(R.id.et_Nickname)
    et_Password = view.findViewById(R.id.et_Password)
    bt_Aceptar = view.findViewById(R.id.bt_Aceptar)
    bt_Cancelar = view.findViewById(R.id.bt_Cancelar)
    et_repeatPassword = view.findViewById(R.id.et_repeatPassword)
    et_ip = view.findViewById(R.id.et_ip_register)

    bt_Cancelar.setOnClickListener { findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment) }
    bt_Aceptar.setOnClickListener {
      con = Conexion()
      if (et_Password.text.toString() != et_repeatPassword.text.toString()) {
        Toast.makeText(requireActivity(), R.string.password_mismatch, Toast.LENGTH_LONG)
          .show()
      } else if (Utiles.validarIP(et_ip.text.toString())) {
        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setMessage(getString(R.string.connecting))
        progressDialog.show()
        GlobalScope.launch(Dispatchers.IO) {
          val result = con.register(
            et_Name.text.toString(),
            et_Nickname.text.toString().lowercase(),
            et_Password.text.toString(),
            et_ip.text.toString()
          )
          withContext(Dispatchers.Main) {
            progressDialog.dismiss()
            if (result) {
              findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            } else {
              Toast.makeText(requireActivity(), R.string.user_exists, Toast.LENGTH_LONG)
                .show()
            }
          }
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