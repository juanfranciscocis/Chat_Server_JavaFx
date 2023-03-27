package com.example.chatserver;

import javafx.application.Platform;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ServerBackend {
    //DATA MEMBERS
    private static DatagramSocket socket;

    private static Set<Integer> conectedPorts = new HashSet<>();


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
                        new UsuariosDB().actualizarConexion(mensaje[0],receivePacket.getPort());
                        conectedPorts.add(receivePacket.getPort());

                        //ENVIAR BASE DE DATOS DE USUARIO, PARA CADA USUARIO EN LA BASE DE DATOS ENVIO EL ID Y EL ESTADO DE CONEXION
                        String mensajeUsuarios= "";
                        for (Usuarios usuario : new UsuariosDB().obtenerUsuarios()) {
                            mensajeUsuarios += usuario.getUsuarioID() + "/" + usuario.isConectado() + "-";
                        }
                        byte[] dataUsuarios = mensajeUsuarios.getBytes();
                        DatagramPacket sendPacketUsuarios = new DatagramPacket(dataUsuarios,
                                dataUsuarios.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(sendPacketUsuarios);

                        usuarioCreadoOEliminado();

                        new MensajesDB().agregarMensaje(new Mensajes(mensaje[0],"AUTENTICADO","SERVER"));
                        chatServerGUIController controller = MainServer.chatServerGUIController;
                        Platform.runLater(() -> {
                            controller.poblarLogsServer();
                        });

                    } else {
                        System.out.println("ID NO AUTENTICADO");
                        String mensajeAutenticado = "ERROR-NO AUTENTICADO, NO PUEDE ENVIAR MENSAJES";
                        byte[] dataAutenticado = mensajeAutenticado.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(dataAutenticado,
                                dataAutenticado.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(sendPacket);
                    }
                    

                } else if (mensaje[0].equals("MENSAJE")) {
                    // EN MENSAJE[1] ESTA EL ID DEL USUARIO QUE ENVIA EL MENSAJE
                    //BUSCAMOS EL PUERTO DEL USUARIO AL QUE SE LE VA A ENVIAR EL MENSAJE
                    int puerto = new UsuariosDB().obtenerPuerto(mensaje[1]);
                    //OBTENER EL PUERTO DESDE DONDE LLEGO EL MENSAJE
                    int puertoOrigen = receivePacket.getPort();
                    // EN MENSAJE[2] ESTA EL MENSAJE, CREAMOS UM STRING CON MENSAJE-ID ORIGEN - MENSAJE
                    String mensajeAEnviar = "MENSAJE-" + new UsuariosDB().obtenerIdConPuerto(puertoOrigen) + "-" + mensaje[2];
                    //ENVIAR EL MENSAJE AL PUERTO
                    byte[] dataMensaje = mensajeAEnviar.getBytes();
                    DatagramPacket sendPacketMensaje = new DatagramPacket(dataMensaje,
                            dataMensaje.length, receivePacket.getAddress(), puerto);
                    socket.send(sendPacketMensaje);
                    System.out.println("MENSAJE ENVIADO CORRECTAMENTE");
                    //GUARDAMOS EL MENSAJE EN LA BASE DE DATOS
                    new MensajesDB().agregarMensaje(new Mensajes(new UsuariosDB().obtenerIdConPuerto(puertoOrigen),mensaje[2],new UsuariosDB().obtenerIdConPuerto(puerto)));
                    chatServerGUIController controller = MainServer.chatServerGUIController;
                    Platform.runLater(() -> {
                        controller.poblarLogsServer();
                    });
                }else if (mensaje[0].equals("DESCONECTAR")){
                    //DESCONECTAR EL USUARIO DE LA BASE DE DATOS
                    new UsuariosDB().desconectarUsuario(mensaje[1]);
                    new MensajesDB().agregarMensaje(new Mensajes(mensaje[1],"DESCONECTADO","SERVER"));
                    chatServerGUIController controller = MainServer.chatServerGUIController;
                    Platform.runLater(() -> {
                        controller.poblarLogsServer();
                    });

                    //ENVIAR BASE DE DATOS DE USUARIOS A TODOS LOS CLIENTES CONECTADOS
                    usuarioCreadoOEliminado();



                }


            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void usuarioCreadoOEliminado() throws IOException {
        //ENVIAR BASE DE DATOS DE USUARIOS A TODOS LOS CLIENTES CONECTADOS
        for (Integer puerto : conectedPorts) {
            String mensajeUsuarios= "NUEVOS USUARIOS-";
            for (Usuarios usuario : new UsuariosDB().obtenerUsuarios()) {
                mensajeUsuarios += usuario.getUsuarioID() + "/" + usuario.isConectado() + "-";
            }
            byte[] dataUsuarios = mensajeUsuarios.getBytes();
            DatagramPacket sendPacketUsuarios = new DatagramPacket(dataUsuarios,
                    dataUsuarios.length, InetAddress.getLocalHost(), puerto);
            socket.send(sendPacketUsuarios);
        }
    }


    public void eliminarMensaje(String mensaje, String idEnvio, String idRecepcion){
        //ENVIAR MENSAJE "DESCONEXION-idEnvio->MENSAJE" A idEnvio y idRecepcion
        try {
            //ENVIAR MENSAJE A idEnvio
            String mensajeAEnviar = "ELIMINADO-" +"TU>"+mensaje;
            byte[] dataMensaje = mensajeAEnviar.getBytes();
            DatagramPacket sendPacketMensaje = new DatagramPacket(dataMensaje,
                    dataMensaje.length, InetAddress.getLocalHost(), new UsuariosDB().obtenerPuerto(idEnvio));
            socket.send(sendPacketMensaje);

            //ENVIAR MENSAJE A idRecepcion
            System.out.println("PUERTO DE RECEPCION: "+new UsuariosDB().obtenerPuerto(idRecepcion));
            int puertoRecepcion = new UsuariosDB().obtenerPuerto(idRecepcion);
            String mensajeAEnviar2 = "ELIMINADO-" +idEnvio+">"+mensaje;
            byte[] dataMensaje2 = mensajeAEnviar2.getBytes();
            DatagramPacket sendPacketMensaje2 = new DatagramPacket(dataMensaje2,
                    dataMensaje2.length, InetAddress.getLocalHost(),puertoRecepcion);
            socket.send(sendPacketMensaje2);
            System.out.println("MENSAJE DE ELIMINADO ENVIADO CORRECTAMENTE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void mensajesConsola(String mensaje){
        System.out.println(mensaje);
    }





}
