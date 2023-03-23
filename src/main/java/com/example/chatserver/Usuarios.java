package com.example.chatserver;

public class Usuarios {

    String usuarioID;
    boolean conectado = false;
    int puerto;

    public Usuarios(String id, boolean conectado, int puerto){
        this.usuarioID = id;
        this.conectado = conectado;
        this.puerto = puerto;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
}
