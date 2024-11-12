package com.utad.android.entitys.JSON;

import com.utad.android.entitys.ArmadurasEntity;
import com.utad.android.entitys.ArmasEntity;
import com.utad.android.entitys.UsersEntity;

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
