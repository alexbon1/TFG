package com.utad.android.controladores.misiones

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.databinding.ActivityMisionCorrerBinding
import com.utad.android.databinding.ActivityWebViewBinding
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.MisionCompletaJSON
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch

class MisionCorrer : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityMisionCorrerBinding
    private lateinit var webView: ActivityWebViewBinding
    private lateinit var locationManager: LocationManager
    private var distanciaRecorrida = 0.0
    private var ultimaUbicacion: Location? = null
    private var distanciaObjetivo = 0
    private var tiempoRestante = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisionCorrerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Solicitar permisos de ubicación
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        } else {
            iniciarUbicacion()
        }

        lifecycleScope.launch {
            cargarUser(Mochila.user.username, Mochila.user.pwd)
            val resourceId = resources.getIdentifier(
                "drawable/" + Mochila.inventario.personaje, null, packageName
            )
            binding.imageViewPersonaje3.setImageResource(resourceId)

            if (Mochila.misionActual != null) {
                val objetivo = Mochila.misionActual.objetivo
                val tiempoTotalMinutos = Mochila.misionActual.otrosDatos[1].toString().toInt()
                distanciaObjetivo = Mochila.misionActual.otrosDatos[2].toString().toInt()

                binding.TextViewTitle.text = Mochila.misionActual.nombre
                binding.progressBar.max = distanciaObjetivo
                binding.progressBar.progress = 0

                iniciarTemporizador(tiempoTotalMinutos)
            }
        }

        binding.imageView9.setOnClickListener {
            finish()
        }
    }

    private fun iniciarUbicacion() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        if (ultimaUbicacion != null) {
            val distancia = ultimaUbicacion!!.distanceTo(location).toDouble()
            distanciaRecorrida += distancia
            Log.d("MisionCorrer", "Distancia recorrida: $distanciaRecorrida")
            Log.d(
                "MisionCorrer",
                "Nueva ubicación: Latitud=${location.latitude}, Longitud=${location.longitude}"
            )
            actualizarProgreso()
        } else {
            Log.d(
                "MisionCorrer",
                "Primera ubicación recibida: Latitud=${location.latitude}, Longitud=${location.longitude}"
            )
        }
        ultimaUbicacion = location
    }

    @SuppressLint("SetTextI18n")
    private fun actualizarProgreso() {
        if (distanciaObjetivo > 0) {
            val progreso = (distanciaRecorrida / distanciaObjetivo * 100).toInt()
            binding.progressBar.progress = progreso
            Log.d("MisionCorrer", "Progreso: $progreso%")
            runOnUiThread {
                binding.textViewTitle5.text =
                    "Distancia: ${distanciaRecorrida.toInt()}/${distanciaObjetivo} M"
            }
            if (distanciaRecorrida >= distanciaObjetivo) {
                completarMision()
            }
        } else {
            Log.e(
                "MisionCorrer",
                "La distancia objetivo es cero o menor, no se puede actualizar el progreso."
            )
        }
    }

    private fun completarMision() {
        val porcentajeCompleto = (distanciaRecorrida / distanciaObjetivo) * 100

        lifecycleScope.launch {
            val args = MisionCompletaJSON(
                Mochila.misionActual.id,
                porcentajeCompleto.toInt(),
                Mochila.user,
                tiempoRestante
            )
            val ejecutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.MISION_COMPLETADA, args
            )
            val completa = ejecutor.execute() as MisionCompletaJSON?
            if (completa != null) {
                val intent = Intent(this@MisionCorrer, MisionCompleta::class.java).apply {
                    putExtra("RECOMPENSAS", completa.resultado)
                    putExtra("TITULO", "Mision Completada")
                    putExtra("FUEGOS",true)
                }
                startActivity(intent)
                finish()
            } else {
                Error.showError(this@MisionCorrer, "Error completando mision")
            }
        }
    }


    private suspend fun cargarUser(username: String, pwd: String) {
        val args = arrayOf(username, pwd)
        val ejecutor =
            AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
        val user = (ejecutor.execute() as? UsuarioJSON)!!
        val ejecutor2 = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO, user.id
        )
        val inventario = ejecutor2.execute() as InventariosEntity
        UtilidadesJSON.GuardarUser(applicationContext, user.toUsersEntity())
        Mochila.user = user
        Mochila.inventario = inventario
    }

    private fun iniciarTemporizador(tiempoTotalMinutos: Int) {
        val tiempoTotalSegundos = tiempoTotalMinutos * 60
        val countDownTimer = object : CountDownTimer(tiempoTotalSegundos * 1000L, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val tiempoRestanteSegundos = millisUntilFinished / 1000
                runOnUiThread {

                    binding.textViewTitle2.text =
                        "Tiempo: ${formatearTiempo(tiempoRestanteSegundos)}"
                    binding.textViewTitle5.text =
                        "Distancia: ${distanciaRecorrida.toInt()}/${distanciaObjetivo} M"
                    tiempoRestante = "Tiempo: ${formatearTiempo(tiempoRestanteSegundos)}"


                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                runOnUiThread {
                    binding.progressBar.progress = binding.progressBar.max
                    binding.textViewTitle2.text = "Tiempo: 00:00:00"
                }
                val intent = Intent(this@MisionCorrer, MisionCompleta::class.java).apply {
                    putExtra("RECOMPENSAS", "........")
                    putExtra("TITULO", "Mision Fallida")
                    putExtra("FUEGOS",false)
                }
                startActivity(intent)
            }
        }
        countDownTimer.start()
    }

    private fun formatearTiempo(segundos: Long): String {
        val horas = segundos / 3600
        val minutos = (segundos % 3600) / 60
        val seg = segundos % 60
        return String.format("%02d:%02d:%02d", horas, minutos, seg)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarUbicacion()
        }
    }
}
