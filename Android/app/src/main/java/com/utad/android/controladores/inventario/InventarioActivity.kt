package com.utad.android.controladores.inventario

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.controladores.dialogs.Error
import com.utad.android.controladores.dialogs.SeleccionInventario
import com.utad.android.entitys.ArmadurasEntity
import com.utad.android.entitys.ArmasEntity
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityInventarioBinding
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch

class InventarioActivity : AppCompatActivity() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityInventarioBinding

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityInventarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewNombreUser.text = Mochila.user.username
        binding.textViewNivel.text = String.format("%d", Mochila.user.nivel)
        lifecycleScope.launch {
            cargarUser(Mochila.user.username, Mochila.user.pwd)
            binding.textViewNombreUser.text = Mochila.user.username
            val resourceId = resources.getIdentifier(
                "drawable/" + Mochila.inventario.personaje,
                null,
                packageName
            )
            binding.imageViewPersonaje.setImageResource(resourceId)
            comprobarSet()
            val set = Mochila.inventario.setActual.split("|")
            if (set != null) {
                if (set[0] != "null") {
                    var inv = arrayListOf(set[0])
                    val ejecutor = AsincronoEjecutorDeConexiones(
                        AsincronoEjecutorDeConexiones.METODOS.GET_ARMAS,
                        inv
                    )
                    val ret = ejecutor.execute() as List<ArmasEntity>
                    val resourceId =
                        resources.getIdentifier("drawable/" + ret[0].imagen, null, packageName)
                    binding.logoArma.setImageResource(resourceId)
                    val resourceId2 =
                        resources.getIdentifier("drawable/"+ ret[0].rareza.lowercase() , null, packageName)
                    binding.logoArma.setBackgroundResource(resourceId2)
                }
                if (set[1] != "null") {
                    var inv = arrayListOf(set[1])
                    val ejecutor = AsincronoEjecutorDeConexiones(
                        AsincronoEjecutorDeConexiones.METODOS.GET_ARMADURAS,
                        inv
                    )
                    val ret = ejecutor.execute() as List<ArmadurasEntity>
                    val resourceId =
                        resources.getIdentifier("drawable/" + ret[0].imagen, null, packageName)
                    binding.logoArmaduras.setImageResource(resourceId)
                    val resourceId2 =
                        resources.getIdentifier("drawable/"+ ret[0].rareza.lowercase() , null, packageName)
                    binding.logoArmaduras.setBackgroundResource(resourceId2)
                }
            }
        }
        binding.logoArma.setOnClickListener {
            lifecycleScope.launch {
                val armas = getArmasDInventario()
                if (armas != null) {
                    SeleccionInventario.showArmas(
                        this@InventarioActivity,
                        this@InventarioActivity,
                        armas
                    )
                } else {
                    Error.showError(
                        this@InventarioActivity,
                        "No tienes Armas compra alguna en la tienda...."
                    )
                }
            }
        }
        binding.logoArmaduras.setOnClickListener {
            lifecycleScope.launch {
                val armaduras = getArmadurasDInventario()
                if (armaduras != null) {
                    SeleccionInventario.showArmaduras(
                        this@InventarioActivity,
                        this@InventarioActivity,
                        armaduras
                    )
                }
            }
        }
        binding.imageViewUsuario.setOnClickListener {
            val intent = Intent(this@InventarioActivity, AjustesUsuario::class.java)
            startActivity(intent)
        }
        binding.buttonReturn.setOnClickListener {
          //  val intent = Intent(this@InventarioActivity, Inicio_Personaje::class.java)
          // startActivity(intent)
            finish()
        }
        binding.imageViewEditPers.setOnClickListener {
            val intent = Intent(this@InventarioActivity, CambiarPersonaje::class.java)
            startActivity(intent)
        }

    }

    private suspend fun getArmadurasDInventario(): List<ArmadurasEntity>? {
        var InvetarioF: List<ArmadurasEntity>? = null
        if (Mochila.inventario.armaduras != null && !Mochila.inventario.armaduras.equals("")) {
            var inv = Mochila.inventario.armaduras.split("|")
            val ejecutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.GET_ARMADURAS,
                inv
            )
            InvetarioF = ejecutor.execute() as List<ArmadurasEntity>?

        } else {
            Error.showError(this, "No tienes Armaduras, Compra algunas en la tienda")
        }
        return InvetarioF
    }

    private suspend fun getArmasDInventario(): List<ArmasEntity>? {
        var InvetarioF: List<ArmasEntity>? = null
        if (Mochila.inventario.armas != null && !Mochila.inventario.armas.equals("")) {
            var inv = Mochila.inventario.armas.split("|")
            val ejecutor =
                AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.GET_ARMAS, inv)
            InvetarioF = ejecutor.execute() as List<ArmasEntity>
        } else {
            Error.showError(this, "No tienes Armas, Compra algunas en la tienda")
        }
        return InvetarioF

    }

    private suspend fun cargarUser(username: String, pwd: String) {
        val args = arrayOf(username, pwd)
        val ejecutor =
            AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
        val user = (ejecutor.execute() as? UsuarioJSON)!!;
        val ejecutor2 = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO,
            user.id
        )
        val inventario = ejecutor2.execute() as InventariosEntity
        UtilidadesJSON.GuardarUser(applicationContext, user.toUsersEntity())
        Mochila.user = user
        Mochila.inventario = inventario
    }

    private suspend fun comprobarSet() {
        if (Mochila.inventario.setActual == null || Mochila.inventario.setActual == "") {
            val preejcutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO,
                Mochila.user.id
            )
            Mochila.inventario = preejcutor.execute() as InventariosEntity?
            val args = arrayOf(String.format("%d", Mochila.inventario.id), "setactual", "null|null")
            val ejecutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.MODIFY_INVENTARIO,
                args
            )
            Mochila.inventario = ejecutor.execute() as InventariosEntity?
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}