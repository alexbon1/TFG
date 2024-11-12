package com.utad.android.controladores

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.utad.android.controladores.batallas.EsperaBatallasActivity
import com.utad.android.controladores.dialogs.Historia
import com.utad.android.controladores.inventario.InventarioActivity
import com.utad.android.controladores.misiones.MainMisiones
import com.utad.android.controladores.tienda.Tienda
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityInicioPersonajeBinding
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch

class Inicio_Personaje : AppCompatActivity() {

    private lateinit var binding: ActivityInicioPersonajeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityInicioPersonajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userTmp = UtilidadesJSON.obtenerUser(applicationContext);

        lifecycleScope.launch {
            cargarUser(userTmp.username,userTmp.pwd)
            binding.textViewNombreUser.text= Mochila.user.username
            val resourceId = resources.getIdentifier("drawable/" + Mochila.inventario.personaje, null, packageName)
            binding.textViewNombreUser.text= Mochila.user.username
            binding.textViewNivel.text = String.format("%d", Mochila.user.nivel)
            binding.imageViewPersonaje.setImageResource(resourceId)
            binding.textViewMonedas.text=Mochila.user.monedas.toString()
        }
        binding.imageViewPersonaje.setOnClickListener{
            lifecycleScope.launch {
                Historia.showHistoria(this@Inicio_Personaje,Mochila.inventario.personaje)
            }
        }
        binding.imageViewMisiones.setOnClickListener {
            val intent = Intent(this@Inicio_Personaje, MainMisiones::class.java)
            startActivity(intent)
        }
        binding.imagenArmas.setOnClickListener {
            val intent = Intent(this@Inicio_Personaje, InventarioActivity::class.java)
            startActivity(intent)
        }
        binding.imagenVS.setOnClickListener {
            val intent = Intent(this@Inicio_Personaje, EsperaBatallasActivity::class.java)
            startActivity(intent)
        }
        binding.imageView3.setOnClickListener {
            val intent = Intent(this@Inicio_Personaje, Tienda::class.java)
            startActivity(intent)
        }
        binding.textViewNivel.setOnClickListener{
            val intent = Intent(this@Inicio_Personaje, ConsultarExperiencia::class.java)
            startActivity(intent)
        }
    }

    private suspend fun cargarUser(username: String, pwd: String) {
        val args = arrayOf(username, pwd)
        val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
        val user = (ejecutor.execute() as? UsuarioJSON)!!;
        val ejecutor2 = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO, user.id)
        val inventario = ejecutor2.execute() as InventariosEntity
        UtilidadesJSON.GuardarUser(applicationContext,user.toUsersEntity())
        Mochila.user=user
        Mochila.inventario=inventario
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}