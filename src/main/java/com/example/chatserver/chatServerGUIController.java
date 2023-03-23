package com.example.chatserver;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class chatServerGUIController{

    @FXML
    private TextField borrarMensajeTextField;

    @FXML
    private TextField crearEliminarTextField;

    @FXML
    private TextField listarDestinatarioTextField;

    @FXML
    private TextField listarUsuarioTextField;

    UsuariosDB usuariosDB = new UsuariosDB();

    @FXML
    void buscarParaEliminar(ActionEvent event) {

    }

    @FXML
    void crearUsuario(ActionEvent event) {

        usuariosDB.agregarUsuario(crearEliminarTextField.getText());
        System.out.println("Agregado");
        new Alert(Alert.AlertType.INFORMATION, "Agregado");
    }

    @FXML
    void eliminarUsuario(ActionEvent event) {
        usuariosDB.eliminarUsuario(crearEliminarTextField.getText());
        System.out.println("Eliminado");
        new Alert(Alert.AlertType.INFORMATION, "Eliminado");
    }

    @FXML
    void listarUsuarios(ActionEvent event) throws IOException {
        ObservableList<Usuarios> usuariosList = usuariosDB.obtenerUsuarios();
        Stage stage = new Stage();
        FXMLLoader fxmlCoincheckerMenu = new FXMLLoader(MainServer.class.getResource("mensajesServer.fxml"));
        fxmlCoincheckerMenu.setController(new mensajesServerController(usuariosList));
        Scene scene = new Scene(fxmlCoincheckerMenu.load());
        stage.setTitle("LISTA DE USUARIOS");
        stage.setScene(scene);
        stage.show();
    }

}
