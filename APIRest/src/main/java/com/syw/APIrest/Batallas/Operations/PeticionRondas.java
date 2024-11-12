package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Accounts.Services.ServicioUsuarios;
import com.syw.APIrest.Batallas.Constantes.ConstantesBatallas;
import com.syw.APIrest.Batallas.InfoBatalla;
import com.syw.APIrest.Batallas.Services.ServicioBatallas;
import com.syw.APIrest.Stadistics.Constantes.ConstantesXP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class PeticionRondas extends PeticionGeneral {
    static VariablesGeneralesBatallas procesar(VariablesGeneralesBatallas variables) throws IOException, ClassNotFoundException, InterruptedException {
        InfoBatalla msg = new InfoBatalla();
        msg.tipo = InfoBatalla.TIPOS.RONDA;
        msg.ronda = variables.vRondas.ronda;
        if (variables.vRondas.ronda == 0) {
            msg.vida1 = ConstantesBatallas.VIDA;
            msg.vida2 = ConstantesBatallas.VIDA;
            msg.mensaje = ServicioBatallas.getServicioInventarios().getByID(variables.User2.getId()).getPersonaje();
            variables.usuario1Writer.writeObject(msg.toString());
            variables.usuario1Writer.flush();
            msg.mensaje = ServicioBatallas.getServicioInventarios().getByID(variables.User1.getId()).getPersonaje();
            variables.usuario2Writer.writeObject(msg.toString());
            variables.usuario2Writer.flush();
            variables.vRondas.ronda++;
        } else {
            String[] auxTurno = {variables.orden, InfoBatalla.USERS.USER2};
            if (variables.orden.equals(InfoBatalla.USERS.USER2)) {
                auxTurno[1] = InfoBatalla.USERS.USER1;
            }
            for (int i = 0; i < auxTurno.length; i++) {
                msg.turno = auxTurno[i];
                enviarAmbos(msg, variables);
                InfoBatalla aux1 = new InfoBatalla((String) variables.usuario1Reader.readObject());
                InfoBatalla aux2 = new InfoBatalla((String) variables.usuario2Reader.readObject());
                InfoBatalla res = new InfoBatalla();
                res.tipo = InfoBatalla.TIPOS.RONDA;
                res.mensaje = InfoBatalla.MENSAJES.RONDAS.RES;
                res.turno = msg.turno;
                if (res.turno.equals(InfoBatalla.USERS.USER1)) {
                    procesarTurno(InfoBatalla.USERS.USER1, InfoBatalla.USERS.USER2, aux1, aux2, res, variables);
                } else {
                    procesarTurno(InfoBatalla.USERS.USER2, InfoBatalla.USERS.USER1, aux2, aux1, res, variables);
                }
                //Comprobaciones de Fin
                Thread.sleep(3000);
                InfoBatalla msgFinal = new InfoBatalla();
                if (variables.vRondas.vida2 <= 0) {
                    msgFinal.tipo = InfoBatalla.TIPOS.CIERRE;
                    msgFinal.Equipacion1= new ArrayList<>();
                    msgFinal.Equipacion1.add("Monedas: "+ variables.dineroRetenido * 2+ "\nXP: "+ ConstantesXP.BATALLAS);
                    msgFinal.mensaje = InfoBatalla.MENSAJES.CIERRES.VICTORIA;
                    variables.usuario1Writer.writeObject(msgFinal.toString());
                    variables.usuario1Writer.flush();
                    msgFinal.Equipacion1= new ArrayList<>();
                    msgFinal.Equipacion1.add("Monedas: -"+ variables.dineroRetenido + "\nXP: "+ 0);
                    msgFinal.mensaje = InfoBatalla.MENSAJES.CIERRES.DERROTA;
                    variables.usuario2Writer.writeObject(msgFinal.toString());
                    variables.usuario2Writer.flush();
                    ServicioUsuarios servicio = ServicioBatallas.getServicioUsuarios();
                    servicio.modify(variables.User1.getId(), "monedas", String.format("%d", variables.User1.getMonedas() + (variables.dineroRetenido * 2)));
                    servicio.modify(variables.User1.getId(), "nivel", String.format("%d", variables.User1.getNivel() + (ConstantesXP.BATALLAS)));
                    Thread.sleep(5000);
                } else if (variables.vRondas.vida1 <= 0) {
                    msgFinal.tipo = InfoBatalla.TIPOS.CIERRE;
                    msgFinal.Equipacion1= new ArrayList<>();
                    msgFinal.Equipacion1.add("Monedas: "+ variables.dineroRetenido * 2+ "\n XP: "+ ConstantesXP.BATALLAS);
                    msgFinal.mensaje = InfoBatalla.MENSAJES.CIERRES.VICTORIA;                    variables.usuario2Writer.writeObject(msgFinal.toString());
                    variables.usuario2Writer.flush();
                    msgFinal.Equipacion1= new ArrayList<>();
                    msgFinal.Equipacion1.add("Monedas: -"+ variables.dineroRetenido + "\n XP: "+ 0);
                    msgFinal.mensaje = InfoBatalla.MENSAJES.CIERRES.DERROTA;
                    variables.usuario1Writer.writeObject(msgFinal.toString());
                    ServicioUsuarios servicio = ServicioBatallas.getServicioUsuarios();
                    servicio.modify(variables.User2.getId(), "monedas", String.format("%d", variables.User2.getMonedas() + (variables.dineroRetenido * 2)));
                    servicio.modify(variables.User2.getId(), "nivel", String.format("%d", variables.User2.getNivel() + (ConstantesXP.BATALLAS)));
                    Thread.sleep(5000);
                }
            }
        }
        return variables;
    }


    private static void procesarTurno(String atacante, String defensor, InfoBatalla auxAtacante, InfoBatalla auxDefensor, InfoBatalla res, VariablesGeneralesBatallas variables) throws IOException {
        danos(auxAtacante, auxDefensor, res);
        double multiDano = getMultiDanos(atacante, variables);
        double multiDefensa = getMultiDefensa(defensor, variables);
        double danoModificado = res.dano * multiDano * multiDefensa;
        res.dano = (int) danoModificado;
        actualizarVida(defensor, res.dano, variables);
        Respuesta(variables, auxAtacante, auxDefensor, res);
    }

    private static void actualizarVida(String defensor, int dano, VariablesGeneralesBatallas variables) {
        if (defensor.equals(InfoBatalla.USERS.USER1)) {
            variables.vRondas.vida1 -= dano;
        } else {
            variables.vRondas.vida2 -= dano;
        }
    }

    private static double getMultiDanos(String user, VariablesGeneralesBatallas variables) {
        try{
            if (user.equals(InfoBatalla.USERS.USER1)) {
                switch (variables.vSet.arma1.getRareza()) {
                    case "Comun":
                        return ConstantesBatallas.MultiplicadoresDano.COMUN;
                    case "Especial":
                        return ConstantesBatallas.MultiplicadoresDano.ESPECIAL;
                    case "Rara":
                        return ConstantesBatallas.MultiplicadoresDano.RARA;
                    case "Epica":
                        return ConstantesBatallas.MultiplicadoresDano.EPICA;
                    case "Legendaria":
                        return ConstantesBatallas.MultiplicadoresDano.LEGANDARIA;
                }
            } else {
                switch (variables.vSet.arma2.getRareza()) {
                    case "Comun":
                        return ConstantesBatallas.MultiplicadoresDano.COMUN;
                    case "Especial":
                        return ConstantesBatallas.MultiplicadoresDano.ESPECIAL;
                    case "Rara":
                        return ConstantesBatallas.MultiplicadoresDano.RARA;
                    case "Epica":
                        return ConstantesBatallas.MultiplicadoresDano.EPICA;
                    case "Legendaria":
                        return ConstantesBatallas.MultiplicadoresDano.LEGANDARIA;
                }
            }
            return 1.0;
        }catch (NullPointerException e){
            return 1.0;
        }
    }

    private static double getMultiDefensa(String user, VariablesGeneralesBatallas variables) {
        try {
            if (user.equals(InfoBatalla.USERS.USER1)) {
                switch (variables.vSet.armadura1.getRareza()) {
                    case "Comun":
                        return ConstantesBatallas.MultiplicadoresDefensa.COMUN;
                    case "Especial":
                        return ConstantesBatallas.MultiplicadoresDefensa.ESPECIAL;
                    case "Rara":
                        return ConstantesBatallas.MultiplicadoresDefensa.RARA;
                    case "Epica":
                        return ConstantesBatallas.MultiplicadoresDefensa.EPICA;
                    case "Legendaria":
                        return ConstantesBatallas.MultiplicadoresDefensa.LEGANDARIA;
                }
            } else {
                switch (variables.vSet.armadura2.getRareza()) {
                    case "Comun":
                        return ConstantesBatallas.MultiplicadoresDefensa.COMUN;
                    case "Especial":
                        return ConstantesBatallas.MultiplicadoresDefensa.ESPECIAL;
                    case "Rara":
                        return ConstantesBatallas.MultiplicadoresDefensa.RARA;
                    case "Epica":
                        return ConstantesBatallas.MultiplicadoresDefensa.EPICA;
                    case "Legendaria":
                        return ConstantesBatallas.MultiplicadoresDefensa.LEGANDARIA;
                }
            }
            return 1.0;
        }catch (NullPointerException e){
            return 1.0;

        }
    }

    private static void Respuesta(VariablesGeneralesBatallas variables, InfoBatalla auxAtacante, InfoBatalla auxDefensor, InfoBatalla res) throws IOException {
        res.vida1 = variables.vRondas.vida1;
        res.vida2 = variables.vRondas.vida2;

        // Enviar la respuesta al usuario 1
        res.Equipacion1 = new LinkedList<>();
        if (res.turno.equals(InfoBatalla.USERS.USER1)) {
            res.Equipacion1.add(auxDefensor.mensaje);
        } else {
            res.Equipacion1.add(auxAtacante.mensaje);
        }
        variables.usuario1Writer.writeObject(res.toString());
        variables.usuario1Writer.flush();

        // Enviar la respuesta al usuario 2
        res.Equipacion1 = new LinkedList<>();
        if (res.turno.equals(InfoBatalla.USERS.USER2)) {
            res.Equipacion1.add(auxDefensor.mensaje);
        } else {
            res.Equipacion1.add(auxAtacante.mensaje);
        }
        variables.usuario2Writer.writeObject(res.toString());
        variables.usuario2Writer.flush();
    }


    private static void danos(InfoBatalla auxAtacante, InfoBatalla auxDefensor, InfoBatalla res) {
        if (auxAtacante.mensaje.equals(InfoBatalla.MENSAJES.RONDAS.ATQ_CU)) {
            if (auxDefensor.mensaje.equals(InfoBatalla.MENSAJES.RONDAS.DEF_CU)) {
                res.dano = 0;
            } else {
                Random random = new Random();
                res.dano = random.nextInt(ConstantesBatallas.DANO_BASE, ConstantesBatallas.DANO_ALTO);
            }
        } else {
            if (auxDefensor.mensaje.equals(InfoBatalla.MENSAJES.RONDAS.DEF_MAG)) {
                res.dano = 0;
            } else {
                Random random = new Random();
                res.dano = random.nextInt(ConstantesBatallas.DANO_BASE, ConstantesBatallas.DANO_ALTO);
            }
        }
    }
}
