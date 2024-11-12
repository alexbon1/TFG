package com.utad.android.controladores.inicio

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.utad.android.R
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Historia
import com.utad.android.entitys.UsersEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.dialogs.TutorialDialog
import com.utad.android.databinding.ActivityCreadorPersonajeBinding
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch

class CreadorPersonaje : AppCompatActivity() {
    val NOMBREVISTA = "CreadorPersonaje";
    private lateinit var binding: ActivityCreadorPersonajeBinding

    var tipo = ""
    var personaje="p001"
    lateinit var personajes: Array<Drawable?>
    var contPersonajes = 0

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreadorPersonajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Cargar las imágenes de los personajes en un array
        personajes = arrayOf(
            getDrawable(R.drawable.p001),
            getDrawable(R.drawable.p002),
            getDrawable(R.drawable.p003),
            getDrawable(R.drawable.p004),
            getDrawable(R.drawable.p005),
            getDrawable(R.drawable.p006),
            getDrawable(R.drawable.p007),
            getDrawable(R.drawable.p008),
            getDrawable(R.drawable.p009),
            getDrawable(R.drawable.p010),
            getDrawable(R.drawable.p011),
            getDrawable(R.drawable.p012),
            getDrawable(R.drawable.p013),
            getDrawable(R.drawable.p014),
            getDrawable(R.drawable.p015),
            getDrawable(R.drawable.p016),
            getDrawable(R.drawable.p017),
            getDrawable(R.drawable.p018),
            getDrawable(R.drawable.p019),
            getDrawable(R.drawable.p020),
            getDrawable(R.drawable.p021),
            getDrawable(R.drawable.p022),

        )

        // Mostrar el primer personaje
        binding.imageViewCreadorPersonaje.setImageDrawable(personajes[contPersonajes])

        // Manejar los clics de los botones para cambiar de personaje
        binding.buttonPasarDrchPers.setOnClickListener {
            if (++contPersonajes >= personajes.size) {
                contPersonajes = 0
            }
            binding.imageViewCreadorPersonaje.setImageDrawable(personajes[contPersonajes])
            actualizarTextoTipo()
            val resourceName = "p${String.format("%03d", contPersonajes + 1)}" // Generar el nombre del recurso
            val resourceId = resources.getIdentifier(resourceName, "drawable", packageName)
            if (resourceId != 0) {
                val nombreDrawable = resources.getResourceEntryName(resourceId)
                personaje=nombreDrawable
            }
        }
        binding.buttonPasariIzqPers.setOnClickListener {
            if (--contPersonajes < 0) {
                contPersonajes = personajes.size - 1
            }
            binding.imageViewCreadorPersonaje.setImageDrawable(personajes[contPersonajes])
            actualizarTextoTipo()
            val resourceName = "p${String.format("%03d", contPersonajes + 1)}" // Generar el nombre del recurso
            val resourceId = resources.getIdentifier(resourceName, "drawable", packageName)
            if (resourceId != 0) {
                val nombreDrawable = resources.getResourceEntryName(resourceId)
                personaje=nombreDrawable
            }
        }

        // Actualizar el texto del tipo inicialmente
        actualizarTextoTipo()



        binding.buttonCrear.setOnClickListener {
            lifecycleScope.launch {
                try {
                    var user = UtilidadesJSON.obtenerUser(applicationContext)
                    var args = arrayOf(user.username,user.pwd)
                    var ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)

                    var user2 = ejecutor.execute() as UsuarioJSON
                    UtilidadesJSON.GuardarUser(applicationContext, user2.toUsersEntity())
                    val tipoModificado = guardarCambios()
                  if(tipoModificado){
                      if (!isFinishing) {
                          user = UtilidadesJSON.obtenerUser(applicationContext)
                          args = arrayOf(user.username,user.pwd)
                          ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)

                          val user2 = ejecutor.execute() as UsuarioJSON
                          UtilidadesJSON.GuardarUser(applicationContext, user2.toUsersEntity())
                          val intent = Intent(this@CreadorPersonaje, Inicio_Personaje::class.java)
                          startActivity(intent)


                      }
                  }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.imageViewCreadorPersonaje.setOnClickListener{
            lifecycleScope.launch {
                Historia.showHistoria(this@CreadorPersonaje,personaje)
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
    private fun actualizarTextoTipo() {
        // Actualizar el texto según si el número de personaje es impar o par
        if ((contPersonajes + 1) % 2 == 0) {
            binding.textView3.text = "Mago"
            tipo="Mago"
        } else {
            binding.textView3.text = "Guerrero"
            tipo="Guerrero"
        }
    }
    private suspend fun guardarCambios(): Boolean{
        val user = UtilidadesJSON.obtenerUser(applicationContext)
        val args1 = arrayOf(user.id.toString(),"personaje",personaje,"tipo",tipo)
        val ejecutor1 = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.ADD_PERSONAJE_TIPO, args1)
        val res = ejecutor1.execute()
        return res as Boolean
    }
}
