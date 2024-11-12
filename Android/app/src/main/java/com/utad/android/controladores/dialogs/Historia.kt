package com.utad.android.controladores.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.utad.android.entitys.PersonajesEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityHistoriaBinding


class Historia {
    companion object {
        private lateinit var binding: ActivityHistoriaBinding
        suspend fun showHistoria(context: Context, idPersonaje: String) {
            val dialog = Dialog(context)
            binding = ActivityHistoriaBinding.inflate(dialog.layoutInflater)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(binding.root)

            val personaje = cargarPersonaje(idPersonaje)
            if (personaje != null) {
                binding.textViewName.text= personaje.nombre
                binding.textViewStory.text= personaje.historia
                binding.buttonClose.setOnClickListener {
                    dialog.dismiss()
                }
                val window = dialog.window
                if (window != null) {
                    val layoutParams = WindowManager.LayoutParams()
                    layoutParams.copyFrom(window.attributes)
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT // Ancho ajustado
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT // Altura envuelta
                    window.attributes = layoutParams
                }
                dialog.show()

            }




        }

        private suspend fun cargarPersonaje(img: String): PersonajesEntity? {
            val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.CONSULTAR_PERSONAJE_IMAGEN,img)
            val result=(ejecutor.execute() as? PersonajesEntity)
            return result
        }
    }
}