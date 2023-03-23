package com.example.chatserver;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class mensajesServerController {

    @FXML
    private Label mensajesLabel;

    @FXML
    private TableView<Usuarios> mensajesTableView;

    ObservableList<Usuarios> usuariosList;

    public mensajesServerController(ObservableList<Usuarios> usuariosList){
        this.usuariosList = usuariosList;
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
            TableColumn<Usuarios, String> usuarioID = new TableColumn<>("usuarioID");
            usuarioID.setCellValueFactory(new PropertyValueFactory<>("usuarioID"));
            TableColumn<Usuarios, Boolean> conectado = new TableColumn<>("conexion");
            conectado.setCellValueFactory(new PropertyValueFactory<>("conexion"));
            mensajesTableView.getColumns().addAll(usuarioID,conectado);
            mensajesTableView.setItems(usuariosList);
        } catch (Exception e) {
            System.out.println("No se pudo poblar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo poblar la tabla");
        }

    }

}
