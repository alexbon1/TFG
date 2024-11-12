package com.utad.android.entitys.JSON;

import com.utad.android.entitys.UsersEntity;

import java.io.Serializable;

public class MisionCompletaJSON implements Serializable {
    public String idMision;
    public int porcentaje;
    public UsersEntity user;
    public String resultado;

    public MisionCompletaJSON(String idMision, int porcentaje , UsersEntity user, String resultado) {
        this.idMision = idMision;
        this.porcentaje = porcentaje;
        this.user = user;
        this.resultado = resultado;
    }
}
