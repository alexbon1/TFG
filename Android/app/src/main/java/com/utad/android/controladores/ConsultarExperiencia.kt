package com.utad.android.controladores

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utad.android.databinding.ActivityConsultarExperienciaBinding
import com.utad.android.storage.Mochila

class ConsultarExperiencia : AppCompatActivity() {
    private lateinit var binding: ActivityConsultarExperienciaBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityConsultarExperienciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewNivel.text = String.format("%d", Mochila.user.nivel)
        var a = Mochila.user
        binding.textViewInfo.text = "Estas a ${
            String.format(
                "%d",
                Mochila.user.xpHastaSiguiente
            )
        } puntos del nivel ${String.format("%d", Mochila.user.nivel + 1)}"

        binding.progressBarXP.max = Mochila.user.rangoActual
        binding.progressBarXP.progress = Mochila.user.rangoActual - Mochila.user.xpHastaSiguiente

        binding.buttonReturn.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }
}