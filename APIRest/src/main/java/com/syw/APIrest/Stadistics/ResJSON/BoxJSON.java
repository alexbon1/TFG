package com.syw.APIrest.Stadistics.ResJSON;

import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;

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
