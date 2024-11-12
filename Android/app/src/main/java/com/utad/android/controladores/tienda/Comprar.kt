package com.utad.android.controladores.tienda

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.entitys.ArmadurasEntity
import com.utad.android.entitys.ArmasEntity
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.BoxJSON
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityComprarBoxBinding
import com.utad.android.storage.Mochila
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Comprar : AppCompatActivity() {
    lateinit var binding: ActivityComprarBoxBinding
    private val tienda = Tienda.tienda

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = ActivityComprarBoxBinding.inflate(layoutInflater)
        val what = intent.getStringExtra("Que")
        val id = intent.getStringExtra("id")

        binding.buttonReturn.setOnClickListener {
            finish()
        }
        var itemArmas: ArmasEntity? = null
        var itemArmaduras: ArmadurasEntity? = null

        if (!what.equals("barata") && !what.equals("cara")) {
            if (what.equals("Armadura")) {
                itemArmaduras = getArmadura(id)
            } else if (what.equals("Arma")) {
                itemArmas = getArmas(id)
            }
            if (itemArmas != null) {
                binding.textViewNombre.text = itemArmas.nombre
                binding.textViewRareza.text = "Rareza: " + itemArmas.rareza
                System.err.println(itemArmas.descripcion)
                binding.textViewDescripcion.text =
                    itemArmas.descripcion + "\n\n\n Precio: ${tienda.precios["Armas" + itemArmas.rareza]}"
                val resourceId =
                    resources.getIdentifier("drawable/" + itemArmas.imagen, null, packageName)
                binding.imageViewElemento.setImageResource(resourceId)
                val resourceId2 =
                    resources.getIdentifier(
                        "drawable/" + itemArmas.rareza.lowercase(),
                        null,
                        packageName
                    )
                binding.imageViewElemento.setBackgroundResource(resourceId2)
                binding.buttonComprar.setOnClickListener {
                    comprar(itemArmas, tienda.precios["Armas" + itemArmas.rareza] as Int)
                }
            } else if (itemArmaduras != null) {
                binding.textViewNombre.text = itemArmaduras.nombre
                binding.textViewRareza.text = "Rareza: " + itemArmaduras.rareza
                binding.textViewDescripcion.text =
                    itemArmaduras.descripcion + "\n\n\n Precio: ${tienda.precios["Armaduras" + itemArmaduras.rareza]}"
                val resourceId =
                    resources.getIdentifier("drawable/" + itemArmaduras.imagen, null, packageName)
                val resourceId2 =
                    resources.getIdentifier(
                        "drawable/" + itemArmaduras.rareza.lowercase(),
                        null,
                        packageName
                    )
                binding.imageViewElemento.setBackgroundResource(resourceId2)
                binding.imageViewElemento.setImageResource(resourceId)
                binding.buttonComprar.setOnClickListener {
                    comprar(
                        itemArmaduras, tienda.precios["Armaduras" + itemArmaduras.rareza] as Int
                    )
                }
            }
        } else {
            if (what.equals("barata")) {
                val resourceId = resources.getIdentifier("drawable/box_barata", null, packageName)
                binding.imageViewElemento.setImageResource(resourceId)
                binding.textViewNombre.text = "Caja Cutre"
                binding.textViewRareza.text = "Comun Raro Especial"
                binding.textViewDescripcion.text =
                    "Si fuera tu me estiraria y compraria algo mejor...... \n\n\n Precio: ${tienda.precios["Caja Cutre"]}"
                binding.buttonComprar.setOnClickListener {
                    lifecycleScope.launch {
                        val ejecutor = AsincronoEjecutorDeConexiones(
                            AsincronoEjecutorDeConexiones.METODOS.COMPRARBOX, "barata"
                        )
                        val res: BoxJSON? = ejecutor.execute() as BoxJSON?
                        if (res != null) {
                            if (res.armas != null) {
                                binding.textViewNombre.text = res.armas.nombre
                                binding.textViewRareza.text = res.armas.rareza
                                val resourceId = resources.getIdentifier(
                                    "drawable/" + res.armas.imagen, null, packageName
                                )
                                binding.imageViewElemento.setImageResource(resourceId)
                            } else if (res.armadura != null) {
                                binding.textViewNombre.text = res.armadura.nombre
                                binding.textViewRareza.text = res.armadura.rareza
                                val resourceId = resources.getIdentifier(
                                    "drawable/" + res.armadura.imagen, null, packageName
                                )
                                binding.imageViewElemento.setImageResource(resourceId)
                            }
                            if (res.loTienes) {
                                binding.textViewDescripcion.text =
                                    "Oh no parece que ya tenias este objeto, una lastima..... \n\n\n (No se admiten devoluciones)"
                            } else {
                                binding.textViewDescripcion.text = ""
                            }
                            binding.buttonComprar.visibility = View.INVISIBLE
                        } else {
                            Error.showError(this@Comprar, "Error procesando la compra")

                        }
                    }
                }
            } else if (what.equals("cara")) {
                val resourceId = resources.getIdentifier("drawable/box_cara", null, packageName)
                binding.imageViewElemento.setImageResource(resourceId)
                binding.textViewNombre.text = "Caja Ultramega OP"
                binding.textViewRareza.text = "Epica Legendaria"
                binding.textViewDescripcion.text =
                    "¿Quieres probar suerte....? \n\n\n Precio: ${tienda.precios["Caja Ultramega OP"]}"
                binding.buttonComprar.setOnClickListener {
                   if (Mochila.user.monedas>tienda.precios["Caja Ultramega OP"] as Int){
                       lifecycleScope.launch {
                           val ejecutor = AsincronoEjecutorDeConexiones(
                               AsincronoEjecutorDeConexiones.METODOS.COMPRARBOX, "cara"
                           )
                           val res: BoxJSON? = ejecutor.execute() as BoxJSON?
                           if (res != null) {
                               if (res.armas != null) {
                                   binding.textViewNombre.text = res.armas.nombre
                                   binding.textViewRareza.text = res.armas.rareza
                                   val resourceId = resources.getIdentifier(
                                       "drawable/" + res.armas.imagen, null, packageName
                                   )
                                   binding.imageViewElemento.setImageResource(resourceId)
                               } else if (res.armadura != null) {
                                   binding.textViewNombre.text = res.armadura.nombre
                                   binding.textViewRareza.text = res.armadura.rareza
                                   val resourceId = resources.getIdentifier(
                                       "drawable/" + res.armadura.imagen, null, packageName
                                   )
                                   binding.imageViewElemento.setImageResource(resourceId)
                               }
                               if (res.loTienes) {
                                   binding.textViewDescripcion.text =
                                       "Oh no parece que ya tenias este objeto, una lastima..... \n\n\n (No se admiten devoluciones)"
                               } else {
                                   binding.textViewDescripcion.text = ""
                               }
                               binding.buttonComprar.visibility = View.INVISIBLE
                           } else {
                               Error.showError(this@Comprar, "Error procesando la compra")

                           }
                       }
                   }else{
                       Error.showError(this@Comprar,"Parece que no tienes suficiente dinero....")
                   }
                }
            }
        }
        setContentView(binding.root)
    }

    private fun comprar(item: Any, precio: Int) {
        if (Mochila.user.monedas > precio) {
            lifecycleScope.launch {
                val ejecutor = AsincronoEjecutorDeConexiones(
                    AsincronoEjecutorDeConexiones.METODOS.COMPRAR, item
                )
                val inventariosEntity = ejecutor.execute() as InventariosEntity?
                if (inventariosEntity != null) {
                    Mochila.inventario = inventariosEntity;
                    val args = arrayOf(Mochila.user.username, Mochila.user.pwd)
                    val ejecutor2 = AsincronoEjecutorDeConexiones(
                        AsincronoEjecutorDeConexiones.METODOS.LOGIN, args
                    )
                    val user = (ejecutor2.execute() as? UsuarioJSON)!!;
                    Mochila.user = user
                    val intent = Intent(this@Comprar, Inicio_Personaje::class.java)
                    startActivity(intent)
                } else {
                    Error.showError(this@Comprar, "Error procesando la compra")
                    delay(5000)
                    val intent = Intent(this@Comprar, Inicio_Personaje::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Error.showError(this@Comprar, "No tienes suficiente dinero....")
        }
    }

    private fun getArmadura(id: String?): ArmadurasEntity? {
        for ((index, armadurasEntity) in tienda.armaduras.withIndex()) {
            if (armadurasEntity.id.equals(id)) {
                return armadurasEntity
            }
        }
        return null
    }

    private fun getArmas(id: String?): ArmasEntity? {
        for ((index, armasEntity) in tienda.armas.withIndex()) {
            if (armasEntity.id.equals(id)) {
                return armasEntity
            }
        }
        return null
    }
}