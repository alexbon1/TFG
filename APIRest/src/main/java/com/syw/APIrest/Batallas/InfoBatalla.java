package com.syw.APIrest.Batallas;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class InfoBatalla implements Serializable {


    public static class TIPOS {
        public static final String MENSAJE = "MENSAJE";
        public static final String RONDA = "RONDA";
        public static final String INICIO = "INICIO";
        public static final String APUESTAS = "APUESTAS";
        public static final String CAMBIOS = "CAMBIOS";
        public static final String CIERRE = "CIERRE";
        public static final String CARA_CRUZ = "CARA";
    }

    public static class USERS {
        public static final String SERVER = "SERVER";
        public static final String USER1 = "UNO";
        public static final String USER2 = "DOS";


    }

    public static class MENSAJES {
        public static final String PING = "PING";

        public static class INICIO {
            public static final String USUARIO = "USUARIO";
            public static final String SET = "SET";
            public static final String ESPEANDO_OPONENTE = "Esperando Oponente ";
            public static final String INI = "INICIALIZACION";
        }

        public static class APUESTAS {
            public static final String PRIMERA = "PRIMERA";
            public static final String RONDA = "RONDA";
            public static final String ACEPTAR = "ACEPTAR";
            public static final String APUESTA = "APUESTA";
            public static final String MUY_ALTA = "MUY ALTA";
        }

        public static class RONDAS {
            public static final String INI = "INICIALIZACION";
            public static final String DEF_MAG = "DEFENSA MAGICA";
            public static final String DEF_CU = "DEFENSA CUERPO";
            public static final String ATQ_MAG = "ATAQUE MAGICO";
            public static final String ATQ_CU = "ATAQUE CUERPO";


            public static final String RES = "RESPUESTA";
        }

        public static class CAMBIOS {
            public static final String USER = "USER";
            public static final String INVENTARIO = "INVENTARIO";
        }

        ;

        public static class CIERRES {
            public static final String DESCUSER1 = "Usuario 1 se ha desconectado";
            public static final String DESCUSER2 = "Usuario 2 se ha desconectado";
            public static final String VICTORIA = "Ganador";
            public static final String DERROTA = "Perdedor";


        }
    }

    public InfoBatalla() {
        tipo = null;
        user = null;
        ronda = -100;
        turno = null;
        vida1 = -1000;
        vida2 = -1000;
        dano = -100;
        Equipacion1 = null;
        Equipacion2 = null;
        mensaje = null;
    }

    public InfoBatalla(String cadena) {
        // Dividir la cadena en partes usando un delimitador, por ejemplo, ","
        String[] partes = cadena.split(";");

        // Asignar cada parte a los atributos correspondientes
        this.tipo = partes[0];
        this.user = partes[1];
        this.ronda = Integer.parseInt(partes[2]);
        this.turno = partes[3];
        this.vida1 = Integer.parseInt(partes[4]);
        this.vida2 = Integer.parseInt(partes[5]);
        this.dano = Integer.parseInt(partes[6]);
        String aux1 = partes[7];
        if (!aux1.equals("null")) {
            aux1 = aux1.replace('[', ' ');
            aux1 = aux1.replace(']', ' ');
            aux1 = aux1.trim();
            this.Equipacion1 = Arrays.asList(aux1.split(", "));
        } else {
            this.Equipacion1 = null;
        }
        String aux2 = partes[8];
        if (!aux2.equals("null")) {
            aux2 = aux2.replace('[', ' ');
            aux2 = aux2.replace(']', ' ');
            aux2 = aux2.trim();
            this.Equipacion2 = Arrays.asList(aux2.split(", "));
        } else {
            this.Equipacion2 = null;
        }
        this.mensaje = partes[9];
    }

    public String tipo;// MENSAJE/RONDA/INICIO
    public String user;// USERNAME O SERVER
    public int ronda;
    public String turno;
    public int vida1;
    public int vida2;
    public int dano;
    public List<String> Equipacion1;
    public List<String> Equipacion2;
    public String mensaje;

    @Override
    public String toString() {
        return tipo + ";" +
                user + ";" +
                ronda + ";" +
                turno + ";" +
                vida1 + ";" +
                vida2 + ";" +
                dano + ";" +
                Equipacion1 + ";" +
                Equipacion2 + ";" +
                mensaje;
    }

}
