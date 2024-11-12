package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Batallas.InfoBatalla;
import com.syw.APIrest.Batallas.Services.ServicioBatallas;

import java.io.IOException;

public class PeticionApuestas extends PeticionGeneral {

    public static VariablesGeneralesBatallas procesar(VariablesGeneralesBatallas variables) throws IOException, ClassNotFoundException {
        if (variables.vApuestas.PrimeraApuesta) {
            InfoBatalla msg = new InfoBatalla();
            msg.tipo = InfoBatalla.TIPOS.APUESTAS;
            msg.user = InfoBatalla.USERS.SERVER;
            msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.PRIMERA;
            variables.usuario1Writer.writeObject(msg.toString());
            variables.usuario1Writer.flush();
            String ret = (String) variables.usuario1Reader.readObject();
            InfoBatalla aux = new InfoBatalla(ret);
            msg = new InfoBatalla();
            msg.tipo = InfoBatalla.TIPOS.APUESTAS;
            msg.user = InfoBatalla.USERS.USER1;
            msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.RONDA;
            msg.dano = aux.dano;
            variables.usuario2Writer.writeObject(msg.toString());
            variables.usuario2Writer.flush();
            variables.vApuestas.PrimeraApuesta = false;
            variables.vApuestas.apuestaIsUser1 = false;
        } else {
            String ret;
            if (variables.vApuestas.apuestaIsUser1) {
                ret = (String) variables.usuario1Reader.readObject();
            } else {
                ret = (String) variables.usuario2Reader.readObject();
            }
            InfoBatalla aux1 = new InfoBatalla(ret);
            InfoBatalla msg = new InfoBatalla();
            if (aux1.user.equals(InfoBatalla.USERS.USER1)) {
                switch (aux1.mensaje) {
                    case InfoBatalla.MENSAJES.APUESTAS.ACEPTAR -> {
                        variables.booleanosGenerales.apuestas = false;
                        variables.booleanosGenerales.cara = true;
                        variables = retenerDinero(aux1.dano, variables);
                    }
                    case InfoBatalla.MENSAJES.APUESTAS.APUESTA -> {
                        msg.user = InfoBatalla.USERS.USER1;
                        msg.tipo = InfoBatalla.TIPOS.APUESTAS;
                        msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.RONDA;
                        msg.dano = aux1.dano;
                        variables.vApuestas.apuestaIsUser1 = false;
                    }
                    case InfoBatalla.MENSAJES.APUESTAS.MUY_ALTA -> {
                        msg.user = InfoBatalla.USERS.USER1;
                        msg.tipo = InfoBatalla.TIPOS.APUESTAS;
                        msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.MUY_ALTA;
                        msg.dano = aux1.dano;
                        variables.vApuestas.apuestaIsUser1 = false;
                    }
                }
                variables.usuario2Writer.writeObject(msg.toString());
                variables.usuario2Writer.flush();
            } else {
                switch (aux1.mensaje) {
                    case InfoBatalla.MENSAJES.APUESTAS.ACEPTAR -> {
                        variables.booleanosGenerales.apuestas = false;
                        variables.booleanosGenerales.cara = true;
                        variables = retenerDinero(aux1.dano, variables);
                    }
                    case InfoBatalla.MENSAJES.APUESTAS.APUESTA -> {
                        msg.user = InfoBatalla.USERS.USER2;
                        msg.tipo = InfoBatalla.TIPOS.APUESTAS;
                        msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.RONDA;
                        msg.dano = aux1.dano;
                        variables.vApuestas.apuestaIsUser1 = true;
                    }
                    case InfoBatalla.MENSAJES.APUESTAS.MUY_ALTA -> {
                        msg.user = InfoBatalla.USERS.USER2;
                        msg.tipo = InfoBatalla.TIPOS.APUESTAS;
                        msg.mensaje = InfoBatalla.MENSAJES.APUESTAS.MUY_ALTA;
                        msg.dano = aux1.dano;
                        variables.vApuestas.apuestaIsUser1 = true;
                    }
                }
                variables.usuario1Writer.writeObject(msg.toString());
                variables.usuario1Writer.flush();
            }
        }

        return variables;
    }

    private static VariablesGeneralesBatallas retenerDinero(int monedas, VariablesGeneralesBatallas variables) throws IOException {
        variables.dineroRetenido = monedas;
        variables.User1.setMonedas(variables.User1.getMonedas() - monedas);
        variables.User2.setMonedas(variables.User2.getMonedas() - monedas);
        ServicioBatallas.getServicioUsuarios().modify(variables.User1.getId(), "monedas", String.format("%d", variables.User1.getMonedas()));
        ServicioBatallas.getServicioUsuarios().modify(variables.User2.getId(), "monedas", String.format("%d", variables.User2.getMonedas()));
        variables.User1 = ServicioBatallas.getServicioUsuarios().getByID(variables.User1.getId());
        variables.User2 = ServicioBatallas.getServicioUsuarios().getByID(variables.User2.getId());
        InfoBatalla msg = new InfoBatalla();
        msg.user = InfoBatalla.USERS.SERVER;
        msg.tipo = InfoBatalla.TIPOS.CAMBIOS;
        msg.mensaje = InfoBatalla.MENSAJES.CAMBIOS.USER;
        variables = enviarAmbos(msg, variables);
        return variables;
    }
}
