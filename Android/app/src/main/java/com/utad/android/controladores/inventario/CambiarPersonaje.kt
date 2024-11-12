package com.utad.android.controladores.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.utad.android.controladores.inicio.CreadorPersonaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.entitys.PersonajesEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityCambiarPersonajeBinding
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch

class CambiarPersonaje : AppCompatActivity() {
    private lateinit var binding: ActivityCambiarPersonajeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityCambiarPersonajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            var personaje=cargarPersonaje(Mochila.inventario.personaje);
            if (personaje != null) {
                binding.textView2.text="Parace ser que " + personaje.nombre + " te ha cogido cariño, vas a abandonarlo cruelmente a su suerte?"
            }else {
                binding.textView2.text="Parace ser que tu campeón te ha cogido cariño, vas a abandonarlo cruelmente a su suerte?"
            }
        }
        binding.buttonSalir.setOnClickListener {
            finish()
        }
        binding.buttonCambiar.setOnClickListener {
            if (Mochila.user.monedas>=1000){
                lifecycleScope.launch {
                    var ejecutor= AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.MODIFICAR_MONEDAS,1000)
                    if (ejecutor.execute() as Boolean){
                        val intent = Intent(this@CambiarPersonaje, CreadorPersonaje::class.java)
                        UtilidadesJSON.GuardarUser(applicationContext,Mochila.user.toUsersEntity())
                        startActivity(intent)
                    }else{
                        Error.showError(this@CambiarPersonaje,"Ha ocurrido un error Vuelva a intentarlo mas tarde")
                    }

                }

            }else{
                Error.showError(this,"No tienes Gladios suficientes")
            }
        }
    }
    private suspend fun cargarPersonaje(img: String): PersonajesEntity? {
        val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.CONSULTAR_PERSONAJE_IMAGEN,img)
        val result=(ejecutor.execute() as? PersonajesEntity)
        return result
    }
}