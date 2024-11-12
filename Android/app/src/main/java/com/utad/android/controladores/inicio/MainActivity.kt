package com.utad.android.controladores.inicio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.controladores.dialogs.TutorialDialog
import com.utad.android.databinding.ActivityMainBinding
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            var ejecutorTk =
                AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.TOKEN, "")
            ejecutorTk.execute()

            if (UtilidadesJSON.isUsuarioAbierto(applicationContext)) {
                val userIni = UtilidadesJSON.obtenerUser(applicationContext)
                val args = arrayOf(userIni.username, userIni.pwd)
                var ejecutor =
                    AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
                val intent: Intent?
                val user: UsuarioJSON? = ejecutor.execute() as UsuarioJSON?
                if (user != null) {
                    Mochila.user = user
                    UtilidadesJSON.GuardarUser(applicationContext, user.toUsersEntity())
                    intent = if (user?.tipo != null && user?.tipo != "") {
                        Intent(this@MainActivity, Inicio_Personaje::class.java)
                    } else {
                        Intent(this@MainActivity, CreadorPersonaje::class.java)
                    }
                    startActivity(intent)
                } else {
                    Error.showError(this@MainActivity, "User o contraseña incorrecta")
                    UtilidadesJSON.borrarUser(applicationContext)
                }
            }
        }
        setContentView(binding.root)
        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
        }
        binding.buttonSign.setOnClickListener {
            val intent = Intent(this@MainActivity, SignIn::class.java)
            intent.putExtra("origen", NOMBREVISTA)
            startActivity(intent)
        }
        binding.buttonTutorial.setOnClickListener {
            val tutorialDialog = TutorialDialog(this)
            tutorialDialog.show()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }

    companion object {
        const val NOMBREVISTA = "MainActivity";
    }
}