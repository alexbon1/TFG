package com.utad.android.entitys.JSON;



import com.utad.android.entitys.UsersEntity;


public class UsuarioJSON extends UsersEntity {
    private int nivelReal;
    private int xp;
    private int xpHastaSiguiente;
    private int rangoActual;

    public UsersEntity toUsersEntity() {
        UsersEntity user = new UsersEntity();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setNombre(this.getNombre());
        user.setApellidos(this.getApellidos());
        user.setEmail(this.getEmail());
        user.setFechaDeNacimiento(this.getFechaDeNacimiento());
        user.setConfirmada(this.getConfirmada());
        user.setPwd(this.getPwd());
        user.setNivel(this.getXp()); // Aquí el campo `nivel` realmente contiene el XP.
        user.setCantidadMisiones(this.getCantidadMisiones());
        user.setMonedas(this.getMonedas());
        user.setTipo(this.getTipo());
        return user;
    }
    public int getNivelReal() {
        return nivelReal;
    }
    public int getNivel(){
        return getNivelReal();
    }
    public void setNivelReal(int nivelReal) {
        this.nivelReal = nivelReal;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXpHastaSiguiente() {
        return xpHastaSiguiente;
    }

    public void setXpHastaSiguiente(int xpHastaSiguiente) {
        this.xpHastaSiguiente = xpHastaSiguiente;
    }

    public int getRangoActual() {
        return rangoActual;
    }

    public void setRangoActual(int rangoActual) {
        this.rangoActual = rangoActual;
    }
}
