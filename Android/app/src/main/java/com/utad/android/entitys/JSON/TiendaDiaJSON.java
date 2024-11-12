package com.utad.android.entitys.JSON;

import com.utad.android.entitys.ArmadurasEntity;
import com.utad.android.entitys.ArmasEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TiendaDiaJSON implements Serializable {
    private ArmasEntity[] armas;
    private ArmadurasEntity[] armaduras;
    private String[] yaLasTienesArmas;
    private String[] yaLasTienesArmaduras;
    private Map<String, Integer> precios;

    public TiendaDiaJSON(ArmasEntity[] armas, ArmadurasEntity[] armaduras, String[] yaLasTienesArmas, String[] yaLasTienesArmaduras) {
        this.armas = armas;
        this.armaduras = armaduras;
        this.yaLasTienesArmas = yaLasTienesArmas;
        this.yaLasTienesArmaduras = yaLasTienesArmaduras;
        this.precios = new HashMap<>();
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
                ", yaLasTienesArmas=" + Arrays.toString(yaLasTienesArmas) +
                ", yaLasTienesArmaduras=" + Arrays.toString(yaLasTienesArmaduras) +
                ", precios=" + precios +
                '}';
    }
}
