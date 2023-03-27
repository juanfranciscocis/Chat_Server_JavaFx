package com.example.chatserver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class MensajesDB {


    final String DATABASE_URL = "jdbc:derby:Usuarios;create=false";

    public MensajesDB() {

    }

    public ObservableList<Mensajes> obtenerMensajes() {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM mensajes")) {

            ObservableList<Mensajes> mensajesObservableList = FXCollections.observableArrayList();

            // Agregar cada registro a la lista observable de Faculty
            while (resultSet.next()) {
                Integer id = resultSet.getInt("mensajeID");
                String enviadoPor = resultSet.getString("enviadoPor");
                String mensaje = resultSet.getString("mensaje");
                String recibidoPor = resultSet.getString("recibidoPor");
                mensajesObservableList.add(new Mensajes(id, enviadoPor, mensaje, recibidoPor));
            }
            return mensajesObservableList;


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }
        return null;
    }


    public void agregarMensaje(Mensajes mensaje) {

        if (mensaje.getMensaje().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Debe ingresar un mensaje").showAndWait();
            return;
        }

        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();) {
            statement.execute("INSERT INTO mensajes (enviadoPor, mensaje, recibidoPor) VALUES ('" + mensaje.getEnviadoPor() + "', '" + mensaje.getMensaje() + "', '" + mensaje.getRecibidoPor() + "')");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }
    }

    public void eliminarMensaje(int mensajeID) {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();) {
            statement.execute("DELETE FROM mensajes WHERE mensajeID = " + mensajeID);
            new Alert(Alert.AlertType.INFORMATION, "Mensaje eliminado").showAndWait();
        } catch (SQLException sqlException) {
            new Alert(Alert.AlertType.ERROR, "Error al eliminar mensaje").showAndWait();

        }
    }

    public ObservableList<Mensajes> mensajesPorEnviadoYRecibido(String idEnviado, String idRecibido) {

        //FROM ALL MESSAGES ONLY GET THE ONES SEND BY IDENVIADO AND RECEIVED BY IDRECIBIDO
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM mensajes WHERE enviadoPor = '" + idEnviado + "' AND recibidoPor = '" + idRecibido + "'")) {

            ObservableList<Mensajes> mensajesObservableList = FXCollections.observableArrayList();

            // Agregar cada registro a la lista observable de Faculty
            while (resultSet.next()) {
                Integer idMensaje = resultSet.getInt("mensajeID");
                String enviadoPor = resultSet.getString("enviadoPor");
                String mensaje = resultSet.getString("mensaje");
                String recibidoPor = resultSet.getString("recibidoPor");
                mensajesObservableList.add(new Mensajes(idMensaje, enviadoPor, mensaje, recibidoPor));
            }
            System.out.println(mensajesObservableList);
            return mensajesObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public ObservableList<Mensajes> buscarParaEliminar(String id ) {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM mensajes WHERE enviadoPor = '" + id + "' OR recibidoPor = '" + id + "'")) {

            ObservableList<Mensajes> mensajesObservableList = FXCollections.observableArrayList();

            // Agregar cada registro a la lista observable de Faculty
            while (resultSet.next()) {
                Integer idMensaje = resultSet.getInt("mensajeID");
                String enviadoPor = resultSet.getString("enviadoPor");
                String mensaje = resultSet.getString("mensaje");
                String recibidoPor = resultSet.getString("recibidoPor");
                mensajesObservableList.add(new Mensajes(idMensaje, enviadoPor, mensaje, recibidoPor));
            }
            return mensajesObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}



