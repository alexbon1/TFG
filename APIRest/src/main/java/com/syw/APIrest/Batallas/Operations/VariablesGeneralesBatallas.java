package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Batallas.Constantes.ConstantesBatallas;
import com.syw.APIrest.Batallas.InfoBatalla;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class VariablesGeneralesBatallas {
    public VariablesApuestas vApuestas;
    public VariablesComprobante vComprobante;
    public VariablesSET vSet;
    public VariablesRondas vRondas;
    public ServerSocket serverSocket;
    public Socket usuario1Socket;
    public Socket usuario2Socket;
    public ObjectOutputStream usuario1Writer;
    public ObjectOutputStream usuario2Writer;
    public ObjectInputStream usuario1Reader;
    public ObjectInputStream usuario2Reader;
    public int puerto;
    public int dineroRetenido = 0;
    public UsersEntity User1;
    public UsersEntity User2;
    public List<String> SET1;
    public List<String> SET2;
    public String orden;

    public VariablesGeneralesBatallas() {
        vApuestas = new VariablesApuestas();
        vComprobante = new VariablesComprobante();
        booleanosGenerales = new booleanosGenerales();
        vSet = new VariablesSET();
        vRondas = new VariablesRondas();
    }

    public booleanosGenerales booleanosGenerales;

    public class booleanosGenerales {
        public boolean start = false;
        public boolean connection = true;
        public boolean apuestas = false;
        public boolean partida = false;
        public boolean enCurso = false;
        public boolean cerrada = false;

        public boolean cara = false;
    }

    public class VariablesApuestas {
        public boolean PrimeraApuesta = true;
        public boolean apuestaIsUser1 = true;
    }

    public class VariablesComprobante {
        public boolean revisar1 = false;
        public boolean revisar2 = false;
        public boolean cerrada = false;
    }

    public class VariablesSET {
        public ArmasEntity arma1 = null;
        public ArmadurasEntity armadura1 = null;
        public ArmasEntity arma2 = null;
        public ArmadurasEntity armadura2 = null;
    }

    public class VariablesRondas {
        public int ronda = 0;
        public int vida1 = ConstantesBatallas.VIDA;
        public int vida2 = ConstantesBatallas.VIDA;


    }
}
