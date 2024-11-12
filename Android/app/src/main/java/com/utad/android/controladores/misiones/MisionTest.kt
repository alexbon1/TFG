package com.utad.android.controladores.misiones

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.utad.android.R
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.databinding.ActivityMisionTestBinding
import com.utad.android.databinding.ActivityWebViewBinding
import com.utad.android.entitys.JSON.MisionCompletaJSON
import com.utad.android.entitys.MisionesDiariasEntity
import com.utad.android.storage.Mochila
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MisionTest : AppCompatActivity() {
    lateinit var binding: ActivityMisionTestBinding
    private lateinit var webView: ActivityWebViewBinding
    private val gson = Gson()
    private var countDownTimer: CountDownTimer? = null
    private var tiempoRestante: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisionTestBinding.inflate(layoutInflater)
        webView = ActivityWebViewBinding.inflate(layoutInflater)
        if (Mochila.misionActual != null) {
            if (Mochila.misionActual.tipoMision.equals("INFO+TEST")) {
                lanzarWeb(Mochila.misionActual.otrosDatos[0] as String)
            } else {
                setContentView(binding.root)
                startCountdown(segundosAMilisegundos(Mochila.misionActual.otrosDatos[Mochila.misionActual.otrosDatos.size - 1] as String))
            }
            val preguntas = mutableListOf<MisionesDiariasEntity.Preguntas>()
            for (otrosDato in Mochila.misionActual.otrosDatos) {
                if (otrosDato is LinkedTreeMap<*, *>) {
                    val json = gson.toJson(otrosDato)
                    val pregunta = gson.fromJson(json, MisionesDiariasEntity.Preguntas::class.java)
                    preguntas.add(pregunta)
                }
            }
            cargarPreguntas(preguntas)
            binding.imageViewFinish.setOnClickListener {
                if (!todasPreguntasRespondidas(preguntas)) {
                    Error.showError(
                        this@MisionTest,
                        "Por favor, responde todas las preguntas antes de finalizar."
                    )
                    return@setOnClickListener
                }
                countDownTimer?.cancel()  // Cancelar el temporizador
                val correctas = comprobarRespuestas(preguntas)
                Mochila.misionActual.descripcion = binding.TextViewCrono.text.toString()
                Mochila.misionActual.porcentajeCompleto = calcularPorcentaje(
                    tiempoRestante,
                    Mochila.misionActual.otrosDatos[Mochila.misionActual.otrosDatos.size - 2] as String,
                    Mochila.misionActual.otrosDatos[Mochila.misionActual.otrosDatos.size - 1] as String,
                    correctas,
                    preguntas.size
                )
                lifecycleScope.launch {
                    if (Mochila.misionActual.porcentajeCompleto > 0) {
                        val args = MisionCompletaJSON(
                            Mochila.misionActual.id,
                            Mochila.misionActual.porcentajeCompleto,
                            Mochila.user,
                            binding.TextViewCrono.text.toString()
                        )
                        val ejecutor = AsincronoEjecutorDeConexiones(
                            AsincronoEjecutorDeConexiones.METODOS.MISION_COMPLETADA, args
                        );
                        val completa = ejecutor.execute() as MisionCompletaJSON?
                        if (completa != null) {
                            val intent = Intent(this@MisionTest, MisionCompleta::class.java).apply {
                                putExtra("RECOMPENSAS", completa.resultado)
                                putExtra("TITULO", "Mision Completada")
                                putExtra("FUEGOS", true)
                            }
                            startActivity(intent)
                        } else {
                            Error.showError(this@MisionTest, "Error Completando La Mision")
                        }
                    } else {
                        val intent = Intent(this@MisionTest, MisionCompleta::class.java).apply {
                            putExtra("RECOMPENSAS", "......")
                            putExtra("TITULO", "Mision Fallida")
                            putExtra("FUEGOS", false)
                        }
                        delay(4000)
                        startActivity(intent)
                    }
                }
            }
            binding.TextViewTitle.text = Mochila.misionActual.nombre
        }

    }

    private fun calcularPorcentaje(
        tiempoRestante: Long, tiempoMinimo: String, tiempoMaximo: String, correctas: Int, size: Int
    ): Int {
        // Convertir tiempo mínimo y máximo de String a Long (en milisegundos)
        val tiempoMinimoMs = tiempoMinimo.toLong() * 1000
        val tiempoMaximoMs = tiempoMaximo.toLong() * 1000

        // Calcular el tiempo transcurrido
        val tiempoTrans = tiempoMaximoMs - tiempoRestante

        // Calcular el porcentaje basado en el tiempo restante
        val porcentajeTiempo = if (tiempoTrans <= tiempoMinimoMs) {
            100
        } else {
            ((tiempoTrans.toFloat() / (tiempoMaximoMs - tiempoMinimoMs).toFloat()) * 100).toInt()
                .coerceIn(1, 100)
        }


        // Calcular el porcentaje basado en las respuestas incorrectas
        val incorrectas = size - correctas
        val porcentajeIncorrectas = (incorrectas).toFloat() * (100 / size.toFloat())

        // Ajustar el porcentaje final restando las incorrectas al porcentaje de tiempo
        val porcentajeFinal = porcentajeTiempo - porcentajeIncorrectas.toInt()

        // Asegurarse de que el porcentaje final está entre 1 y 100
        return porcentajeFinal.coerceIn(0, 100)
    }


    private fun comprobarRespuestas(preguntas: List<MisionesDiariasEntity.Preguntas>): Int {
        var respuestasCorrectas = 0

        preguntas.forEachIndexed { index, pregunta ->
            val radioGroup = binding.preguntasContainer.getChildAt(index * 2 + 1) as RadioGroup
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                if (selectedRadioButton.text == pregunta.correcta) {
                    respuestasCorrectas++
                    selectedRadioButton.setTextColor(Color.GREEN) // Color verde para las correctas
                } else {
                    selectedRadioButton.setTextColor(Color.RED) // Color rojo para las incorrectas
                }
            }
            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                radioButton.isEnabled = false // Deshabilitar el RadioButton
            }
        }

        return respuestasCorrectas
    }

    private fun segundosAMilisegundos(segundos: String): Long {
        return segundos.toLong() * 1000
    }

    private fun cargarPreguntas(preguntas: List<MisionesDiariasEntity.Preguntas>) {
        val preguntasContainer = binding.preguntasContainer
        val customFont = resources.getFont(R.font.new_rocker)

        for (pregunta in preguntas) {
            val textViewPregunta = TextView(this).apply {
                text = pregunta.pregunta
                textSize = 18f
                setPadding(0, 10, 0, 10)
                typeface = customFont
            }
            textViewPregunta.setTextColor(Color.YELLOW)
            val radioGroup = RadioGroup(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = RadioGroup.VERTICAL
            }

            pregunta.respuestas.forEach { respuesta ->
                val radioButton = RadioButton(this).apply {
                    text = respuesta
                    textSize = 16f
                    typeface = customFont
                }
                radioButton.setTextColor(Color.WHITE)
                radioGroup.addView(radioButton)
            }

            preguntasContainer.addView(textViewPregunta)
            preguntasContainer.addView(radioGroup)
        }
    }


    private fun lanzarWeb(url: String) {
        webView.buttonRendirse.setOnClickListener {
            setContentView(binding.root)
            startCountdown(segundosAMilisegundos(Mochila.misionActual.otrosDatos[Mochila.misionActual.otrosDatos.size - 1] as String))
        }
        webView.webview.loadUrl(url)
        setContentView(webView.root)
    }

    fun startCountdown(tiempo: Long) {
        countDownTimer = object : CountDownTimer(tiempo, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                // Calcular horas, minutos y segundos restantes
                val minutes = (millisUntilFinished / 1000 % 3600) / 60
                val seconds = millisUntilFinished / 1000 % 60
                tiempoRestante = millisUntilFinished

                // Actualizar el TextView
                val time = String.format("%02d:%02d", minutes, seconds)
                binding.TextViewCrono.text = "Tiempo: $time"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.TextViewCrono.text = "Tiempo: 00:00:00"
                finish()
                binding.scrollView.visibility = View.INVISIBLE
                val intent = Intent(this@MisionTest, Inicio_Personaje::class.java)
                startActivity(intent)
            }
        }.start()
    }

    private fun todasPreguntasRespondidas(preguntas: List<MisionesDiariasEntity.Preguntas>): Boolean {
        preguntas.forEachIndexed { index, _ ->
            val radioGroup = binding.preguntasContainer.getChildAt(index * 2 + 1) as RadioGroup
            if (radioGroup.checkedRadioButtonId == -1) {
                return false
            }
        }
        return true
    }
}
