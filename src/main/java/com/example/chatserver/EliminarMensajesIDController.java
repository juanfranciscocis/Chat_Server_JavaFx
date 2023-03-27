package com.example.chatserver;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EliminarMensajesIDController {


    @FXML
    private TextField mensajeIDTextEdit;

    @FXML
    public TableView<Mensajes> mensajesTableView;

    static ObservableList<Mensajes> mensajesList;

    String id;

    public EliminarMensajesIDController(ObservableList<Mensajes> mensajes, String id) {
        this.mensajesList = mensajes;
        this.id = id;

    }

    @FXML
    void initialize() {
        poblarTabla();
    }

    void poblarTabla(){
        try {
            mensajesTableView.getColumns().clear();
            mensajesTableView.getItems().clear();


        } catch (Exception e) {
            System.out.println("No se pudo limpiar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo limpiar la tabla");
        }

        try {
            TableColumn<Mensajes, Integer> mensajeID = new TableColumn<>("mensajeID");
            mensajeID.setCellValueFactory(new PropertyValueFactory<>("mensajeID"));
            TableColumn<Mensajes, String> enviadoPor = new TableColumn<>("enviadoPor");
            enviadoPor.setCellValueFactory(new PropertyValueFactory<>("enviadoPor"));
            TableColumn<Mensajes, String> recibidoPor = new TableColumn<>("recibidoPor");
            recibidoPor.setCellValueFactory(new PropertyValueFactory<>("recibidoPor"));
            TableColumn<Mensajes, String> mensaje = new TableColumn<>("mensaje");
            mensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
            mensajesTableView.getColumns().addAll(mensajeID,enviadoPor,recibidoPor,mensaje);
            mensajesTableView.setItems(mensajesList);
        } catch (Exception e) {
            System.out.println("No se pudo poblar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo poblar la tabla");
        }

    }


    @FXML
    void eliminarMensaje(ActionEvent event) {
        try {
            int mensajeID = Integer.parseInt(mensajeIDTextEdit.getText());
            new MensajesDB().eliminarMensaje(mensajeID);

            mensajesList = new MensajesDB().buscarParaEliminar(id);
            System.out.println(mensajesList);
            poblarTabla();


            chatServerGUIController controller = MainServer.chatServerGUIController;
            controller.poblarLogsServer();



        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo eliminar el mensaje").showAndWait();
        }

    }




}
