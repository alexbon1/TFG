package com.syw.APIrest.Stadistics.ResJSON;


import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;

import java.io.Serializable;

public class CompraJSON implements Serializable {
    public UsersEntity user;
    public ArmasEntity arma;
    public ArmadurasEntity armadura;

    public CompraJSON(UsersEntity user, ArmadurasEntity compra) {
        this.user = user;
        armadura = compra;

    }
    public CompraJSON(UsersEntity user, ArmasEntity compra) {
        this.user = user;
        arma = compra;
    }
    //Para Deserializar
    public CompraJSON() {
    }
}
