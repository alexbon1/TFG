package com.utad.android.entitys;

import java.io.Serializable;
import java.util.Objects;

public class ArmadurasEntity implements Serializable {

    private String id;
    private String nombre;
    private String rareza;
    private String imagen;
    private Integer stats;
    private String descripcion;


    public ArmadurasEntity(String nombre, String rareza, String imagen, Integer stats, String descripcion) {
        this.nombre = nombre;
        this.rareza = rareza;
        this.imagen = imagen;
        this.stats = stats;
        this.descripcion = descripcion;
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

    public String getRareza() {
        return rareza;
    }

    public void setRareza(String rareza) {
        this.rareza = rareza;
    }

    public Integer getStats() {
        return stats;
    }

    public void setStats(Integer stats) {
        this.stats = stats;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

        ArmadurasEntity armaduras = (ArmadurasEntity) o;

        if (!Objects.equals(id, armaduras.id)) return false;
        if (!Objects.equals(nombre, armaduras.nombre)) return false;
        if (!Objects.equals(rareza, armaduras.rareza)) return false;
        if (!Objects.equals(imagen, armaduras.imagen)) return false;
        if (!Objects.equals(stats, armaduras.stats)) return false;
        return Objects.equals(descripcion, armaduras.descripcion);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (rareza != null ? rareza.hashCode() : 0);
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        result = 31 * result + (stats != null ? stats.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArmadurasEntity{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rareza='" + rareza + '\'' +
                ", imagen='" + imagen + '\'' +
                ", stats=" + stats +
                ", Descripcion='" + descripcion + '\'' +
                '}';
    }
}