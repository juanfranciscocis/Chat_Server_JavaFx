package com.example.chatserver;

public class Usuarios {

    String usuarioID;
    boolean conectado = false;

    public Usuarios(String id, boolean conectado){
        this.usuarioID = id;
        this.conectado = conectado;
    }

    public String getUsuarioID(){
        return usuarioID;
    }

    public boolean getConexion(){
        return conectado;
    }

    public void setUsuarioID(String id){
        this.usuarioID = id;
    }

    public void setConectado(boolean conectado){
        this.conectado = conectado;
    }



}
