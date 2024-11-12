package com.utad.android.entitys.JSON;


import com.utad.android.entitys.ArmadurasEntity;
import com.utad.android.entitys.ArmasEntity;

public class BoxJSON {
    public ArmadurasEntity armadura;
    public ArmasEntity armas;
    public Boolean loTienes;

    public BoxJSON(ArmadurasEntity armadura, ArmasEntity armas, Boolean loTienes) {
        this.armadura = armadura;
        this.armas = armas;
        this.loTienes = loTienes;
    }
}
