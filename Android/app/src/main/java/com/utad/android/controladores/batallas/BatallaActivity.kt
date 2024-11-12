package com.utad.android.controladores.batallas

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utad.android.R
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.controladores.Inicio_Personaje
import com.utad.android.controladores.dialogs.Error
import com.utad.android.controladores.misiones.MisionCompleta
import com.utad.android.databinding.ActivityBatallaBinding
import com.utad.android.databinding.SubActivityApuestasBatallaBinding
import com.utad.android.databinding.SubActivityCaraCruzBinding
import com.utad.android.databinding.SubActivityRondasBatallaBinding
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.entitys.batallas.InfoBatalla
import com.utad.android.storage.Mochila
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.Socket


class BatallaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBatallaBinding
    private var puerto = -100
    private lateinit var socket: Socket
    private lateinit var Writer: ObjectOutputStream
    private lateinit var Reader: ObjectInputStream
    private var user = InfoBatalla.USERS.USER1;
    private var enemigo: String? = "";
    private var enemigoPERS: String? = "";
    private var equipacionEnemiga: List<String>? = null;
    private var equipacionMia: List<String>? = null;
    private var empezar = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            super.onCreate(savedInstanceState)
            binding = ActivityBatallaBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.buttonRendirse.setOnClickListener {
                cerrarRecursos()
                val intent = Intent(this@BatallaActivity, Inicio_Personaje::class.java)
                startActivity(intent)
            }
            val intent = intent
            if (intent != null) {
                val entrada: String? = intent.getStringExtra("entrada")
                user = if (entrada != null && intent.getStringExtra("entrada").equals("NUEVA")) {
                    InfoBatalla.USERS.USER1
                } else {
                    InfoBatalla.USERS.USER2
                }
                puerto = if (entrada != null && entrada == "NUEVA") {
                    crearBatalla();
                } else {
                    intent.getIntExtra("puerto", -100)
                }
                if (puerto != -100) {
                    withContext(Dispatchers.IO) {
                     try {
                         socket = Socket("10.0.2.2", puerto)
                         Writer = ObjectOutputStream(socket.getOutputStream())
                         Reader = ObjectInputStream(socket.getInputStream())
                         startListening()
                     }catch (e:Exception){
                         Error.showError(this@BatallaActivity, "User 1 se a desconectado")
                         delay(5000)
                         finish()
                     }
                    }
                } else {
                    Error.showError(this@BatallaActivity, "Puerto Extraño o Fallo de Conexion")
                    delay(5000)
                    finish()
                }
            }


        }
    }

    private suspend fun startListening() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                while (socket.isConnected) {
                    val json = Reader.readObject() as? String
                    if (json != null) {
                        val obj = InfoBatalla(json)
                        withContext(Dispatchers.Main) {
                            if (obj.tipo == InfoBatalla.TIPOS.MENSAJE) {
                                procesarMensaje(obj)
                            } else if (obj.tipo == InfoBatalla.TIPOS.INICIO) {
                                procesarInicio(obj)
                            } else if (obj.tipo == InfoBatalla.TIPOS.APUESTAS) {
                                procesarApuestas(obj)
                            } else if (obj.tipo == InfoBatalla.TIPOS.RONDA) {
                                procesarRonda(obj)
                            } else if (obj.tipo == InfoBatalla.TIPOS.CAMBIOS) {
                                procesarCambios(obj)
                            } else if (obj.tipo == InfoBatalla.TIPOS.CARA_CRUZ) {
                                procesarCaraCruz(obj)
                            } else if (obj.tipo == InfoBatalla.TIPOS.CIERRE) {
                                procesarCierres(obj)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    Reader.close()
                    Writer.close()
                    socket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    private fun procesarApuestas(obj: InfoBatalla) {
        when (obj.mensaje) {
            InfoBatalla.MENSAJES.APUESTAS.PRIMERA -> {
                val newContentView =
                    layoutInflater.inflate(R.layout.sub_activity_apuestas_batalla, null)
                val bindingApuestas = SubActivityApuestasBatallaBinding.bind(newContentView)
                bindingApuestas.textViewMonedas2.text = String.format("%d", Mochila.user.monedas)
                bindingApuestas.textViewApuesta.text = "0"
                bindingApuestas.buttonApostar.text = "Primera Apuesta"
                bindingApuestas.button10.setOnClickListener {
                    val args =
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()) + 10
                    if (Mochila.user.monedas < args) {
                        Error.showError(this@BatallaActivity, "No tienes tanto dinero")
                    } else {
                        bindingApuestas.textViewApuesta.text = String.format("%d", args)
                    }
                }
                bindingApuestas.button50.setOnClickListener {
                    val args =
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()) + 50
                    if (Mochila.user.monedas < args) {
                        Error.showError(this@BatallaActivity, "No tienes tanto dinero")
                    } else {
                        bindingApuestas.textViewApuesta.text = String.format("%d", args)
                    }
                }
                bindingApuestas.button100.setOnClickListener {
                    val args =
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()) + 100
                    if (Mochila.user.monedas < args) {
                        Error.showError(this@BatallaActivity, "No tienes tanto dinero")
                    } else {
                        bindingApuestas.textViewApuesta.text = String.format("%d", args)
                    }
                }
                bindingApuestas.buttonApostar.setOnClickListener {
                    subirApuesta(
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()),
                        bindingApuestas
                    )
                }
                setContentView(newContentView)
            }

            InfoBatalla.MENSAJES.APUESTAS.RONDA -> {
                val newContentView =
                    layoutInflater.inflate(R.layout.sub_activity_apuestas_batalla, null)
                val bindingApuestas = SubActivityApuestasBatallaBinding.bind(newContentView)
                bindingApuestas.buttonApostar.text = "Aceptar Apuesta"
                bindingApuestas.textViewMonedas2.text = String.format("%d", Mochila.user.monedas)
                bindingApuestas.textViewApuesta.text = String.format("%d", obj.dano)
                bindingApuestas.buttonRendirse.setOnClickListener {
                    cerrarRecursos()
                    val intent = Intent(this@BatallaActivity, Inicio_Personaje::class.java)
                    startActivity(intent)
                }
                bindingApuestas.button10.setOnClickListener {
                    val args =
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()) + 10
                    if (Mochila.user.monedas < args) {
                        Error.showError(this@BatallaActivity, "No tienes tanto dinero")
                    } else {
                        bindingApuestas.textViewApuesta.text = String.format("%d", args)
                        subirApuesta(args, bindingApuestas)
                    }
                }
                bindingApuestas.button50.setOnClickListener {
                    val args =
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()) + 50
                    if (Mochila.user.monedas < args) {
                        Error.showError(this@BatallaActivity, "No tienes tanto dinero")
                    } else {
                        bindingApuestas.textViewApuesta.text = String.format("%d", args)
                        subirApuesta(args, bindingApuestas)
                    }
                }
                bindingApuestas.button100.setOnClickListener {
                    val args =
                        Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()) + 100
                    if (Mochila.user.monedas < args) {
                        Error.showError(this@BatallaActivity, "No tienes tanto dinero")
                    } else {
                        bindingApuestas.textViewApuesta.text = String.format("%d", args)
                        subirApuesta(args, bindingApuestas)
                    }
                }
                bindingApuestas.buttonApostar.setOnClickListener {
                    aceptarApuesta(Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()))
                }
                setContentView(newContentView)
                if (obj.dano > Mochila.user.monedas) {
                    Error.showError(this@BatallaActivity, "La apuesta es demasiado alta para ti")
                    bindingApuestas.textViewApuesta.text = String.format("%d", Mochila.user.monedas)
                    bindingApuestas.button10.visibility = View.INVISIBLE
                    bindingApuestas.button50.visibility = View.INVISIBLE
                    bindingApuestas.button100.visibility = View.INVISIBLE
                    bindingApuestas.buttonApostar.text = "Aceptar"
                    bindingApuestas.buttonApostar.setOnClickListener {
                        apuestaAlta(Mochila.user.monedas)
                        bindingApuestas.buttonApostar.visibility = View.INVISIBLE
                    }
                }
            }

            InfoBatalla.MENSAJES.APUESTAS.MUY_ALTA -> {
                val newContentView =
                    layoutInflater.inflate(R.layout.sub_activity_apuestas_batalla, null)
                val bindingApuestas = SubActivityApuestasBatallaBinding.bind(newContentView)
                bindingApuestas.buttonApostar.text = "Aceptar"
                bindingApuestas.textViewApuesta.text = String.format("%d", obj.dano)
                bindingApuestas.button10.visibility = View.INVISIBLE
                bindingApuestas.button50.visibility = View.INVISIBLE
                bindingApuestas.button100.visibility = View.INVISIBLE
                bindingApuestas.buttonRendirse.setOnClickListener {
                    cerrarRecursos()
                    val intent = Intent(this@BatallaActivity, Inicio_Personaje::class.java)
                    startActivity(intent)
                }
                bindingApuestas.buttonApostar.setOnClickListener {
                    aceptarApuesta(Integer.parseInt(bindingApuestas.textViewApuesta.text.toString()))
                }
                setContentView(newContentView)
                Error.showError(
                    this@BatallaActivity,
                    "Se a bajado la apuesta por que el otro jugador no tiene suficiente dinero"
                )
            }
        }
    }

    private fun apuestaAlta(apuesta: Int): Int {
        lifecycleScope.launch(Dispatchers.IO) {
            val msg = InfoBatalla()
            msg.user = user
            msg.tipo = InfoBatalla.TIPOS.APUESTAS
            msg.dano = apuesta
            msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.MUY_ALTA
            Writer.writeObject(msg.toString())
            Writer.flush()
        }
        return apuesta
    }


    private fun aceptarApuesta(apuesta: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            var msg = InfoBatalla()
            msg.user = user
            msg.tipo = InfoBatalla.TIPOS.APUESTAS
            msg.dano = apuesta
            msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.ACEPTAR
            Writer.writeObject(msg.toString())
            Writer.flush()
        }
    }

    private fun subirApuesta(apuesta: Int, newBinding: SubActivityApuestasBatallaBinding): Int {
        lifecycleScope.launch(Dispatchers.IO) {
            var msg = InfoBatalla()
            msg.user = user
            msg.tipo = InfoBatalla.TIPOS.APUESTAS
            msg.dano = apuesta
            msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.APUESTA
            Writer.writeObject(msg.toString())
            Writer.flush()
        }
        newBinding.buttonApostar.visibility = View.INVISIBLE
        newBinding.button10.visibility = View.INVISIBLE
        newBinding.button50.visibility = View.INVISIBLE
        newBinding.button100.visibility = View.INVISIBLE
        return apuesta
    }

    private fun procesarInicio(obj: InfoBatalla) {
        lifecycleScope.launch(Dispatchers.IO) {
            when (obj.mensaje) {
                InfoBatalla.MENSAJES.INICIO.USUARIO -> {
                    println(Mochila.user.id)
                    Writer.writeObject(Mochila.user.id)
                }

                InfoBatalla.MENSAJES.INICIO.SET -> {
                    var set = InfoBatalla()
                    set.user = user
                    set.tipo = InfoBatalla.TIPOS.INICIO
                    set.mensaje = InfoBatalla.MENSAJES.INICIO.SET
                    if (user.equals(InfoBatalla.USERS.USER1)) {
                        set.Equipacion1 = Mochila.inventario.setActual.split("|")
                        if (set.Equipacion1.size == 1 && set.Equipacion1[0].equals("")) {
                            set.Equipacion1 = null;
                        }
                    } else if (user.equals(InfoBatalla.USERS.USER2)) {
                        set.Equipacion2 = Mochila.inventario.setActual.split("|")
                        if (set.Equipacion2.size == 1 && set.Equipacion2[0].equals("")) {
                            set.Equipacion2 = null;
                        }
                    }
                    Writer.writeObject(set.toString())
                    Writer.flush()
                }

                InfoBatalla.MENSAJES.INICIO.ESPEANDO_OPONENTE -> {
                    withContext(Dispatchers.Main) {
                        binding.textViewEspera.text = obj.mensaje
                    }
                }

                InfoBatalla.MENSAJES.INICIO.INI -> {
                    enemigo = obj.user;
                    if (user == InfoBatalla.USERS.USER1) {
                        equipacionEnemiga = obj.Equipacion2;
                        equipacionMia = obj.Equipacion1;
                    } else {
                        equipacionEnemiga = obj.Equipacion1;
                        equipacionMia = obj.Equipacion2;
                    }
                }
            }
        }
    }

    private fun procesarCaraCruz(msg: InfoBatalla) {
        val newContentView = layoutInflater.inflate(R.layout.sub_activity_cara_cruz, null)
        val binding2 = SubActivityCaraCruzBinding.bind(newContentView)
        empezar = msg.mensaje
        binding2.imageView10.animate().alpha(1f).setDuration(2000).withEndAction {
            if (empezar.equals(user)) {
                startDotAnimation(binding2, "Atacante")
            } else {
                startDotAnimation(binding2, "Defensor")
            }
        }
        setContentView(newContentView)
    }

    private fun startDotAnimation(binding2: SubActivityCaraCruzBinding, eres: String) {
        val handler = Handler(Looper.getMainLooper())
        var dotCount = 0

        val runnable = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (dotCount < 3) {
                    binding2.textView4.text = binding2.textView4.text.toString() + "."
                    dotCount++
                    handler.postDelayed(this, 1000)
                } else {
                    binding2.textView4.text = eres
                    lifecycleScope.launch(Dispatchers.IO) {
                        Writer.writeObject("")
                        Writer.flush()
                    }
                }
            }
        }
        handler.post(runnable)
    }

    var vidaYo = 0
    var vidaEn = 0
    var VIDA_MAX = 0
    var ultima_accion: String? = null

    @SuppressLint("InflateParams", "DiscouragedApi", "SetTextI18n")
    private fun procesarRonda(msg: InfoBatalla) {
        val newContentView = layoutInflater.inflate(R.layout.sub_activity_rondas_batalla, null)
        val binding2 = SubActivityRondasBatallaBinding.bind(newContentView)
        if (msg.ronda == 0) {
            if (user == InfoBatalla.USERS.USER1) {
                vidaYo = msg.vida1
                vidaEn = msg.vida2
            } else {
                vidaYo = msg.vida2
                vidaEn = msg.vida1
            }
            enemigoPERS = msg.mensaje
            VIDA_MAX = vidaYo
        } else {
            if (msg.mensaje.equals(InfoBatalla.MENSAJES.RONDAS.RES)) {
                var resourceId = 0
                when (msg.Equipacion1[0]) {
                    InfoBatalla.MENSAJES.RONDAS.ATQ_CU -> {
                        resourceId = resources.getIdentifier(
                            "drawable/ataque_fisico", null, packageName
                        )
                        binding2.textViewAccion.setTextColor(Color.RED)
                    }

                    InfoBatalla.MENSAJES.RONDAS.ATQ_MAG -> {
                        resourceId = resources.getIdentifier(
                            "drawable/ataque_magico", null, packageName
                        )
                        binding2.textViewAccion.setTextColor(Color.RED)
                    }

                    InfoBatalla.MENSAJES.RONDAS.DEF_MAG -> {
                        resourceId = resources.getIdentifier(
                            "drawable/defensa_magica", null, packageName
                        )
                    }

                    InfoBatalla.MENSAJES.RONDAS.DEF_CU -> {
                        resourceId = resources.getIdentifier(
                            "drawable/defensa_fisica", null, packageName
                        )
                    }
                }
                var resourceId2 = 0
                when (ultima_accion) {
                    InfoBatalla.MENSAJES.RONDAS.ATQ_CU -> {
                        resourceId2 = resources.getIdentifier(
                            "drawable/ataque_fisico", null, packageName
                        )
                    }

                    InfoBatalla.MENSAJES.RONDAS.ATQ_MAG -> {
                        resourceId2 = resources.getIdentifier(
                            "drawable/ataque_magico", null, packageName
                        )
                    }

                    InfoBatalla.MENSAJES.RONDAS.DEF_MAG -> {
                        resourceId2 = resources.getIdentifier(
                            "drawable/defensa_magica", null, packageName
                        )
                    }

                    InfoBatalla.MENSAJES.RONDAS.DEF_CU -> {
                        resourceId2 = resources.getIdentifier(
                            "drawable/defensa_fisica", null, packageName
                        )
                    }
                }
                binding2.imageViewAccionYo.setImageResource(resourceId2)
                binding2.imageViewAccionCont.setImageResource(resourceId)
                if (msg.dano>0){
                    binding2.textViewAccion.text = "-" + String.format("%d", msg.dano)
                }else{
                    binding2.textViewAccion.setTextColor(Color.YELLOW)
                    binding2.textViewAccion.text="Daño Esquivado"
                }
                if (user == InfoBatalla.USERS.USER1) {
                    vidaYo = msg.vida1
                    vidaEn = msg.vida2
                } else {
                    vidaYo = msg.vida2
                    vidaEn = msg.vida1
                }
            } else {
                if (msg.turno == user) {
                    //Ataque-
                    setBindingAtaque(binding2)
                } else {
                    //Defensa
                    setBindingDefensa(binding2)
                }
            }
        }
        binding2.progressBarYO.max = VIDA_MAX
        binding2.progressBarYO.setProgress(vidaYo, false)
        binding2.progressBarEnemigo.max = VIDA_MAX
        binding2.progressBarEnemigo.setProgress(vidaEn, false)
        val resourceId = resources.getIdentifier(
            "drawable/" + Mochila.inventario.personaje, null, packageName
        )
        binding2.imageViewYOJP.setImageResource(resourceId)
        val resourceId2 = resources.getIdentifier(
            "drawable/$enemigoPERS", null, packageName
        )
        binding2.imageViewEnmigoJP.setImageResource(resourceId2)
        //Armas y Armaduras
        if (equipacionMia != null) {
            if (equipacionMia!![0] != "null") {
                val resourceId3 = resources.getIdentifier(
                    "drawable/" + equipacionMia!![0], null, packageName
                )
                binding2.imageViewArmaYO.setImageResource(resourceId3)
                val resourceIdRar = resources.getIdentifier(
                    "drawable/" + equipacionMia!![2].lowercase(), null, packageName
                )
                binding2.imageViewArmaYO.setBackgroundResource(resourceIdRar)
            }
            if (equipacionMia!![1] != "null") {
                val resourceId3 = resources.getIdentifier(
                    "drawable/" + equipacionMia!![1], null, packageName
                )
                binding2.imageViewArmaduraYO.setImageResource(resourceId3)
                val resourceIdRar = resources.getIdentifier(
                    "drawable/" + equipacionMia!![3].lowercase(), null, packageName
                )
                binding2.imageViewArmaduraYO.setBackgroundResource(resourceIdRar)
            }
        }
        if (equipacionEnemiga != null) {
            if (equipacionEnemiga!![0] != "null") {
                val resourceId3 = resources.getIdentifier(
                    "drawable/" + equipacionEnemiga!![0], null, packageName
                )
                binding2.imageViewArmaEN.setImageResource(resourceId3)
                val resourceIdRar = resources.getIdentifier(
                    "drawable/" + equipacionEnemiga!![2].lowercase(), null, packageName
                )
                binding2.imageViewArmaEN.setBackgroundResource(resourceIdRar)
            }
            if (equipacionEnemiga!![1] != "null") {
                val resourceId3 = resources.getIdentifier(
                    "drawable/" + equipacionEnemiga!![1], null, packageName
                )
                binding2.imageViewArmaduraEN.setImageResource(resourceId3)
                val resourceIdRar = resources.getIdentifier(
                    "drawable/" + equipacionEnemiga!![3].lowercase(), null, packageName
                )
                binding2.imageViewArmaduraEN.setBackgroundResource(resourceIdRar)
            }
        }
        setContentView(newContentView)
    }


    @SuppressLint("DiscouragedApi")
    private fun setBindingAtaque(binding2: SubActivityRondasBatallaBinding) {
        val resourceId = resources.getIdentifier(
            "drawable/ataque_fisico", null, packageName
        )
        binding2.imageViewFisico.setImageResource(resourceId)
        binding2.imageViewFisico.setOnClickListener {
            mandarAccion(InfoBatalla.MENSAJES.RONDAS.ATQ_CU)
            binding2.imageViewMagica.visibility = View.INVISIBLE
            binding2.imageViewFisico.visibility = View.INVISIBLE
            binding2.imageViewAccionYo.setImageResource(resourceId)
        }
        val resourceId2 = resources.getIdentifier(
            "drawable/ataque_magico", null, packageName
        )
        binding2.imageViewMagica.setOnClickListener {
            mandarAccion(InfoBatalla.MENSAJES.RONDAS.ATQ_MAG)
            binding2.imageViewMagica.visibility = View.INVISIBLE
            binding2.imageViewFisico.visibility = View.INVISIBLE
            binding2.imageViewAccionYo.setImageResource(resourceId2)
        }
        binding2.textViewAccion.text = "Ataca"
        binding2.imageViewMagica.setImageResource(resourceId2)
    }

    private fun mandarAccion(ataque: String) {
        var msg = InfoBatalla();
        msg.tipo = InfoBatalla.TIPOS.RONDA
        msg.mensaje = ataque
        ultima_accion = ataque
        lifecycleScope.launch(Dispatchers.IO) {
            Writer.writeObject(msg.toString())
            Writer.flush()
        }
    }

    private fun setBindingDefensa(binding2: SubActivityRondasBatallaBinding) {
        val resourceId = resources.getIdentifier(
            "drawable/defensa_fisica", null, packageName
        )
        binding2.imageViewFisico.setOnClickListener {
            mandarAccion(InfoBatalla.MENSAJES.RONDAS.DEF_CU)
            binding2.imageViewMagica.visibility = View.INVISIBLE
            binding2.imageViewFisico.visibility = View.INVISIBLE
            binding2.imageViewAccionYo.setImageResource(resourceId)
        }
        binding2.imageViewFisico.setImageResource(resourceId)
        val resourceId2 = resources.getIdentifier(
            "drawable/defensa_magica", null, packageName
        )
        binding2.imageViewMagica.setImageResource(resourceId2)
        binding2.imageViewMagica.setOnClickListener {
            mandarAccion(InfoBatalla.MENSAJES.RONDAS.DEF_MAG)
            binding2.imageViewMagica.visibility = View.INVISIBLE
            binding2.imageViewFisico.visibility = View.INVISIBLE
            binding2.imageViewAccionYo.setImageResource(resourceId2)
        }
        binding2.textViewAccion.text = "Defiendete"
    }

    private fun procesarMensaje(msg: InfoBatalla) {
        when (msg.user) {
            InfoBatalla.USERS.SERVER -> {
                when (msg.mensaje) {
                    InfoBatalla.MENSAJES.PING -> {

                    }
                }
            }

            InfoBatalla.USERS.USER1 -> {}
            InfoBatalla.USERS.USER2 -> {}
            else -> {}
        }

    }

    private fun procesarCambios(msg: InfoBatalla) {
        lifecycleScope.launch {
            when (msg.mensaje) {
                InfoBatalla.MENSAJES.CAMBIOS.USER -> {
                    val args = arrayOf(Mochila.user.username, Mochila.user.pwd)
                    val ejecutor = AsincronoEjecutorDeConexiones(
                        AsincronoEjecutorDeConexiones.METODOS.LOGIN, args
                    )
                    Mochila.user = ejecutor.execute() as UsuarioJSON?
                }

                InfoBatalla.MENSAJES.CAMBIOS.INVENTARIO -> {
                    val ejecutor = AsincronoEjecutorDeConexiones(
                        AsincronoEjecutorDeConexiones.METODOS.GET_INVENTARIO, Mochila.user.id
                    )
                    Mochila.inventario = ejecutor.execute() as InventariosEntity?
                }
            }
        }
    }

    private suspend fun procesarCierres(obj: InfoBatalla) {
        when (obj.mensaje) {
             InfoBatalla.MENSAJES.CIERRES.DESCUSER1 -> {
                 setContentView(binding.root)
                 binding.textViewEspera.text = obj.mensaje;
                 delay(10000)
                 val intent = Intent(this@BatallaActivity, Inicio_Personaje::class.java)
                 startActivity(intent)
             }

             InfoBatalla.MENSAJES.CIERRES.DESCUSER2 -> {
                 setContentView(binding.root)
                 binding.textViewEspera.text = obj.mensaje;
                 delay(10000)
                 val intent = Intent(this@BatallaActivity, Inicio_Personaje::class.java)
                 startActivity(intent)
             }
             InfoBatalla.MENSAJES.CIERRES.VICTORIA->{
                 val intent = Intent(this@BatallaActivity, MisionCompleta::class.java).apply {
                     putExtra("RECOMPENSAS", obj.Equipacion1[0])
                     putExtra("TITULO", obj.mensaje)
                     putExtra("FUEGOS",true)
                 }
                 startActivity(intent)
             }
            InfoBatalla.MENSAJES.CIERRES.DERROTA->{
                val intent = Intent(this@BatallaActivity, MisionCompleta::class.java).apply {
                    putExtra("RECOMPENSAS", obj.Equipacion1[0])
                    putExtra("TITULO", obj.mensaje)
                    putExtra("FUEGOS",false)
                }
                startActivity(intent)
            }

         }
        binding.textViewEspera.text = obj.mensaje;
        delay(10000)
        val msg = InfoBatalla()
        msg.mensaje = InfoBatalla.MENSAJES.CAMBIOS.USER
        procesarCambios(msg)
        msg.mensaje = InfoBatalla.MENSAJES.CAMBIOS.INVENTARIO
        procesarCambios(msg)
        cerrarRecursos()
    }

    private fun cerrarRecursos() {
        Writer.close()
        Reader.close()
        socket.close()
    }

    private suspend fun crearBatalla(): Int {
        val ejecutor = AsincronoEjecutorDeConexiones(
            AsincronoEjecutorDeConexiones.METODOS.BATALLAS_NUEVA_BATALLA, Mochila.user
        )
        val ret = ejecutor.execute() as Int
        return ret
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }

}
