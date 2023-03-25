package com.example.chatserver;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class chatServerGUIController{

    @FXML
    private TextField borrarMensajeTextField;

    @FXML
    private TextField crearEliminarTextField;

    @FXML
    private TableColumn<Mensajes, String> enviadoPor;


    @FXML
    private TableView<Mensajes> logTableView;

    @FXML
    private TextField listarDestinatarioTextField;

    @FXML
    private TextField listarUsuarioTextField;

    static UsuariosDB usuariosDB = new UsuariosDB();

    ObservableList<Mensajes> mensajesList;

    @FXML
    void initialize() {
        poblarLogsServer();
    }

    @FXML
    void buscarParaEliminar(ActionEvent event) throws IOException {

        ObservableList<Mensajes> mensajes = new MensajesDB().buscarParaEliminar(borrarMensajeTextField.getText());
        Stage stage = new Stage();
        FXMLLoader fxmlCoincheckerMenu = new FXMLLoader(MainServer.class.getResource("eliminarMensajesID.fxml"));
        fxmlCoincheckerMenu.setController(new EliminarMensajesIDController(mensajes));
        Scene scene = new Scene(fxmlCoincheckerMenu.load());
        stage.setTitle("LISTA DE USUARIOS");
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void crearUsuario(ActionEvent event) throws IOException {

        usuariosDB.agregarUsuario(crearEliminarTextField.getText());
        ServerBackend.usuarioCreadoOEliminado();

        new MensajesDB().agregarMensaje(new Mensajes(crearEliminarTextField.getText(),"CREADO","SERVER"));
        poblarLogsServer();

        System.out.println("Agregado");
        new Alert(Alert.AlertType.INFORMATION, "Agregado");
    }

    @FXML
    void eliminarUsuario(ActionEvent event) throws IOException {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "TODO LO RELACIONADO CON EL USUARIO SERA ELIMNADO HASTA LOS LOGS, SEGURO QUE QUIERE PROSEGUIR?");

        //first check connection state of the user
        //if connected, send a message to the user to disconnect


        confirm.showAndWait().ifPresent(response -> {
            if (response == confirm.getButtonTypes().get(0)) {
                System.out.println("Eliminado");
                new Alert(Alert.AlertType.INFORMATION, "Eliminado");
                usuariosDB.eliminarUsuario(crearEliminarTextField.getText());
                try {
                    ServerBackend.usuarioCreadoOEliminado();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Eliminado");
                new MensajesDB().agregarMensaje(new Mensajes(crearEliminarTextField.getText(),"ELIMINADO","SERVER"));
                poblarLogsServer();
            } else {
                System.out.println("Cancelado");
            }
        });





    }

    @FXML
    void listarUsuarios(ActionEvent event) throws IOException {
        ObservableList<Usuarios> usuariosList = usuariosDB.obtenerUsuarios();
        Stage stage = new Stage();
        FXMLLoader fxmlCoincheckerMenu = new FXMLLoader(MainServer.class.getResource("usuariosGUI.fxml"));
        fxmlCoincheckerMenu.setController(new UsuariosGUIController(usuariosList));
        Scene scene = new Scene(fxmlCoincheckerMenu.load());
        stage.setTitle("LISTA DE USUARIOS");
        stage.setScene(scene);
        stage.show();
    }


    void poblarLogsServer(){
        try {
            logTableView.getColumns().clear();
            logTableView.getItems().clear();
        } catch (Exception e) {
            System.out.println("No se pudo limpiar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo limpiar la tabla");
        }

        try {
            mensajesList = new MensajesDB().obtenerMensajes();
            TableColumn<Mensajes, Integer> mensajeID = new TableColumn<>("mensajeID");
            mensajeID.setCellValueFactory(new PropertyValueFactory<>("mensajeID"));
            TableColumn<Mensajes, String> enviadoPor = new TableColumn<>("enviadoPor");
            enviadoPor.setCellValueFactory(new PropertyValueFactory<>("enviadoPor"));
            TableColumn<Mensajes, String> recibidoPor = new TableColumn<>("recibidoPor");
            recibidoPor.setCellValueFactory(new PropertyValueFactory<>("recibidoPor"));
            TableColumn<Mensajes, String> mensaje = new TableColumn<>("mensaje");
            mensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
            TableColumn<Mensajes,String> hora = new TableColumn<>("hora");
            hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
            TableColumn<Mensajes,Boolean> confirmacion = new TableColumn<>("confirmacion");
            confirmacion.setCellValueFactory(new PropertyValueFactory<>("confirmacion"));
            logTableView.getColumns().addAll(mensajeID,enviadoPor,recibidoPor,mensaje,hora,confirmacion);
            logTableView.setItems(mensajesList);
            //SORT TABLE BY HORA
            logTableView.getSortOrder().add(hora);
        } catch (Exception e) {
            System.out.println("No se pudo poblar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo poblar la tabla");
        }
    }



    @FXML
    void listarMensajes(ActionEvent event) throws IOException {

        ObservableList<Mensajes> mensajes = new MensajesDB().mensajesPorEnviadoYRecibido(listarUsuarioTextField.getText(),listarDestinatarioTextField.getText());
        Stage stage = new Stage();
        FXMLLoader fxmlCoincheckerMenu = new FXMLLoader(MainServer.class.getResource("mensajesGUI.fxml"));
        fxmlCoincheckerMenu.setController(new MensajesGUIController(mensajes));
        Scene scene = new Scene(fxmlCoincheckerMenu.load());
        stage.setTitle("LISTA DE USUARIOS");
        stage.setScene(scene);
        stage.show();

    }







}
