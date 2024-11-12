package com.syw.APIrest.Stadistics.ResJSON;


import com.syw.APIrest.Accounts.Entitys.UsersEntity;

import java.io.Serializable;

public class MisionCompletaJSON implements Serializable {
    public String idMision;
    public int porcentaje;
    public long tiempo;
    public UsersEntity user;
    public String resultado;

    public MisionCompletaJSON() {
    }

    public MisionCompletaJSON(String idMision, int porcentaje, long tiempo, UsersEntity user, String resultado) {
        this.idMision = idMision;
        this.porcentaje = porcentaje;
        this.tiempo = tiempo;
        this.user = user;
        this.resultado = resultado;
    }
}