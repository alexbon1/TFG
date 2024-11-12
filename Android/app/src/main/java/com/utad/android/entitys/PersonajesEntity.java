package com.utad.android.entitys;


public class PersonajesEntity {


    private String id;
    private String historia;
    private String nombre;
    private String imagen;

    public PersonajesEntity(String imagen) {
        this.imagen = imagen;
    }

    public PersonajesEntity(String historia, String nombre, String imagen) {
        this.historia = historia;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonajesEntity that = (PersonajesEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (historia != null ? !historia.equals(that.historia) : that.historia != null) return false;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
        if (imagen != null ? !imagen.equals(that.imagen) : that.imagen != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (historia != null ? historia.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        return result;
    }
}
