package com.utad.android.entitys;


public class InventariosEntity {

    private int id;
    private String armaduras;
    private String armas;
    private String cartas;
    private String setActual;
    private String personaje;

    public InventariosEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArmaduras() {
        return armaduras;
    }

    public void setArmaduras(String armaduras) {
        this.armaduras = armaduras;
    }

    public String getArmas() {
        return armas;
    }

    public void setArmas(String armas) {
        this.armas = armas;
    }

    public String getCartas() {
        return cartas;
    }

    public void setCartas(String cartas) {
        this.cartas = cartas;
    }

    public String getSetActual() {
        return setActual;
    }

    public void setSetActual(String setActual) {
        this.setActual = setActual;
    }

    public String getPersonaje() {
        return personaje;
    }

    public void setPersonaje(String personaje) {
        this.personaje = personaje;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventariosEntity that = (InventariosEntity) o;

        if (id != that.id) return false;
        if (!armaduras.equals(that.armaduras)) return false;
        if (!armas.equals(that.armas)) return false;
        if (!cartas.equals(that.cartas)) return false;
        if (!setActual.equals(that.setActual)) return false;
        return personaje.equals(that.personaje);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + armaduras.hashCode();
        result = 31 * result + armas.hashCode();
        result = 31 * result + cartas.hashCode();
        result = 31 * result + setActual.hashCode();
        result = 31 * result + personaje.hashCode();
        return result;
    }
}
