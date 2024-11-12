package com.utad.android.controladores.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.android.controladores.inicio.MainActivity
import com.utad.android.controladores.inicio.SignIn
import com.utad.android.controladores.dialogs.DialogChangePassword
import com.utad.android.databinding.ActivityAjustesUsuarioBinding
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON

class AjustesUsuario : AppCompatActivity() {
    private lateinit var binding: ActivityAjustesUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjustesUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCambiarDatos.setOnClickListener {
            val intent = Intent(this@AjustesUsuario, SignIn::class.java)
            intent.putExtra("origen", NOMBREVISTA)
            startActivity(intent)
        }
        binding.buttonCambiarContra.setOnClickListener{
            DialogChangePassword.show(this,this)
        }
        binding.buttonReturn.setOnClickListener {
            finish()
        }
        binding.buttonSalir.setOnClickListener {
            Mochila.user=null
            Mochila.inventario=null
            Mochila.personaje=null
            UtilidadesJSON.borrarUser(applicationContext)
            val intent = Intent(this@AjustesUsuario, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val NOMBREVISTA = "AJUSTESUSUARIO";
    }
}