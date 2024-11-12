package com.syw.APIrest.Accounts.Entitys;

import javax.persistence.*;
import java.io.BufferedWriter;
import java.io.IOException;

@Entity
@Table(name = "batallas", schema = "registros_usuarios", catalog = "")
public class BatallasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "User1")
    private UsersEntity userEntity1;

    @ManyToOne
    @JoinColumn(name = "User2")
    private UsersEntity userEntity2;

    @Basic
    @Column(name = "log")
    private String log;

    @Transient
    private Process proceso;

    @Transient
    private BufferedWriter input;


    public BatallasEntity(Process proceso, BufferedWriter input, UsersEntity userEntity1) {
        this.userEntity1 = userEntity1;
        this.proceso = proceso;
        this.input = input;
    }

    public void escribir(String msg) {
        try {
            input.write(msg);
            input.newLine();  // Agrega un salto de línea después de escribir el mensaje
            input.flush();    // Limpia el búfer para asegurar que los datos se envíen
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void escribir(int msg) {
        escribir(Integer.toString(msg));  // Convierte el entero a String y llama al método anterior
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsersEntity getUserEntity1() {
        return userEntity1;
    }

    public void setUserEntity1(UsersEntity userEntity1) {
        this.userEntity1 = userEntity1;
    }

    public UsersEntity getUserEntity2() {
        return userEntity2;
    }

    public void setUserEntity2(UsersEntity userEntity2) {
        this.userEntity2 = userEntity2;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Process getProceso() {
        return proceso;
    }

    public void setProceso(Process proceso) {
        this.proceso = proceso;
    }

    public BufferedWriter getInput() {
        return input;
    }

    public void setInput(BufferedWriter input) {
        this.input = input;
    }


}
