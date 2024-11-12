package com.syw.APIrest.Stadistics.Entitys;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;


@Document(collection = "Misiones diarias")
public class MisionesDiariasEntity {
   @Id 
    private String id;
    private String nombre;
    private String descripcion;
    private String objetivo;
    private String tipoUser;
    private String tipoMision;
    private Object[] otrosDatos;

    public MisionesDiariasEntity(String nombre, String descripcion, String objetivo, String tipoUser, String tipoMision, Object[] otrosDatos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.tipoUser = tipoUser;
        this.tipoMision = tipoMision;
        this.otrosDatos = otrosDatos;
    }

    public MisionesDiariasEntity() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getTipoMision() {
        return tipoMision;
    }

    public void setTipoMision(String tipoMision) {
        this.tipoMision = tipoMision;
    }

    public Object[] getOtrosDatos() {
        return otrosDatos;
    }

    public void setOtrosDatos(Object[] otrosDatos) {
        this.otrosDatos = otrosDatos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MisionesDiariasEntity that = (MisionesDiariasEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(nombre, that.nombre)) return false;
        if (!Objects.equals(descripcion, that.descripcion)) return false;
        if (!Objects.equals(objetivo, that.objetivo)) return false;
        if (!Objects.equals(tipoUser, that.tipoUser)) return false;
        if (!Objects.equals(tipoMision, that.tipoMision)) return false;
        return Arrays.equals(otrosDatos, that.otrosDatos);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (objetivo != null ? objetivo.hashCode() : 0);
        result = 31 * result + (tipoUser != null ? tipoUser.hashCode() : 0);
        result = 31 * result + (tipoMision != null ? tipoMision.hashCode() : 0);
        result = 31 * result + (otrosDatos != null ? Arrays.hashCode(otrosDatos) : 0);
        return result;
    }
    public static class Preguntas{
        private String pregunta;
        private String[] respuestas;
        private String correcta;

        public Preguntas(String pregunta, String[] respuestas, String correcta) {
            this.pregunta = pregunta;
            this.respuestas = respuestas;
            this.correcta = correcta;
        }

        public String getPregunta() {
            return pregunta;
        }

        public void setPregunta(String pregunta) {
            this.pregunta = pregunta;
        }

        public String[] getRespuestas() {
            return respuestas;
        }

        public void setRespuestas(String[] respuestas) {
            this.respuestas = respuestas;
        }

        public String getCorrecta() {
            return correcta;
        }

        public void setCorrecta(String correcta) {
            this.correcta = correcta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Preguntas preguntas = (Preguntas) o;

            if (!Objects.equals(pregunta, preguntas.pregunta)) return false;
            if (!Arrays.equals(respuestas, preguntas.respuestas)) return false;
            return Objects.equals(correcta, preguntas.correcta);
        }

        @Override
        public int hashCode() {
            int result = pregunta != null ? pregunta.hashCode() : 0;
            result = 31 * result + Arrays.hashCode(respuestas);
            result = 31 * result + (correcta != null ? correcta.hashCode() : 0);
            return result;
        }
    }
}
