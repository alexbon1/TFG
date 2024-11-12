package com.syw.APIrest.Stadistics.Entitys;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Objects;

@Document(collection = "Cartas")
public class CartasEntity {
    @Id
    private String id;
    private String tipo;
    private String naturaleza;
    private String imagen;
    private int cantidad;

    public CartasEntity(String tipo, String naturaleza, String imagen, int cantidad) {
        this.tipo = tipo;
        this.naturaleza = naturaleza;
        this.imagen = imagen;
        this.cantidad = cantidad;
    }

    public CartasEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartasEntity that = (CartasEntity) o;

        if (cantidad != that.cantidad) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(tipo, that.tipo)) return false;
        if (!Objects.equals(naturaleza, that.naturaleza)) return false;
        return Objects.equals(imagen, that.imagen);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + (naturaleza != null ? naturaleza.hashCode() : 0);
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        result = 31 * result + cantidad;
        return result;
    }
}
