package com.utad.android.controladores.tienda

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.dialogs.Error
import com.utad.android.databinding.ActivityTiendaBinding
import com.utad.android.entitys.ArmadurasEntity
import com.utad.android.entitys.ArmasEntity
import com.utad.android.entitys.JSON.TiendaDiaJSON
import com.utad.android.storage.Mochila
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class Tienda : AppCompatActivity() {
    private lateinit var binding: ActivityTiendaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityTiendaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCountdown()
        binding.textViewMonedas.text = String.format("%d", Mochila.user.monedas)
        lifecycleScope.launch {
            cargarTiendaDia()
            var i = 1
            for (armasEntity in tienda.armas) {
                cambiarItem(i, armasEntity)
                i++
            }
            for (armadura in tienda.armaduras) {
                cambiarItem(i, armadura)
                i++
            }
        }
        binding.boxBarata.setOnClickListener {
            val intent = Intent(this@Tienda, Comprar::class.java)
            intent.putExtra("Que", "barata")
            startActivity(intent)
        }
        binding.boxCara.setOnClickListener {
            val intent = Intent(this@Tienda, Comprar::class.java)
            intent.putExtra("Que", "cara")
            startActivity(intent)
        }
        binding.buttonReturn.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun cambiarItem(i: Int, entity: Any?) {
        var tienes = false
        val matrix = ColorMatrix()
        matrix.setSaturation(0f) // Valor de saturación 0 para blanco y negro
        val filter = ColorMatrixColorFilter(matrix)
        if (entity is ArmasEntity) {
            if (tienda.yaLasTienesArmas != null) {
                if (tienda.yaLasTienesArmas.contains(entity.id)) {
                    tienes = true
                }
            }
        } else if (entity is ArmadurasEntity) {
            if (tienda.yaLasTienesArmaduras != null) {
                if (tienda.yaLasTienesArmaduras.contains(entity.id)) {
                    tienes = true
                }
            }
        }
        val intent = Intent(this@Tienda, Comprar::class.java)
        when (i) {
            1 -> {
                var resourceId: Int? = null
                var resourceId2: Int? = null
                if (entity is ArmasEntity) {
                    resourceId =
                        resources.getIdentifier("drawable/" + entity.imagen, null, packageName)
                    intent.putExtra("Que", "Arma")
                    intent.putExtra("id", entity.id)
                    resourceId2 =
                        resources.getIdentifier(
                            "drawable/" + entity.rareza.lowercase(),
                            null,
                            packageName
                        )
                } else if (entity is ArmadurasEntity) {
                    resourceId =
                        resources.getIdentifier("drawable/" + entity.imagen, null, packageName)
                    intent.putExtra("Que", "Armadura")
                    intent.putExtra("id", entity.id)
                    resourceId2 =
                        resources.getIdentifier(
                            "drawable/" + entity.rareza.lowercase(),
                            null,
                            packageName
                        )
                }
                if (resourceId != null) {
                    binding.item1.setImageResource(resourceId)
                    if (resourceId2 != null) {
                        binding.item1.setBackgroundResource(resourceId2)
                    }

                    if (tienes) {
                        binding.item1.colorFilter = filter
                    } else {
                        binding.item1.setOnClickListener {
                            startActivity(intent)
                        }
                    }
                }
            }

            2 -> {
                var resourceId: Int? = null
                var resourceId2: Int? = null
                if (entity is ArmasEntity) {
                    resourceId =
                        resources.getIdentifier("drawable/" + entity.imagen, null, packageName)
                    intent.putExtra("Que", "Arma")
                    intent.putExtra("id", entity.id)
                    resourceId2 =
                        resources.getIdentifier(
                            "drawable/" + entity.rareza.lowercase(),
                            null,
                            packageName
                        )
                } else if (entity is ArmadurasEntity) {
                    resourceId =
                        resources.getIdentifier("drawable/" + entity.imagen, null, packageName)
                    intent.putExtra("Que", "Armadura")
                    intent.putExtra("id", entity.id)
                    resourceId2 =
                        resources.getIdentifier(
                            "drawable/" + entity.rareza.lowercase(),
                            null,
                            packageName
                        )
                }
                if (resourceId != null) {
                    binding.item2.setImageResource(resourceId)
                    if (resourceId2 != null) {
                        binding.item2.setBackgroundResource(resourceId2)
                    }
                    if (tienes) {
                        binding.item2.colorFilter = filter
                    } else {
                        binding.item2.setOnClickListener {
                            startActivity(intent)
                        }
                    }
                }
            }

            3 -> {
                var resourceId: Int? = null
                var resourceId2: Int? = null
                if (entity is ArmasEntity) {
                    resourceId =
                        resources.getIdentifier("drawable/" + entity.imagen, null, packageName)
                    intent.putExtra("Que", "Arma")
                    intent.putExtra("id", entity.id)
                    resourceId2 =
                        resources.getIdentifier(
                            "drawable/" + entity.rareza.lowercase(),
                            null,
                            packageName
                        )
                } else if (entity is ArmadurasEntity) {
                    resourceId =
                        resources.getIdentifier("drawable/" + entity.imagen, null, packageName)
                    intent.putExtra("Que", "Armadura")
                    intent.putExtra("id", entity.id)
                    resourceId2 =
                        resources.getIdentifier(
                            "drawable/" + entity.rareza.lowercase(),
                            null,
                            packageName
                        )
                }
                if (resourceId != null) {
                    binding.item3.setImageResource(resourceId)
                    if (resourceId2 != null) {
                        binding.item3.setBackgroundResource(resourceId2)
                    }
                    if (tienes) {
                        binding.item3.colorFilter = filter
                    } else {
                        binding.item3.setOnClickListener {
                            startActivity(intent)
                        }
                    }
                }
            }
        }


    }

    private suspend fun cargarTiendaDia() {
        val ejecutor = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.CONSULTAR_TIENDA_DIARIA,
            ""
        )
        val aux = ejecutor.execute() as TiendaDiaJSON?
        if (aux != null) {
            tienda = aux
            println(aux)
        } else {
            Error.showError(this@Tienda, "Error de conexion, Vuelva a intentarlo mas tarde")
            delay(5000)
            finish()
        }
    }

    fun startCountdown() {
        val timeUntilNextDay = getTimeUntilNextDay()

        object : CountDownTimer(timeUntilNextDay, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calcular horas, minutos y segundos restantes
                val hours = millisUntilFinished / 1000 / 3600
                val minutes = (millisUntilFinished / 1000 % 3600) / 60
                val seconds = millisUntilFinished / 1000 % 60

                // Actualizar el TextView
                val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.textViewTimer.text = "Reinicio Tienda en $time"
            }

            override fun onFinish() {
                // Acción al finalizar el temporizador, si es necesario
                binding.textViewTimer.text = "00:00:00"
                finish()
            }
        }.start()
    }

    fun getTimeUntilNextDay(): Long {
        val currentTime = Calendar.getInstance()

        // Crear una instancia de Calendar para el próximo día a la medianoche
        val nextDay = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Calcular la diferencia en milisegundos
        return nextDay.timeInMillis - currentTime.timeInMillis
    }

    companion object {
        lateinit var tienda: TiendaDiaJSON

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}