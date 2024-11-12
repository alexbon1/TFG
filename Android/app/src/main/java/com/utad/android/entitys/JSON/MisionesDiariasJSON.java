package com.utad.android.entitys.JSON;

import java.util.Arrays;
import java.util.Objects;

public class MisionesDiariasJSON {

    private String id;
    private String nombre;
    private String descripcion;
    private String objetivo;
    private String tipoUser;
    private String tipoMision;
    private Object[] otrosDatos;
    private int recompensa;
    private int xp;
    private int porcentajeCompleto;

//    public MisionesDiariasJSON(MisionesDiariasEntity mision, int recompensa, int xp) {
//        this.id = mision.getId();
//        this.nombre = mision.getNombre();
//        this.descripcion = mision.getDescripcion();
//        this.objetivo = mision.getObjetivo();
//        this.tipoUser = mision.getTipoUser();
//        this.tipoMision = mision.getTipoMision();
//        this.otrosDatos = mision.getOtrosDatos();
//        this.recompensa = recompensa;
//        this.xp = xp;
//    }
    public MisionesDiariasJSON(String nombre, String descripcion, String objetivo, String tipoUser, String tipoMision, Object[] otrosDatos, int recompensa, int xp, int porcentajeCompleto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.tipoUser = tipoUser;
        this.tipoMision = tipoMision;
        this.otrosDatos = otrosDatos;
        this.recompensa = recompensa;
        this.xp = xp;
        this.porcentajeCompleto = porcentajeCompleto;
    }

    public MisionesDiariasJSON() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getTipoMision() {
        return tipoMision;
    }

    public void setTipoMision(String tipoMision) {
        this.tipoMision = tipoMision;
    }

    public Object[] getOtrosDatos() {
        return otrosDatos;
    }

    public void setOtrosDatos(Object[] otrosDatos) {
        this.otrosDatos = otrosDatos;
    }

    public int getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(int recompensa) {
        this.recompensa = recompensa;
    }

    public int getxp() {
        return xp;
    }

    public void setxp(int xp) {
        this.xp = xp;
    }

    public int getPorcentajeCompleto() {
        return porcentajeCompleto;
    }

    public void setPorcentajeCompleto(int porcentajeCompleto) {
        this.porcentajeCompleto = porcentajeCompleto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MisionesDiariasJSON that = (MisionesDiariasJSON) o;

        if (recompensa != that.recompensa) return false;
        if (xp != that.xp) return false;
        if (porcentajeCompleto != that.porcentajeCompleto) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(nombre, that.nombre)) return false;
        if (!Objects.equals(descripcion, that.descripcion)) return false;
        if (!Objects.equals(objetivo, that.objetivo)) return false;
        if (!Objects.equals(tipoUser, that.tipoUser)) return false;
        if (!Objects.equals(tipoMision, that.tipoMision)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(otrosDatos, that.otrosDatos);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (objetivo != null ? objetivo.hashCode() : 0);
        result = 31 * result + (tipoUser != null ? tipoUser.hashCode() : 0);
        result = 31 * result + (tipoMision != null ? tipoMision.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(otrosDatos);
        result = 31 * result + recompensa;
        result = 31 * result + xp;
        result = 31 * result + porcentajeCompleto;
        return result;
    }

    @Override
    public String toString() {
        return "MisionesDiariasJSON{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", tipoUser='" + tipoUser + '\'' +
                ", tipoMision='" + tipoMision + '\'' +
                ", otrosDatos=" + Arrays.toString(otrosDatos) +
                ", recompensa=" + recompensa +
                ", xp=" + xp +
                ", porcentajeCompleto=" + porcentajeCompleto +
                '}';
    }
}
