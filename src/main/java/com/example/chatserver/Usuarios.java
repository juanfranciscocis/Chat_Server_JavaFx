package com.example.chatserver;

public class ClienteConectado {

    private String id;
    private boolean conectado = false;

    public ClienteConectado(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public boolean getConectado(){
        return conectado;
    }

    public void setConectado(boolean conectado){
        this.conectado = conectado;
    }



}
