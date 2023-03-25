package com.example.chatserver;

public class Mensajes {

    int mensajeID;
    String enviadoPor;
    String mensaje;
    String recibidoPor;

    String hora;

    Boolean confirmacion;

    public Mensajes(int mensajeID,String enviadoPor, String mensaje, String recibidoPor, String hora, Boolean confirmacion) {
        this.mensajeID = mensajeID;
        this.enviadoPor = enviadoPor;
        this.mensaje = mensaje;
        this.recibidoPor = recibidoPor;
        this.hora = java.time.LocalTime.now().toString();
        this.confirmacion = true;
    }

    public Mensajes(String enviadoPor, String mensaje, String recibidoPor) {
        this.enviadoPor = enviadoPor;
        this.mensaje = mensaje;
        this.recibidoPor = recibidoPor;
        this.hora = java.time.LocalTime.now().toString();
        this.confirmacion = true;
    }

    public Mensajes(int mensajeID, String enviadoPor, String mensaje, String recibidoPor) {
        this.mensajeID = mensajeID;
        this.enviadoPor = enviadoPor;
        this.mensaje = mensaje;
        this.recibidoPor = recibidoPor;
        this.hora = java.time.LocalTime.now().toString();
        this.confirmacion = true;
    }

    public int getMensajeID() {
        return mensajeID;
    }

    public void setMensajeID(int mensajeID) {
        this.mensajeID = mensajeID;
    }

    public String getEnviadoPor() {
        return enviadoPor;
    }

    public void setEnviadoPor(String enviadoPor) {
        this.enviadoPor = enviadoPor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRecibidoPor() {
        return recibidoPor;
    }

    public void setRecibidoPor(String recibidoPor) {
        this.recibidoPor = recibidoPor;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Boolean getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(Boolean confirmacion) {
        this.confirmacion = confirmacion;
    }
}
