package com.utad.android.controladores.batallas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.entitys.UsersEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivityEsperaBatallasBinding
import com.utad.android.databinding.CardPersonajeBinding
import com.utad.android.storage.Mochila
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EsperaBatallasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEsperaBatallasBinding
    private var lista: Set<UsersEntity> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEsperaBatallasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonReturn.setOnClickListener { finish() }

        binding.buttonNueva.setOnClickListener {
            val intent = Intent(this, BatallaActivity::class.java)
            intent.putExtra("entrada", "NUEVA")
            intent.putExtra("puerto", -100)
            startActivity(intent)
        }

        lifecycleScope.launch {
            cargarLista()
            comprobarSet()
            recargarUser()
            val items = mutableListOf<CardItem>()
            for (usersEntity in lista) {
                val ejecutor = AsincronoEjecutorDeConexiones(
                    AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO,
                    usersEntity.id
                )
                val inventarioRiv: InventariosEntity = withContext(Dispatchers.IO) {
                    ejecutor.execute() as InventariosEntity
                }
                val resourceId = resources.getIdentifier(
                    "drawable/" + inventarioRiv.personaje,
                    null,
                    packageName
                )
                val card = CardItem(resourceId, usersEntity.username, usersEntity.nivel)
                items.add(card)
            }
            addItemsToLayout(items)
        }
        binding.buttonRecarga.setOnClickListener {
            lifecycleScope.launch {
                cargarLista()
                val items = mutableListOf<CardItem>()
                for (usersEntity in lista) {
                    val ejecutor = AsincronoEjecutorDeConexiones(
                        AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO,
                        usersEntity.id
                    )
                    val inventarioRiv: InventariosEntity = withContext(Dispatchers.IO) {
                        ejecutor.execute() as InventariosEntity
                    }
                    val resourceId = resources.getIdentifier(
                        "drawable/" + inventarioRiv.personaje,
                        null,
                        packageName
                    )
                    val card = CardItem(resourceId, usersEntity.username, usersEntity.nivel)
                    items.add(card)
                }
                addItemsToLayout(items)
            }
        }
    }

    private suspend fun recargarUser() {
        val args = arrayOf(Mochila.user.username, Mochila.user.pwd)
        val ejecutor =
            AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.LOGIN, args)
        Mochila.user = ejecutor.execute() as UsuarioJSON?
    }

    private suspend fun comprobarSet() {
        if (Mochila.inventario.setActual == null || Mochila.inventario.setActual=="") {
            val preejcutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO,
                Mochila.user.id
            )
            Mochila.inventario = preejcutor.execute() as InventariosEntity?
            val args = arrayOf(String.format("%d", Mochila.inventario.id), "setactual", "null|null")
            val ejecutor = AsincronoEjecutorDeConexiones(
                AsincronoEjecutorDeConexiones.METODOS.MODIFY_INVENTARIO,
                args
            )
            Mochila.inventario = ejecutor.execute() as InventariosEntity?
        }
    }

    private fun addItemsToLayout(items: List<CardItem>) {
        binding.linearLayoutContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)
        for (item in items) {
            val cardBinding =
                CardPersonajeBinding.inflate(inflater, binding.linearLayoutContainer, false)
            cardBinding.textViewNombre.text = item.nombre
            cardBinding.textViewNivel.text = String.format("%d", item.nivel)
            cardBinding.cardImage.setImageResource(item.imageResId)
            cardBinding.root.setOnClickListener {
                lifecycleScope.launch {
                    val intent = Intent(this@EsperaBatallasActivity, BatallaActivity::class.java)
                    intent.putExtra("entrada", "EXISTENTE")
                    var ene: UsersEntity? = null
                    for (usersEntity in lista) {
                        if (usersEntity.username == item.nombre) {
                            ene = usersEntity
                            break
                        }
                    }
                    intent.putExtra("puerto", ene?.let { it1 -> getPuerto(it1) })
                    startActivity(intent)
                }
            }
            val line = View(applicationContext)
            line.setBackgroundColor(Color.BLACK)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 20)
            params.setMargins(0, 25, 0, 25);
            line.layoutParams = params;
            binding.linearLayoutContainer.addView(cardBinding.root)
            binding.linearLayoutContainer.addView(line)
        }
    }

    private suspend fun getPuerto(ene: UsersEntity): Int {
        val ejecutor =
            AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.JOIN_BATALLA, ene)
        return ejecutor.execute() as Int
    }

    data class CardItem(val imageResId: Int, val nombre: String, val nivel: Int)

    private suspend fun cargarLista() {
        val ejecutor = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.BATALLAS_lISTA_ESPERA,
            Mochila.user.toUsersEntity()
        )
        val ret = ejecutor.execute() as? Set<UsersEntity>
        lista = ret ?: HashSet()
    }


}
