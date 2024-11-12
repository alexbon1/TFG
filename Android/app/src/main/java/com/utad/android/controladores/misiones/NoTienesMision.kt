package com.utad.android.controladores.misiones

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.android.R
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.databinding.ActivityNoTienesBinding

class NoTienesMision : AppCompatActivity() {
    lateinit var binding: ActivityNoTienesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNoTienesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView5.text=intent.getStringExtra("MSG")
        binding.buttonRendirse.setOnClickListener {
            val intent = Intent(this@NoTienesMision, Inicio_Personaje::class.java)
            startActivity(intent)
        }
    }
}