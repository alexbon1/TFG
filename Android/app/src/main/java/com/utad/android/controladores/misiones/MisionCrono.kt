package com.utad.android.controladores.misiones

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityMisionCronoBinding
import com.utad.android.databinding.ActivityWebViewBinding
import com.utad.android.entitys.JSON.MisionCompletaJSON
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MisionCrono : AppCompatActivity() {
    private lateinit var binding: ActivityMisionCronoBinding
    private var tiempoRestante = 1.0
    private lateinit var countDownTimer:CountDownTimer
    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisionCronoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            cargarUser(Mochila.user.username, Mochila.user.pwd)
            val resourceId = resources.getIdentifier("drawable/" + Mochila.inventario.personaje, null, packageName)
            binding.imageViewPersonaje3.setImageResource(resourceId)

            if (Mochila.misionActual != null) {
                val tiempoTotalMinutos = Mochila.misionActual.otrosDatos[1].toString().toInt()

                binding.TextViewTitle.text = Mochila.misionActual.nombre
                binding.progressBar.max = tiempoTotalMinutos * 60
                binding.progressBar.progress = 0

                iniciarTemporizador(tiempoTotalMinutos)
            }
        }

        binding.imageView9.setOnClickListener {
            countDownTimer.cancel()

            val intent = Intent(this@MisionCrono, MisionCompleta::class.java).apply {
                putExtra("RECOMPENSAS", "......")
                putExtra("TITULO", "Mision No Completada")
                putExtra("FUEGOS",false)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
            }, 3000) // 3000 milisegundos = 3 segundos

        }
    }

    private suspend fun cargarUser(username: String, pwd: String) {
        val args = arrayOf(username, pwd)
        val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
        val user = (ejecutor.execute() as? UsuarioJSON)!!
        val ejecutor2 = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO, user.id)
        val inventario = ejecutor2.execute() as InventariosEntity
        UtilidadesJSON.GuardarUser(applicationContext, user.toUsersEntity())
        Mochila.user = user
        Mochila.inventario = inventario
    }

    private fun iniciarTemporizador(tiempoTotalMinutos: Int) {
        val tiempoTotalSegundos = tiempoTotalMinutos * 60
        countDownTimer = object : CountDownTimer(tiempoTotalSegundos * 1000L, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                 tiempoRestante = (millisUntilFinished / 1000).toDouble()

                val tiempoTranscurrido = tiempoTotalSegundos - tiempoRestante
                runOnUiThread {
                    binding.progressBar.setProgress(tiempoTranscurrido.toInt(), true)
                    binding.textViewTitle2.text = "Tiempo: ${formatearTiempo(tiempoRestante)}"
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                runOnUiThread {
                    binding.progressBar.progress = binding.progressBar.max
                    binding.textViewTitle2.text = "Tiempo: 00:00:00"
                }
                completarMision()
            }
        }
        countDownTimer.start()
    }

    private fun completarMision() {
        lifecycleScope.launch {
            val args = MisionCompletaJSON(
                Mochila.misionActual.id,
                100,  // Se asume que la misión de cronómetro siempre completa el 100%
                Mochila.user,
                ""
            )
            val ejecutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.MISION_COMPLETADA, args
            )
            val completa = ejecutor.execute() as MisionCompletaJSON?
            if (completa != null) {
                val intent = Intent(this@MisionCrono, MisionCompleta::class.java).apply {
                    putExtra("RECOMPENSAS", completa.resultado)
                    putExtra("TITULO", "Mision Completada")
                    putExtra("FUEGOS",true)
                }
                startActivity(intent)
                finish()
            } else {
                // Error al completar la misión
                Log.e("MisionCrono", "Error completando la misión.")
            }
        }
    }

    private fun formatearTiempo(segundos: Double): String {
        val horas = (segundos / 3600).toInt()
        val minutos = ((segundos % 3600) / 60).toInt()
        val seg = (segundos % 60).toInt()
        return String.format("%02d:%02d:%02d", horas, minutos, seg)
    }

}
