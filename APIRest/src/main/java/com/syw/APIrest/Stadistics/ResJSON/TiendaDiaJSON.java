package com.syw.APIrest.Stadistics.ResJSON;

import com.syw.APIrest.Stadistics.Constantes.ConstantesMonedas;
import com.syw.APIrest.Stadistics.Constantes.ConstantesTienda;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TiendaDiaJSON implements Serializable {
    private ArmasEntity[] armas;
    private ArmadurasEntity[] armaduras;
    private String[] yaLasTienesArmas;
    private String[] yaLasTienesArmaduras;
    private Map<String,Integer> precios;
    public TiendaDiaJSON(ArmasEntity[] armas, ArmadurasEntity[] armaduras, String[] YaLasTienesArmas, String[] YaLasTienesArmaduras) {
        this.armas = armas;
        this.armaduras = armaduras;
        this.yaLasTienesArmas = YaLasTienesArmas;
        this.yaLasTienesArmaduras = YaLasTienesArmaduras;
        this.precios=new HashMap<>();
        precios.put("Armas"+ConstantesTienda.Tipos.COMUNES, ConstantesMonedas.Armas.PRECIO_COMUNES);
        precios.put("Armas"+ConstantesTienda.Tipos.RARAS, ConstantesMonedas.Armas.PRECIO_RARAS);
        precios.put("Armas"+ConstantesTienda.Tipos.ESPECIALES, ConstantesMonedas.Armas.PRECIO_ESPECIALES);
        precios.put("Armas"+ConstantesTienda.Tipos.EPICAS, ConstantesMonedas.Armas.PRECIO_EPICAS);
        precios.put("Armas"+ConstantesTienda.Tipos.LEGENDARIAS, ConstantesMonedas.Armas.PRECIO_LEGENDARIAS);

        precios.put("Armaduras"+ConstantesTienda.Tipos.COMUNES, ConstantesMonedas.Armaduras.PRECIO_COMUNES);
        precios.put("Armaduras"+ConstantesTienda.Tipos.RARAS, ConstantesMonedas.Armaduras.PRECIO_RARAS);
        precios.put("Armaduras"+ConstantesTienda.Tipos.ESPECIALES, ConstantesMonedas.Armaduras.PRECIO_ESPECIALES);
        precios.put("Armaduras"+ConstantesTienda.Tipos.EPICAS, ConstantesMonedas.Armaduras.PRECIO_EPICAS);
        precios.put("Armaduras"+ConstantesTienda.Tipos.LEGENDARIAS, ConstantesMonedas.Armaduras.PRECIO_LEGENDARIAS);

        precios.put(ConstantesTienda.Tipos.BOX_BARATA, ConstantesMonedas.Box.BARATAS);
        precios.put(ConstantesTienda.Tipos.BOX_CARA, ConstantesMonedas.Box.CARAS);

    }

    public ArmasEntity[] getArmas() {
        return armas;
    }

    public void setArmas(ArmasEntity[] armas) {
        this.armas = armas;
    }

    public ArmadurasEntity[] getArmaduras() {
        return armaduras;
    }

    public void setArmaduras(ArmadurasEntity[] armaduras) {
        this.armaduras = armaduras;
    }

    public String[] getYaLasTienesArmas() {
        return yaLasTienesArmas;
    }

    public void setYaLasTienesArmas(String[] yaLasTienesArmas) {
        this.yaLasTienesArmas = yaLasTienesArmas;
    }

    public String[] getYaLasTienesArmaduras() {
        return yaLasTienesArmaduras;
    }

    public void setYaLasTienesArmaduras(String[] yaLasTienesArmaduras) {
        this.yaLasTienesArmaduras = yaLasTienesArmaduras;
    }

    public Map<String, Integer> getPrecios() {
        return precios;
    }

    public void setPrecios(Map<String, Integer> precios) {
        this.precios = precios;
    }

    public String Serializar() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);
        out.close();
        byte[] serializedObject = bos.toByteArray();
        return Base64.getEncoder().encodeToString(serializedObject);
    }
    public static TiendaDiaJSON Deserializar(String serializedString) throws IOException, ClassNotFoundException {
        byte[] serializedObject = Base64.getDecoder().decode(serializedString);
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
        ObjectInput in = new ObjectInputStream(bis);
        return (TiendaDiaJSON) in.readObject();
    }

    @Override
    public String toString() {
        return "TiendaDiaJSON{" +
                "armas=" + Arrays.toString(armas) +
                ", armaduras=" + Arrays.toString(armaduras) +
                ", YaLasTienesArmas=" + Arrays.toString(yaLasTienesArmas) +
                ", YaLasTienesArmaduras=" + Arrays.toString(yaLasTienesArmaduras) +
                ", precios=" + precios +
                '}';
    }
}
