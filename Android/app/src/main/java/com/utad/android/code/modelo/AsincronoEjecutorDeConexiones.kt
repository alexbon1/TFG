package com.utad.android.code.modelo

import com.utad.android.entitys.ArmadurasEntity
import com.utad.android.entitys.ArmasEntity
import com.utad.android.entitys.InventariosEntity
import com.utad.android.entitys.JSON.BoxJSON
import com.utad.android.entitys.JSON.MisionCompletaJSON
import com.utad.android.entitys.JSON.MisionesDiariasJSON
import com.utad.android.entitys.JSON.TiendaDiaJSON
import com.utad.android.entitys.JSON.UsuarioJSON
import com.utad.android.entitys.PersonajesEntity
import com.utad.android.entitys.UsersEntity
import com.utad.android.storage.Mochila
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsincronoEjecutorDeConexiones(private val metodo: METODOS, private val args: Any) {

    enum class METODOS {
        EXISTE_USERNAME,
        LOGIN,
        SIGN_IN,
        ADD_PERSONAJE_TIPO,
        CONSULTAR_PERSONAJE_IMAGEN,
        GET_INVENTARIO,
        MISION_DIARIA_BASE,
        BATALLAS_lISTA_ESPERA,
        BATALLAS_NUEVA_BATALLA,
        CONSULTAR_TIENDA_DIARIA,
        GET_ARMAS, GET_ARMADURAS,
        MODIFICAR_MONEDAS,
        JOIN_BATALLA,
        MODIFY_INVENTARIO,
        MODIFY_ALL_USER,
        MODIFY_PWD,
        COMPRAR,
        COMPRARBOX,
        MISION_COMPLETADA,
        MISION_EXTRA,
        TOKEN
    }

    companion object {
        private val metodoMap =
            mapOf(METODOS.EXISTE_USERNAME to { args: Any -> existeUsername(args) },
                METODOS.LOGIN to { args: Any -> login(args) },
                METODOS.SIGN_IN to { args: Any -> signIn(args as UsersEntity) },
                METODOS.ADD_PERSONAJE_TIPO to { args: Any -> addPersonajeYTipo(args as Array<String>) },
                METODOS.CONSULTAR_PERSONAJE_IMAGEN to { args: Any ->
                    consultarPersonajePorImagen(args as String)
                },
                METODOS.GET_INVENTARIO to { args: Any -> consultaInventarioById(args as Int) },
                METODOS.MISION_DIARIA_BASE to { _: Any -> misionDiariaBase() },
                METODOS.MISION_EXTRA to { _: Any -> misionExtra() },
                METODOS.BATALLAS_lISTA_ESPERA to { args: Any -> batallasListaEspera(args as UsersEntity) },
                METODOS.BATALLAS_NUEVA_BATALLA to { args: Any -> batallasNuevaBatalla(args as UsersEntity) },
                METODOS.CONSULTAR_TIENDA_DIARIA to { consultarTiendaDiaria() },
                METODOS.GET_ARMAS to { args: Any -> getArmas(args as List<String>) },
                METODOS.GET_ARMADURAS to { args: Any -> getArmaduras(args as List<String>) },
                METODOS.MODIFICAR_MONEDAS to { args: Any -> modifyMonedas(args as Int) },
                METODOS.JOIN_BATALLA to { args: Any -> joinBatalla(args as UsersEntity) },
                METODOS.MODIFY_INVENTARIO to { args: Any -> modifyInventario(args as Array<String>) },
                METODOS.MODIFY_ALL_USER to { args: Any -> modifyAllUser(args as UsersEntity) },
                METODOS.MODIFY_PWD to { args: Any -> modifyPwd(args as String) },
                METODOS.COMPRAR to { args: Any -> comprar(args) },
                METODOS.COMPRARBOX to { args: Any -> comprarBox(args as String) },
                METODOS.MISION_COMPLETADA to { args: Any -> completarMision(args as MisionCompletaJSON) },
                METODOS.TOKEN to { _: Any -> getToken() })

        private fun getToken() {
            ConexionToken.solicitarToken()
        }

        private fun misionExtra():MisionesDiariasJSON? {
            return try {
                ConexionMisiones.misionExtra()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


        // Método para obtener la misión diaria base
        private fun misionDiariaBase(): MisionesDiariasJSON? {
            return try {
                ConexionMisiones.misionDiariaBase()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para consultar el inventario por ID
        private fun consultaInventarioById(id: Int): InventariosEntity? {
            return try {
                ConexionInventarios.getInventarioByID(id)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para registrar un usuario
        private fun signIn(args: UsersEntity): Boolean {
            return try {
                ConexionCuentas.signIn(args)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Método para verificar si existe un nombre de usuario
        private fun existeUsername(args: Any): Boolean {
            return try {
                if (args is Array<*> && args.isNotEmpty() && args[0] is String) {
                    ConexionCuentas.existeUser(args[0] as String)
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Método para realizar el login
        private fun login(args: Any): UsuarioJSON? {
            return try {
                if (args is Array<*> && args.size == 2 && args[0] is String && args[1] is String) {
                    val user = UsersEntity(args[0] as String, args[1] as String)
                    ConexionCuentas.login(user)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para agregar un personaje y su tipo
        private fun addPersonajeYTipo(args: Array<String>): Boolean {
            return try {
                val ret1 = ConexionCuentas.modifyUsuario(args[0].toInt(), args[3], args[4])
                val ret2 = ConexionInventarios.modifyInventario(args[0].toInt(), args[1], args[2])
                ret1 != null && ret2 != null
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Método para consultar un personaje por imagen
        private fun consultarPersonajePorImagen(id: String): PersonajesEntity? {
            return try {
                ConexionPersonajes.getPersonajePorImagen(id)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para obtener la lista de espera de batallas
        private fun batallasListaEspera(user: UsersEntity): Set<UsersEntity>? {
            return try {
                ConexionBatallas.UsersEspera(user)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para iniciar una nueva batalla
        private fun batallasNuevaBatalla(user: UsersEntity): Int {
            return try {
                ConexionBatallas.BatallaNueva(user)
            } catch (e: Exception) {
                e.printStackTrace()
                -1
            }
        }

        // Método para consultar la tienda diaria
        private fun consultarTiendaDiaria(): TiendaDiaJSON? {
            return try {
                ConexionTienda.getTiendaDiaria()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para obtener armas
        private fun getArmas(strings: List<String>): List<ArmasEntity>? {
            return try {
                ConexionEquipo.getArmas(strings)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para obtener armaduras
        private fun getArmaduras(strings: List<String>): List<ArmadurasEntity>? {
            return try {
                ConexionEquipo.getArmaduras(strings)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para modificar monedas
        private fun modifyMonedas(i: Int): Boolean {
            return try {
                val user2 = ConexionCuentas.modifyUsuario(
                    Mochila.user.id, "Monedas", String.format("%d", Mochila.user.monedas - i)
                )
                if (user2 != null) {
                    if (user2.monedas == Mochila.user.monedas) {
                        false
                    } else {
                        Mochila.user = user2
                        true
                    }
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Método para unirse a una batalla
        private fun joinBatalla(usersEntity: UsersEntity): Int {
            return try {
                ConexionBatallas.JoinBatalla(usersEntity)
            } catch (e: Exception) {
                e.printStackTrace()
                -1
            }
        }

        // Método para modificar un inventario
        private fun modifyInventario(strings: Array<String>): InventariosEntity? {
            return try {
                ConexionInventarios.modifyInventario(strings[0].toInt(), strings[1], strings[2])
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para modificar todos los datos de un usuario
        private fun modifyAllUser(args: UsersEntity): Boolean {
            return try {
                val fieldsToUpdate = mapOf(
                    "nombre" to args.nombre,
                    "username" to args.username,
                    "apellidos" to args.apellidos,
                    "email" to args.email,
                    "fechaDeNacimiento" to args.fechaDeNacimiento
                )

                for ((field, value) in fieldsToUpdate) {
                    if (value != Mochila.user::class.java.getDeclaredField(field)
                            .get(Mochila.user)
                    ) {
                        if (ConexionCuentas.modifyUsuario(args.id, field, value) == null) {
                            return false
                        }
                    }
                }

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Método para modificar la contraseña
        private fun modifyPwd(pwd: String): Boolean {
            return try {
                val user2 = ConexionCuentas.modifyUsuario(Mochila.user.id, "pwd", pwd)
                if (user2 != null && user2 != Mochila.user) {
                    Mochila.user = user2
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // Método para realizar una compra
        private fun comprar(compra: Any): InventariosEntity? {
            return try {
                ConexionTienda.comprar(compra)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // Método para comprar una caja (barata o cara)
        private fun comprarBox(tipo: String): BoxJSON? {
            return try {
                when (tipo) {
                    "barata" -> ConexionTienda.comprarBoxBarata()
                    "cara" -> ConexionTienda.comprarBoxCara()
                    else -> null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        private fun completarMision(Mision: MisionCompletaJSON):MisionCompletaJSON {
            return ConexionMisiones.completarMision(Mision)
        }
    }

    // Ejecuta el método correspondiente de forma asíncrona
    suspend fun execute(): Any? {
        return withContext(Dispatchers.Default) {
            metodoMap[metodo]?.invoke(args)
        }
    }
}
