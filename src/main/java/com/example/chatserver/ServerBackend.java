package com.example.chatserver;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerBackend {
    //DATA MEMBERS
    private DatagramSocket socket;

    //CONSTRUCTOR
    public ServerBackend() {
        try {
            socket = new DatagramSocket(5000);
            mensajesConsola("Servidor iniciado en el puerto: " + socket.getPort());
            //Nuevo hilo para recibir mensajes
            new Thread(() -> entradaDeMensajes()).start();
        } catch (SocketException socketException) {
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    //METHODS
    public void entradaDeMensajes()
    {
        while (true)
        {
            try
            {
                byte[] data = new byte[100];
                DatagramPacket receivePacket =
                        new DatagramPacket(data, data.length);

                socket.receive(receivePacket);

                mensajesConsola("\nMesanje recibido:" +
                        "\nDel host: " + receivePacket.getAddress() +
                        "\nHost puerto: " + receivePacket.getPort() +
                        "\nTamaño: " + receivePacket.getLength() +
                        "\nMensaje:\n\t" + new String(receivePacket.getData(),
                        0, receivePacket.getLength()));
                //al recibir el mensaje se envia siempre un id separado por - y el mensaje
                String[] mensaje = new String(receivePacket.getData(),
                        0, receivePacket.getLength()).split("-");

                if(mensaje[1].equals("AUTENTICAR")){
                    if (new UsuariosDB().verificarID(mensaje[0])) {
                        System.out.println("ID AUTENTICADO");
                        String mensajeAutenticado = "MENSAJE-AUTENTICADO, PUEDE ENVIAR MENSAJES";
                        byte[] dataAutenticado = mensajeAutenticado.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(dataAutenticado,
                                dataAutenticado.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(sendPacket);

                        //CLIENTE AUTENTICADO Y CONECTADO
                        new UsuariosDB().actualizarConexion(mensaje[0]);

                        //ENVIAR BASE DE DATOS DE USUARIO, PARA CADA USUARIO EN LA BASE DE DATOS ENVIO EL ID Y EL ESTADO DE CONEXION
                        String mensajeUsuarios= "";
                        for (Usuarios usuario : new UsuariosDB().obtenerUsuarios()) {
                            mensajeUsuarios += usuario.getUsuarioID() + "/" + usuario.getConexion() + "-";
                        }
                        byte[] dataUsuarios = mensajeUsuarios.getBytes();
                        DatagramPacket sendPacketUsuarios = new DatagramPacket(dataUsuarios,
                                dataUsuarios.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(sendPacketUsuarios);

                    } else {
                        System.out.println("ID NO AUTENTICADO");
                        String mensajeAutenticado = "ERROR-NO AUTENTICADO, NO PUEDE ENVIAR MENSAJES";
                        byte[] dataAutenticado = mensajeAutenticado.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(dataAutenticado,
                                dataAutenticado.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(sendPacket);
                    }
                }




            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    private void mensajesConsola(String mensaje){
        System.out.println(mensaje);
    }





}
