package com.utad.android.controladores.inicio
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error.Companion.showError
import com.utad.android.entitys.UsersEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityLoginBinding
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    val NOMBREVISTA = "Login";
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UsuarioJSON
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            // Inicia la corrutina en el hilo principal
            lifecycleScope.launch {
                if (tryLogin()) {
                    if (!isFinishing) {
                        val intent:Intent?
                        if (user?.tipo!=null &&  user?.tipo!=""){
                             intent = Intent(this@Login, Inicio_Personaje::class.java)
                        }else{
                             intent = Intent(this@Login, CreadorPersonaje::class.java)
                        }
                        intent.putExtra("origen", NOMBREVISTA)
                        startActivity(intent)
                    }
                }
            }
        }





        binding.buttonReturn.setOnClickListener {
            finish()
        }
    }

    private suspend fun tryLogin(): Boolean {
        if (binding.editTextUser.text.toString().isNotEmpty() && binding.editTextPassword.text.toString().isNotEmpty()) {
            val args = arrayOf(binding.editTextUser.text.toString(), binding.editTextPassword.text.toString())

            val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)

            val ret = ejecutor.execute() as? UsuarioJSON
            if (ret != null) {
                user=ret
                UtilidadesJSON.GuardarUser(applicationContext, user.toUsersEntity())
                return true
            } else {
                showError(this,"Usuario o contraseña incorrectos")
            }
        } else {
            showError(this,"Rellena todos los campos")
        }
        return false
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}
