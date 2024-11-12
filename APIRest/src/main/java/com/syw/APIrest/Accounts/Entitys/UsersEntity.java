package com.syw.APIrest.Accounts.Entitys;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "registros_usuarios", catalog = "")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id")
    private int id;
    @Basic
    @Column(name = "Username")
    private String username;
    @Basic
    @Column(name = "Nombre")
    private String nombre;
    @Basic
    @Column(name = "Apellidos")
    private String apellidos;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "Fecha_De_Nacimiento")
    private Date fechaDeNacimiento;
    @Basic
    @Column(name = "Confirmada")
    private byte confirmada;
    @Basic
    @Column(name = "pwd")
    private String pwd;
    @Basic
    @Column(name = "Nivel")
    private int nivel;
    @Basic
    @Column(name = "Cantidad_Misiones")
    private int cantidadMisiones;
    @Basic
    @Column(name = "Monedas")
    private int monedas;

    private String tipo;

    public UsersEntity(int id, String username, String nombre, String apellidos, String email, Date fechaDeNacimiento, byte confirmada, String pwd, int nivel, int cantidadMisiones, int monedas, String tipo) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.confirmada = confirmada;
        this.pwd = pwd;
        this.nivel = nivel;
        this.cantidadMisiones = cantidadMisiones;
        this.monedas = monedas;
        this.tipo = tipo;
    }

    public UsersEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public byte getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(byte confirmada) {
        this.confirmada = confirmada;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getCantidadMisiones() {
        return cantidadMisiones;
    }

    public void setCantidadMisiones(int cantidadMisiones) {
        this.cantidadMisiones = cantidadMisiones;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return Objects.equals(id, that.id); // comparar campos únicos
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // usar campos únicos
    }

    @Override
    public String toString() {
        return "UsersEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", fechaDeNacimiento=" + fechaDeNacimiento +
                ", confirmada=" + confirmada +
                ", pwd='" + pwd + '\'' +
                ", nivel=" + nivel +
                ", cantidadMisiones=" + cantidadMisiones +
                ", monedas=" + monedas +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
