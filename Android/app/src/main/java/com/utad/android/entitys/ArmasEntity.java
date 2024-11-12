package com.utad.android.entitys;

import java.io.Serializable;
import java.util.Objects;

public class ArmasEntity implements Serializable {

    private String id;
    private Integer fuerza;
    private String imagen;
    private String nombre;
    private String rareza;
    private String descripcion;

    public ArmasEntity(Integer fuerza, String imagen, String nombre, String rareza,String Descripcion) {
        this.fuerza = fuerza;
        this.imagen = imagen;
        this.nombre = nombre;
        this.rareza = rareza;
        this.descripcion = Descripcion;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFuerza() {
        return fuerza;
    }

    public void setFuerza(Integer fuerza) {
        this.fuerza = fuerza;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRareza() {
        return rareza;
    }

    public void setRareza(String rareza) {
        this.rareza = rareza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArmasEntity that = (ArmasEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(fuerza, that.fuerza)) return false;
        if (!Objects.equals(imagen, that.imagen)) return false;
        if (!Objects.equals(nombre, that.nombre)) return false;
        if (!Objects.equals(rareza, that.rareza)) return false;
        return Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fuerza != null ? fuerza.hashCode() : 0);
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (rareza != null ? rareza.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArmasEntity{" +
                "id='" + id + '\'' +
                ", fuerza=" + fuerza +
                ", imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rareza='" + rareza + '\'' +
                ", Descripcion='" + descripcion + '\'' +
                '}';
    }
}
