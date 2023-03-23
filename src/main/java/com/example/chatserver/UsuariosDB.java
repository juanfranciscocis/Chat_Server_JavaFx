package com.example.chatserver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class UsuariosDB {

    final String DATABASE_URL = "jdbc:derby:Usuarios;create=false";

    public UsuariosDB(){

    }

    public ObservableList<Usuarios> obtenerUsuarios() {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM usuarios")) {

            ObservableList<Usuarios> usuariosObservableList = FXCollections.observableArrayList();

            // Agregar cada registro a la lista observable de Faculty
            while (resultSet.next()) {
                String id = resultSet.getString("usuarioID");
                Boolean conectado = resultSet.getBoolean("conexion");
                Integer puerto = resultSet.getInt("port");
                usuariosObservableList.add(new Usuarios(id, conectado, puerto));
            }
            return usuariosObservableList;


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }
        return null;
    }

    public void agregarUsuario(String id) {

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Debe ingresar un ID").showAndWait();
            return;
        }

        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();) {
            statement.execute("INSERT INTO usuarios (usuarioID, conexion, port) VALUES ('" + id + "', false, 0)");
            new Alert(Alert.AlertType.INFORMATION, "Usuario creado").showAndWait();
        } catch (SQLException sqlException) {
            new Alert(Alert.AlertType.ERROR, "Error al crear usuario").showAndWait();

        }
    }

    public void eliminarUsuario(String id) {

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Debe ingresar un ID").showAndWait();
            return;
        }

        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();) {
            statement.execute("DELETE FROM usuarios WHERE usuariosID = '" + id + "'");
        } catch (SQLException sqlException) {
            new Alert(Alert.AlertType.ERROR, "Error al eliminar usuario").showAndWait();

        }
    }

    void actualizarConexion(String id, int puerto) {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();) {
            statement.execute("UPDATE usuarios SET conexion = true, port = " + puerto + " WHERE usuarioID = '" + id + "'");
        } catch (SQLException sqlException) {
            new Alert(Alert.AlertType.ERROR, "Error al actualizar conexion").showAndWait();

        }
    }

    public boolean verificarID(String id) {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM usuarios WHERE usuarioID = '" + id + "'")) {

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }

        return false;



    }



}
