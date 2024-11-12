package com.utad.android.controladores.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.utad.android.controladores.inventario.InventarioActivity
import com.utad.android.entitys.ArmadurasEntity
import com.utad.android.entitys.ArmasEntity
import com.utad.android.entitys.InventariosEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.DialogSeleccionInventarioBinding
import com.utad.android.storage.Mochila
import kotlinx.coroutines.launch

class SeleccionInventario {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var binding: DialogSeleccionInventarioBinding
        lateinit var lifecycleOwner: LifecycleOwner

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        fun showArmas(context: Context, lifecycleOwner: LifecycleOwner, Inv: List<ArmasEntity>) {
            val dialog = Dialog(context)
            Companion.lifecycleOwner = lifecycleOwner
            Companion.context = context
            binding = DialogSeleccionInventarioBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(binding.root)

            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            )
            setActualArma()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.button2.setOnClickListener {
                dialog.dismiss()
                //val intent = Intent(context, InventarioActivity::class.java)
                //startActivity(context, intent, null)
            }

            binding.linearLayoutImages.gravity = Gravity.CENTER_HORIZONTAL

            var horizontalLayout: LinearLayout? = null
            for ((index, item) in Inv.withIndex()) {
                if (index % 2 == 0) {
                    horizontalLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                        orientation = LinearLayout.HORIZONTAL
                    }
                    binding.linearLayoutImages.addView(horizontalLayout)
                }

