package com.example.android_java.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.iotproyect.R
import com.example.iotproyect.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var desconectar:MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        desconectar = menu.findItem(R.id.desconectar)
        desconectar.isEnabled = false
        desconectar.setOnMenuItemClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                FirstFragment.con.desconectar()
                withContext(Dispatchers.Main){
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.global_FirstFragment)
                }
            }
            desconectar.isEnabled = false
            return@setOnMenuItemClickListener true
        }
        return true
    }

    override fun onBackPressed() {
        /**
         * Hacemos que si estamos en FirstFragment que la app se cierre en vez de volver
         * a cualquier otro Fragment
         */
        if (javaClass.simpleName == FirstFragment::class.java.name) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Habilitamos nuestro boton de desconectar
     */
    fun habilitarDesconectar(){
        desconectar.isEnabled = true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.desconectar -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}