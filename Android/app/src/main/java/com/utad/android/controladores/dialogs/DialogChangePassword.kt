package com.utad.android.controladores.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.DialogChangePasswordBinding
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.launch

class DialogChangePassword {
    companion object {
        private lateinit var binding: DialogChangePasswordBinding
        fun show(context: Context,  lifecycleOwner: LifecycleOwner) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            binding = DialogChangePasswordBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(binding.root)
            binding.buttonChange.setOnClickListener {
                val currentPassword = binding.editTextCurrentPassword.text.toString()
                val newPassword = binding.editTextNewPassword.text.toString()
                val confirmNewPassword = binding.editTextConfirmNewPassword.text.toString()
                if (Mochila.user.pwd.equals(currentPassword)){
                    if (newPassword == confirmNewPassword) {
                        if(validarPWD(newPassword,context)){
                            lifecycleOwner.lifecycleScope.launch {
                                val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.MODIFY_PWD,newPassword)
                                if (ejecutor.execute() as Boolean){
                                    UtilidadesJSON.GuardarUser(context,Mochila.user.toUsersEntity())
                                    dialog.dismiss()
                                }else{
                                    Error.showError(context, "Error al cambiar contraseña")
                                }
                            }
                        }
                    } else {
                        Error.showError(context, "Contraseñas diferentes")
                    }
                }else{
                    Error.showError(context, "Contraseña actual erronea")
                }
            }
            binding.buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        private fun cambiarContrasena(currentPassword: String, newPassword: String) {

        }
        private fun validarPWD(pwd: String,context: Context): Boolean {
            val longitudRequerida = 8
            val contieneMayuscula = pwd.any { it.isUpperCase() }
            val contieneMinuscula = pwd.any { it.isLowerCase() }
            val contieneDigito = pwd.any { it.isDigit() }
            val contieneCaracterEspecial = pwd.any { !it.isLetterOrDigit() }

            if (pwd.length < longitudRequerida) {
                Error.showError(context, "La contraseña debe tener al menos 8 caracteres.")
                return false
            }

            if (!contieneMayuscula) {
                Error.showError(context, "La contraseña debe incluir al menos una letra mayúscula.")
                return false
            }

            if (!contieneMinuscula) {
                Error.showError(context, "La contraseña debe incluir al menos una letra minúscula.")
                return false
            }

            if (!contieneDigito) {
                Error.showError(context, "La contraseña debe incluir al menos un dígito.")
                return false
            }

            if (!contieneCaracterEspecial) {
                Error.showError(
                    context,
                    "La contraseña debe incluir al menos un carácter especial."
                )
                return false
            }

            // Si la contraseña cumple con todas las condiciones, retornamos true
            return true
        }
    }
}