                val imageView = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        400, // Width in pixels
                        400  // Height in pixels
                    ).apply {
                        setMargins(8, 8, 8, 8) // Set margin as needed
                    }
                    val resourceId = context.resources.getIdentifier(
                        item.imagen, "drawable", context.packageName
                    )
                    setImageDrawable(ContextCompat.getDrawable(context, resourceId))
                    binding.imageViewSelected.setImageResource(resourceId)

                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                val resourceId2 = context.resources.getIdentifier(
                    item.rareza.lowercase(), "drawable", context.packageName
                )
                imageView.setBackgroundResource(resourceId2)
                imageView.setOnClickListener {
                    setActualArma(item)
                }
                horizontalLayout?.addView(imageView)
            }
            dialog.show()
        }

        private fun setActualArma() {
            lifecycleOwner.lifecycleScope.launch {
                val set = Mochila.inventario.setActual.split("|")
                if (set != null) {
                    if (set[0] != "null") {
                        val inv = arrayListOf(set[0])
                        val ejecutor = AsincronoEjecutorDeConexiones(
                            AsincronoEjecutorDeConexiones.METODOS.GET_ARMAS, inv
                        )
                        val ret = ejecutor.execute() as List<ArmasEntity>
                        val resourceId = context.resources.getIdentifier(
                            ret[0].imagen, "drawable", context.packageName
                        )
                        binding.imageViewSelected.setImageResource(resourceId)
                        val resourceId2 = context.resources.getIdentifier(
                            ret[0].rareza.lowercase(), "drawable", context.packageName
                        )
                        binding.imageViewSelected.setBackgroundResource(resourceId2)
                        binding.textViewImageName.text = ret[0].nombre
                        binding.textViewTipo.text = ret[0].rareza
                        binding.textViewDescripcion.text = ret[0].descripcion
                    }
                }
            }
        }

        private fun setActualArma(arma: ArmasEntity) {
            lifecycleOwner.lifecycleScope.launch {
                val resourceId = context.resources.getIdentifier(
                    arma.imagen, "drawable", context.packageName
                )
                binding.imageViewSelected.setImageResource(resourceId)
                val resourceId2 = context.resources.getIdentifier(
                    arma.rareza.lowercase(), "drawable", context.packageName
                )
                binding.imageViewSelected.setBackgroundResource(resourceId2)
                binding.textViewImageName.text = arma.nombre
                binding.textViewTipo.text = arma.rareza
                binding.textViewDescripcion.text = arma.descripcion

                InventarioActivity.binding.logoArma.setImageResource(resourceId)
                InventarioActivity.binding.logoArma.setBackgroundResource(resourceId2)
                val set = Mochila.inventario.setActual.split("|").toMutableList()
                set[0] = arma.id
                val new = set[0] + "|" + set[1]
                val args = arrayOf(String.format("%d", Mochila.inventario.id), "setactual", new)
                val ejecutor = AsincronoEjecutorDeConexiones(
                    AsincronoEjecutorDeConexiones.METODOS.MODIFY_INVENTARIO, args
                )
                Mochila.inventario = ejecutor.execute() as InventariosEntity?
            }
        }

        fun showArmaduras(
            context: Context, lifecycleOwner: LifecycleOwner, Inv: List<ArmadurasEntity>
        ) {
            val dialog = Dialog(context)
            Companion.lifecycleOwner = lifecycleOwner
            Companion.context = context
            binding = DialogSeleccionInventarioBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(binding.root)

            // Make the dialog full screen
            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setActualArmadura()
            binding.button2.setOnClickListener {
                dialog.dismiss()
            }

            binding.linearLayoutImages.gravity = Gravity.CENTER_HORIZONTAL

            var horizontalLayout: LinearLayout? = null
            for ((index, item) in Inv.withIndex()) {
                if (index % 2 == 0) {
                    horizontalLayout = LinearLayout(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                        orientation = LinearLayout.HORIZONTAL
                    }
                    binding.linearLayoutImages.addView(horizontalLayout)
                }

                val imageView = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        400, // Width in pixels
                        400  // Height in pixels
                    ).apply {
                        setMargins(8, 8, 8, 8) // Set margin as needed
                    }
                    val resourceId = context.resources.getIdentifier(
                        item.imagen, "drawable", context.packageName
                    )
                    setImageDrawable(ContextCompat.getDrawable(context, resourceId))
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                imageView.setOnClickListener {
                    setActualArmadura(item)
                }
                val resourceId2 = context.resources.getIdentifier(
                    item.rareza.lowercase(), "drawable", context.packageName
                )

                imageView.setBackgroundResource(resourceId2)
                horizontalLayout?.addView(imageView)
            }

            dialog.show()
        }

        private fun setActualArmadura() {
            lifecycleOwner.lifecycleScope.launch {
                val set = Mochila.inventario.setActual.split("|")
                if (set != null) {
                    if (set[1] != "null") {
                        val inv = arrayListOf(set[1])
                        val ejecutor = AsincronoEjecutorDeConexiones(
                            AsincronoEjecutorDeConexiones.METODOS.GET_ARMADURAS, inv
                        )
                        val ret = ejecutor.execute() as List<ArmadurasEntity>
                        val resourceId = context.resources.getIdentifier(
                            ret[0].imagen, "drawable", context.packageName
                        )
                        binding.imageViewSelected.setImageResource(resourceId)
                        val resourceId2 = context.resources.getIdentifier(
                            ret[0].rareza.lowercase(), "drawable", context.packageName
                        )
                        binding.imageViewSelected.setBackgroundResource(resourceId2)
                        binding.textViewImageName.text = ret[0].nombre
                        binding.textViewTipo.text = ret[0].rareza
                        binding.textViewDescripcion.text = ret[0].descripcion
                    }
                }
            }
        }

        private fun setActualArmadura(armadura: ArmadurasEntity) {
            lifecycleOwner.lifecycleScope.launch {
                val resourceId = context.resources.getIdentifier(
                    armadura.imagen, "drawable", context.packageName
                )
                binding.imageViewSelected.setImageResource(resourceId)
                binding.imageViewSelected.setImageResource(resourceId)
                val resourceId2 = context.resources.getIdentifier(
                    armadura.rareza.lowercase(), "drawable", context.packageName
                )
                binding.imageViewSelected.setBackgroundResource(resourceId2)
                binding.textViewImageName.text = armadura.nombre
                binding.textViewTipo.text = armadura.rareza
                binding.textViewDescripcion.text = armadura.descripcion
                InventarioActivity.binding.logoArmaduras.setImageResource(resourceId)
                InventarioActivity.binding.logoArmaduras.setBackgroundResource(resourceId2)
                val set = Mochila.inventario.setActual.split("|").toMutableList()
                set[1] = armadura.id
                val new = set[0] + "|" + set[1]
                val args = arrayOf(String.format("%d", Mochila.inventario.id), "setactual", new)
                val ejecutor = AsincronoEjecutorDeConexiones(
                    AsincronoEjecutorDeConexiones.METODOS.MODIFY_INVENTARIO, args
                )
                Mochila.inventario = ejecutor.execute() as InventariosEntity?
            }
        }
    }
}
