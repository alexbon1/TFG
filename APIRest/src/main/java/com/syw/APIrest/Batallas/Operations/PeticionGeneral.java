package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Batallas.InfoBatalla;

import java.io.IOException;

public class PeticionGeneral {
    protected static VariablesGeneralesBatallas enviarAmbos(InfoBatalla msg, VariablesGeneralesBatallas variables) throws IOException, IOException {
        variables.usuario1Writer.writeObject(msg.toString());
        variables.usuario1Writer.flush();
        variables.usuario2Writer.writeObject(msg.toString());
        variables.usuario2Writer.flush();
        return variables;
    }
}
