package com.utad.android.controladores.misiones

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.databinding.ActivityMisionesDiariasBinding
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.MisionesDiariasJSON
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MisionesDiarias : AppCompatActivity() {

    private lateinit var binding: ActivityMisionesDiariasBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityMisionesDiariasBinding.inflate(layoutInflater)
        var tipo = "";
        binding.imageViewMision.isEnabled = false
        lifecycleScope.launch {
            if (intent.getBooleanExtra("NORMAL", true)) {
                val mision: MisionesDiariasJSON? = cargarMisionBase()
                Mochila.misionActual = mision
                cargarUser(Mochila.user.username, Mochila.user.pwd)
                if (mision != null) {
                    tipo = mision.tipoMision.toString()
                };
                if (mision != null) {
                    binding.TextViewTitle.text = mision.nombre
                    binding.TextViewInfo.text = mision.descripcion
                    val recompensa =
                        "Recompensa: " + mision.recompensa + "Gladios " + mision.getxp() + "XP"
                    binding.textViewTitle2.text = recompensa
                }
            } else {
                val mision: MisionesDiariasJSON? = cargarMisionExtra()
                Mochila.misionActual = mision
                if (mision != null) {
                    tipo = mision.tipoMision.toString()
                };
                if (mision != null) {
                    binding.TextViewTitle.text = mision.nombre
                    binding.TextViewInfo.text = mision.descripcion
                    val recompensa =
                        "Recompensa: " + mision.recompensa + "Gladios " + mision.getxp() + "XP"
                    binding.textViewTitle2.text = recompensa
                }
            }
        }
        binding.imageViewMision.setOnClickListener {

            if (tipo == "GPS+CRONO") {
                val intent = Intent(this@MisionesDiarias, MisionCorrer::class.java)
                startActivity(intent)
            } else if (tipo == "CRONO") {
                val intent = Intent(this@MisionesDiarias, MisionCrono::class.java)
                startActivity(intent)
            } else if (tipo == "INFO+TEST" || tipo == "TEST") {
                val intent = Intent(this@MisionesDiarias, MisionTest::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@MisionesDiarias, Inicio_Personaje::class.java)
                startActivity(intent)
            }

        }
        binding.buttonReturn.setOnClickListener {
            finish()
        }

    }

    private suspend fun cargarMisionExtra(): MisionesDiariasJSON? {
        if (Mochila.user.monedas >= 500) {
            val ejecutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.MISION_EXTRA, ""
            )
            val ret = ejecutor.execute()
            if (ret == null) {
                Error.showError(this, "Error de conexion vuelva a intentarlo mas tarde")
                delay(5000)
                finish()
            }
            setContentView(binding.root)
            binding.imageViewMision.isEnabled=true
            return ret as MisionesDiariasJSON?
        } else {
            val intent = Intent(this@MisionesDiarias, NoTienesMision::class.java)
            intent.putExtra("MSG", "No tienes un duro....")
            startActivity(intent)
            return null
        }
    }

    private suspend fun cargarUser(username: String, pwd: String) {
        val args = arrayOf(username, pwd)
        val ejecutor =
            AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
        val user = (ejecutor.execute() as? UsuarioJSON)!!;
        val ejecutor2 = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO, user.id
        )
        val inventario = ejecutor2.execute() as InventariosEntity
        UtilidadesJSON.GuardarUser(applicationContext, user.toUsersEntity())
        Mochila.user = user
        Mochila.inventario = inventario
        if (Mochila.user.cantidadMisiones == 1) {
            val intent = Intent(this@MisionesDiarias, NoTienesMision::class.java)
            intent.putExtra("MSG", "Ya has completado tus misiones de hoy....")
            startActivity(intent)
        } else {
            setContentView(binding.root)
            binding.imageViewMision.isEnabled = true
        }
    }

    private suspend fun cargarMisionBase(): MisionesDiariasJSON? {
        val ejecutor = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.MISION_DIARIA_BASE, ""
        )
        val ret = ejecutor.execute()
        if (ret == null) {
            Error.showError(this, "Error de conexion vuelva a intentarlo mas tarde")
            delay(5000)
            finish()
        }
        return ret as MisionesDiariasJSON?
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}