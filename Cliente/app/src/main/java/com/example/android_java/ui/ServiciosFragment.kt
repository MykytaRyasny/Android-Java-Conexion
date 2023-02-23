package com.example.android_java.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_java.server.Utiles.Companion.mandarImagen
import com.example.iotproyect.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServiciosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ServiciosFragment : Fragment() {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null

  private lateinit var btSalir: Button
  private lateinit var btImagenes: Button
  private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Con esta funcion deshabilitamos la flecha para ir hacia atras
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
    pickMultipleMedia = registerForActivityResult(
      ActivityResultContracts.PickMultipleVisualMedia(
        5
      )
    ) { uris ->
      GlobalScope.launch(Dispatchers.IO) {
        val byteArrayList = mutableListOf<ByteArray>()
        if (uris.isNotEmpty()) {
          for (uri in uris) {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            byteArrayList.add(inputStream?.readBytes() ?: byteArrayOf())
          }
        } else {
          Log.d("PhotoPicker", "No media selected")
        }
        mandarImagen(byteArrayList)
        }
      }
    }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (activity as AppCompatActivity).supportActionBar?.setTitle("Bienvenido")
    if (javaClass.simpleName == ServiciosFragment::class.java.name) {
      requireActivity().onBackPressedDispatcher.addCallback {
        GlobalScope.launch(Dispatchers.IO) {
          FirstFragment.con.desconectar()
          withContext(Dispatchers.Main) {
            findNavController().navigate(R.id.action_serviciosFragment_to_FirstFragment)
          }
        }
      }
    }
    btImagenes = view.findViewById(R.id.btVisorDeImagenes)
    btImagenes.setOnClickListener {
      pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    btSalir = view.findViewById(R.id.btSalir)
    btSalir.setOnClickListener {
      GlobalScope.launch(Dispatchers.IO) {
        FirstFragment.con.desconectar()
        withContext(Dispatchers.Main) {
          findNavController().navigate(R.id.action_serviciosFragment_to_FirstFragment)
        }
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {


    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_servicios, container, false)
  }


  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiciosFragment.
     */

    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) = ServiciosFragment().apply {
      arguments = Bundle().apply {
        putString(ARG_PARAM1, param1)
        putString(ARG_PARAM2, param2)
      }
    }
  }
}