package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Batallas.InfoBatalla;
import com.syw.APIrest.Batallas.Threads.HiloComprobante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PeticionConexion {
    public static void procesar(VariablesGeneralesBatallas variables) throws IOException {
        System.out.println("Puerto : " + variables.puerto + " ocupado");
        variables.usuario1Socket = variables.serverSocket.accept();
        System.out.println("User1 En línea");
        variables.usuario1Writer = new ObjectOutputStream(variables.usuario1Socket.getOutputStream());
        variables.usuario1Reader = new ObjectInputStream(variables.usuario1Socket.getInputStream());
        variables.vComprobante.revisar1 = true;

        InfoBatalla msg = new InfoBatalla();
        msg.tipo = InfoBatalla.TIPOS.INICIO;
        msg.user = InfoBatalla.USERS.SERVER;
        msg.mensaje = InfoBatalla.MENSAJES.INICIO.ESPEANDO_OPONENTE;
        variables.usuario1Writer.writeObject(msg.toString());
        variables.usuario1Writer.flush();
        HiloComprobante hiloComprobante = new HiloComprobante(variables);
        hiloComprobante.start();
        variables.usuario2Socket = variables.serverSocket.accept();
        System.out.println("User2 En línea");
        variables.usuario2Writer = new ObjectOutputStream(variables.usuario2Socket.getOutputStream());
        variables.usuario2Reader = new ObjectInputStream(variables.usuario2Socket.getInputStream());
        variables.vComprobante.revisar2 = true;
        hiloComprobante.variables = variables;
        variables.usuario2Writer.writeObject(msg.toString());
        variables.usuario2Writer.flush();

        variables.booleanosGenerales.connection = false;
        variables.booleanosGenerales.start = true;

    }
}
