package com.utad.android.controladores.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.utad.android.databinding.DialogErrorBinding

class Error {

    companion object {
        private lateinit var binding: DialogErrorBinding
        fun showError(context: Context, message: String) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            binding = DialogErrorBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(binding.root)
            binding.textViewErrorMessage.text = message
            binding.buttonClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}