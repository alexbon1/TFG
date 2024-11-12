package com.syw.APIrest.Stadistics.Entitys;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;


@Document(collection = "Historico")
public class HistoricoEntity {
    @Id
    private String id;
    private String fecha;
    private String seccion;
    private String idObject;
    private String usuarios;

    public HistoricoEntity(String fecha, String seccion, String idObject) {
        this.fecha = fecha;
        this.seccion = seccion;
        this.idObject = idObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdObject() {
        return idObject;
    }

    public void setIdObject(String idObject) {
        this.idObject = idObject;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(String usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public String toString() {
        return "HistoricoEntity{" +
                "id='" + id + '\'' +
                ", fecha='" + fecha + '\'' +
                ", seccion='" + seccion + '\'' +
                ", idObject='" + idObject + '\'' +
                ", usuarios='" + usuarios + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoricoEntity that = (HistoricoEntity) o;

        if (!id.equals(that.id)) return false;
        if (!fecha.equals(that.fecha)) return false;
        if (!seccion.equals(that.seccion)) return false;
        if (!idObject.equals(that.idObject)) return false;
        return usuarios.equals(that.usuarios);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fecha.hashCode();
        result = 31 * result + seccion.hashCode();
        result = 31 * result + idObject.hashCode();
        result = 31 * result + usuarios.hashCode();
        return result;
    }
}
