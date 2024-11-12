package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Batallas.InfoBatalla;

import java.io.IOException;
import java.util.Random;

public class PeticionDecision extends PeticionGeneral{

    public static VariablesGeneralesBatallas procesar(VariablesGeneralesBatallas variables) throws IOException, ClassNotFoundException, InterruptedException {
        Random random = new Random();
        InfoBatalla msg = new InfoBatalla();
        msg.tipo=InfoBatalla.TIPOS.CARA_CRUZ;
        if (random.nextInt(2) == 0){
            msg.mensaje=InfoBatalla.USERS.USER1;
            variables.orden=InfoBatalla.USERS.USER1;
        }else{
            msg.mensaje=InfoBatalla.USERS.USER2;
            variables.orden=InfoBatalla.USERS.USER2;
        }
        enviarAmbos(msg,variables);
        variables.usuario1Reader.readObject();
        variables.usuario2Reader.readObject();
        Thread.sleep(2000);
        variables.booleanosGenerales.cara = false;
        variables.booleanosGenerales.partida = true;
        return variables;
    }
}
