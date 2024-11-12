package com.utad.android.controladores.misiones

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.databinding.ActivityMisionCompletaBinding
import com.utad.android.storage.Mochila

class MisionCompleta : AppCompatActivity() {
    private lateinit var binding: ActivityMisionCompletaBinding
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisionCompletaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir las recompensas del Intent
        val titulo = intent.getStringExtra("TITULO")
        val recompensas = intent.getStringExtra("RECOMPENSAS")
        binding.TextViewTitle.text = titulo
        binding.textViewTitle3.text = recompensas

        binding.buttonReturn2.setOnClickListener {
            val intent = Intent(this@MisionCompleta, Inicio_Personaje::class.java)
            startActivity(intent)
        }

        if (intent.getBooleanExtra("FUEGOS",false)){
            val images = listOf(binding.imageView12, binding.imageView13, binding.imageView14)

            images.forEachIndexed { index, imageView ->
                handler.postDelayed({
                    showImageWithRotation(imageView)
                }, (index + 1) * 1000L) // 1 segundo por cada imagen
            }
            val resourceId2 =
                resources.getIdentifier(
                    "drawable/" + Mochila.inventario.personaje,
                    null,
                    packageName
                )
            binding.imageViewFallida.setImageResource(resourceId2)
        }else{
            binding.imageViewFallida.visibility=View.VISIBLE
        }

    }

    private fun showImageWithRotation(imageView: View) {
        imageView.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        animator.duration = 500 // medio segundo para la animación de giro
        animator.start()
    }
}
