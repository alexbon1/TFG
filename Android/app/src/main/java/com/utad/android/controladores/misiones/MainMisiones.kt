package com.utad.android.controladores.misiones

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.utad.android.databinding.ActivityMainMisionesBinding
import com.utad.android.storage.Mochila
import java.util.Calendar

class MainMisiones : AppCompatActivity() {
    private lateinit var binding: ActivityMainMisionesBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMisionesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCountdown()
        binding.buttonDiaria.setOnClickListener {
            val intent = Intent(this@MainMisiones, MisionesDiarias::class.java)
            intent.putExtra("NORMAL",true)
            startActivity(intent)
        }
        binding.buttonAventura.setOnClickListener {
            val intent = Intent(this@MainMisiones, MisionesDiarias::class.java)
            intent.putExtra("NORMAL",false)
            startActivity(intent)
        }
        binding.buttonReturn.setOnClickListener {
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
                binding.textViewTimer.text = "Nuevas Misiones en $time"
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

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}