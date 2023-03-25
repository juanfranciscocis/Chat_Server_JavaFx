package com.example.chatserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Statement;

public class MainServer extends Application {

    static ServerBackend serverBackend = new ServerBackend();
    static chatServerGUIController chatServerGUIController = new chatServerGUIController();
    @Override
    public void start(Stage stage) throws IOException {

    /*
        try (Connection connection = DriverManager.getConnection("jdbc:derby:Usuarios;create=true")){
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE usuarios");
            statement.execute("DROP TABLE mensajes");
            System.out.println("Tables dropped (MAIN)");
        }catch (Exception e) {
            System.out.println("Tables already dropped (MAIN)");
        }

     */









        try (Connection connection = DriverManager.getConnection("jdbc:derby:Usuarios;create=true")){
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE usuarios (usuarioID VARCHAR(6) NOT NULL, conexion BOOLEAN NOT NULL, port INT NOT NULL ,PRIMARY KEY (usuarioID))");
            statement.execute("CREATE TABLE mensajes (mensajeID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1), enviadoPor VARCHAR(6) NOT NULL, mensaje VARCHAR(500) NOT NULL, recibidoPor VARCHAR(6) NOT NULL, hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, confirmacion BOOLEAN NOT NULL DEFAULT TRUE, PRIMARY KEY (mensajeID))");
            System.out.println("Table Usuario creado (MAIN)");

        }catch (Exception e) {
            System.out.println("Tables already exist (MAIN)");
        }




        FXMLLoader fxmlLoader = new FXMLLoader(MainServer.class.getResource("chatServerGUI.fxml"));
        fxmlLoader.setController(chatServerGUIController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}