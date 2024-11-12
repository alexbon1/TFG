package com.syw.APIrest.Accounts.ResJSON;

import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Accounts.Services.ServicioUsuarios;

import java.sql.Date;

public class UsuarioJSON extends UsersEntity {
    private int nivelReal;
    private int xp;
    private int xpHastaSiguiente;
    private int rangoActual;

    public UsuarioJSON(int id, String username, String nombre, String apellidos, String email, Date fechaDeNacimiento, byte confirmada, String pwd, int nivel, int cantidadMisiones, int monedas, String tipo) {
        super(id, username, nombre, apellidos, email, fechaDeNacimiento, confirmada, pwd, nivel, cantidadMisiones, monedas, tipo);
        nivelReal= ServicioUsuarios.obtenerNivel(getNivel());
        xp = getNivel();
        xpHastaSiguiente= ServicioUsuarios.xpParaSiguienteNivel(xp);
        rangoActual=ServicioUsuarios.obtenerRangoXP(nivelReal);
    }
    public UsuarioJSON(UsersEntity user) {
        super(user.getId(), user.getUsername(), user.getNombre(), user.getApellidos(), user.getEmail(),
                user.getFechaDeNacimiento(), user.getConfirmada(), user.getPwd(), user.getNivel(),
                user.getCantidadMisiones(), user.getMonedas(), user.getTipo());

        this.nivelReal = ServicioUsuarios.obtenerNivel(user.getNivel());
        this.xp = user.getNivel(); // Aquí el campo `nivel` realmente contiene el XP.
        this.xpHastaSiguiente = ServicioUsuarios.xpParaSiguienteNivel(this.xp);
        this.rangoActual = ServicioUsuarios.obtenerRangoXP(this.nivelReal);
    }
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
