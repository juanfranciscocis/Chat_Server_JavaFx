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

        //desconectar usuario
        desconectarUsuario(id);


        //eliminar todos los mensajes de ese usuario de la tabla mensajes
        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement = connection.prepareStatement("DELETE FROM mensajes WHERE enviadoPor = ? OR recibidoPor = ?")
        ) {
            statement.setString(1, id);
            statement.setString(2, id);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement = connection.prepareStatement("DELETE FROM usuarios WHERE usuarioID = ? AND conexion = false")
        ) {
            statement.setString(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                new Alert(Alert.AlertType.WARNING, "El usuario con ID " + id + " no existe o está conectado").showAndWait();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Usuario eliminado exitosamente").showAndWait();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
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


    public int obtenerPuerto(String s) {
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM usuarios WHERE usuarioID = '" + s + "'")) {

            if (resultSet.next()) {
                System.out.println("PUERTO DE ENVIO ENCONTRADO");
                return resultSet.getInt("port");
            } else {
                return 0;
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }

        return 0;
    }

    public String obtenerIdConPuerto(int puerto){
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM usuarios WHERE port = " + puerto)) {

            if (resultSet.next()) {
                System.out.println("ID CON PUERTO ENCONTRADO");
                return resultSet.getString("usuarioID");
            } else {
                return null;
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }

        return null;
    }

    void desconectarUsuario(String id){
        try (
                Connection connection = DriverManager.getConnection(
                        DATABASE_URL);
                Statement statement = connection.createStatement();) {
            statement.execute("UPDATE usuarios SET conexion = false, port = 0 WHERE usuarioID = '" + id + "'");
        } catch (SQLException sqlException) {
            new Alert(Alert.AlertType.ERROR, "Error al actualizar conexion").showAndWait();

        }

    }


    void desconectarATodos(){
        try (
            Connection connection = DriverManager.getConnection(
                    DATABASE_URL);
            Statement statement = connection.createStatement();){
            statement.execute("UPDATE usuarios SET conexion = false, port=0");
        }catch (SQLException sqlException) {
            new Alert(Alert.AlertType.ERROR, "Error al actualizar conexion").showAndWait();

        }
    }
}
