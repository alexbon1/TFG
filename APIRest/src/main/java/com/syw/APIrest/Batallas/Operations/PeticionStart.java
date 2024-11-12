package com.syw.APIrest.Batallas.Operations;

import com.syw.APIrest.Batallas.InfoBatalla;
import com.syw.APIrest.Batallas.Services.ServicioBatallas;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PeticionStart extends PeticionGeneral {


    public static VariablesGeneralesBatallas procesar(VariablesGeneralesBatallas variables) throws IOException, ClassNotFoundException {
        peticionUsuarios(variables);
        return peticionSET(variables);
    }

    private static VariablesGeneralesBatallas peticionSET(VariablesGeneralesBatallas variables) throws IOException, ClassNotFoundException {
        InfoBatalla msg = new InfoBatalla();
        msg.tipo = InfoBatalla.TIPOS.INICIO;
        msg.user = InfoBatalla.USERS.SERVER;
        msg.mensaje = InfoBatalla.MENSAJES.INICIO.SET;
        enviarAmbos(msg, variables);
        String set1 = (String) variables.usuario1Reader.readObject();
        String set2 = (String) variables.usuario2Reader.readObject();
        variables.SET1 = (new InfoBatalla(set1)).Equipacion1;
        variables.SET2 = (new InfoBatalla(set2)).Equipacion2;
        variables = sacarPartesSets(variables);
        envioDatosEnemigos(variables);
        return variables;
    }


    private static void envioDatosEnemigos(VariablesGeneralesBatallas variables) throws IOException {
        InfoBatalla msg;
        msg = new InfoBatalla();
        msg.tipo = InfoBatalla.TIPOS.INICIO;
        msg.mensaje = InfoBatalla.MENSAJES.INICIO.INI;
        msg.Equipacion1 = imagenesEquipacion(variables.SET1);
        msg.Equipacion2 = imagenesEquipacion(variables.SET2);
        msg.user = variables.User2.getUsername();
        variables.usuario1Writer.writeObject(msg.toString());
        variables.usuario1Writer.flush();
        msg.user = variables.User1.getUsername();
        variables.usuario2Writer.writeObject(msg.toString());
        variables.usuario2Writer.flush();
    }

    private static void peticionUsuarios(VariablesGeneralesBatallas variables) throws IOException, ClassNotFoundException {
        InfoBatalla msg = new InfoBatalla();
        msg.tipo = InfoBatalla.TIPOS.INICIO;
        msg.mensaje = InfoBatalla.MENSAJES.INICIO.USUARIO;
        msg.user = InfoBatalla.USERS.SERVER;
        enviarAmbos(msg, variables);
        int idUser1 = (int) variables.usuario1Reader.readObject();
        int idUser2 = (int) variables.usuario2Reader.readObject();
        variables.User1 = ServicioBatallas.getServicioUsuarios().getByID(idUser1);
        variables.User2 = ServicioBatallas.getServicioUsuarios().getByID(idUser2);
    }

    private static List<String> imagenesEquipacion(List<String> set) {
        if (set != null && !set.isEmpty()) {
            String imagenArma = "null";
            String rarezaArma = "null";
            Optional<ArmasEntity> arma = ServicioBatallas.getServicioArmas().getByID(set.get(0));
            if (arma.isPresent()) {
                imagenArma = arma.get().getImagen();
                rarezaArma=arma.get().getRareza();
            }
            String imagenArmadura = "null";
            String rarezaArmadura= "null";
            if (set.size() > 1) {
                Optional<ArmadurasEntity> armadura = ServicioBatallas.getServicioArmaduras().getByID(set.get(1));
                if (armadura.isPresent()) {
                    imagenArmadura = armadura.get().getImagen();
                    rarezaArmadura=armadura.get().getRareza();
                }
            }
            return new ArrayList<>(Arrays.asList(imagenArma,imagenArmadura,rarezaArma,rarezaArmadura));
        } else {
            return null;
        }
    }

    private static VariablesGeneralesBatallas sacarPartesSets(VariablesGeneralesBatallas variables) {
        if (variables.SET1 != null && !variables.SET1.isEmpty()) {
            if (!variables.SET1.get(0).equals("null")) {
                Optional<ArmasEntity> optional = ServicioBatallas.getServicioArmas().getByID(variables.SET1.get(0));
                optional.ifPresent(armasEntity -> variables.vSet.arma1 = armasEntity);
            }
            if (variables.SET1.size() > 1 && !variables.SET1.get(1).equals("null")) {
                Optional<ArmadurasEntity> optional2 = ServicioBatallas.getServicioArmaduras().getByID(variables.SET1.get(1));
                optional2.ifPresent(armadura -> variables.vSet.armadura1 = armadura);
            }
        }
        if (variables.SET2 != null && !variables.SET2.isEmpty()) {
            if (!variables.SET2.get(0).equals("null")) {
                Optional<ArmasEntity> optional = ServicioBatallas.getServicioArmas().getByID(variables.SET2.get(0));
                optional.ifPresent(armasEntity -> variables.vSet.arma2 = armasEntity);
            }
            if (variables.SET2.size() > 1  && !variables.SET2.get(1).equals("null")) {
                Optional<ArmadurasEntity> optional2 = ServicioBatallas.getServicioArmaduras().getByID(variables.SET2.get(1));
                optional2.ifPresent(armadura -> variables.vSet.armadura2 = armadura);
            }
        }
        return variables;
    }

}
