package com.utad.android.controladores.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.utad.android.R

class TutorialDialog(context: Context) {
    private val dialog: Dialog = Dialog(context)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_tutorial, null)
        dialog.setContentView(view)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imageViewClose = view.findViewById<ImageView>(R.id.imageViewClose)
        val textViewInventario = view.findViewById<TextView>(R.id.textViewInventario)
        val textViewInventarioContent = view.findViewById<TextView>(R.id.textViewInventarioContent)
        val textViewMisiones = view.findViewById<TextView>(R.id.textViewMisiones)
        val textViewMisionesContent = view.findViewById<TextView>(R.id.textViewMisionesContent)
        val textViewCombate = view.findViewById<TextView>(R.id.textViewCombate)
        val textViewCombateContent = view.findViewById<TextView>(R.id.textViewCombateContent)

        // Set click listeners to toggle visibility of content
        textViewInventario.setOnClickListener {
            if (textViewInventarioContent.visibility == TextView.GONE) {
                textViewInventarioContent.visibility = TextView.VISIBLE
            } else {
                textViewInventarioContent.visibility = TextView.GONE
            }
        }

        textViewMisiones.setOnClickListener {
            if (textViewMisionesContent.visibility == TextView.GONE) {
                textViewMisionesContent.visibility = TextView.VISIBLE
            } else {
                textViewMisionesContent.visibility = TextView.GONE
            }
        }

        textViewCombate.setOnClickListener {
            if (textViewCombateContent.visibility == TextView.GONE) {
                textViewCombateContent.visibility = TextView.VISIBLE
            } else {
                textViewCombateContent.visibility = TextView.GONE
            }
        }

        imageViewClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}
