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
    @Override
    public void start(Stage stage) throws IOException {

        /*
        try (Connection connection = DriverManager.getConnection("jdbc:derby:Usuarios;create=true")){
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE usuarios");
            System.out.println("Tables dropped (MAIN)");
        }catch (Exception e) {
            System.out.println("Tables already dropped (MAIN)");
        }

         */



        try (Connection connection = DriverManager.getConnection("jdbc:derby:Usuarios;create=true")){
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE usuarios (usuarioID VARCHAR(6) NOT NULL, conexion BOOLEAN NOT NULL, PRIMARY KEY (usuarioID))");
            System.out.println("Table Usuario creado (MAIN)");

        }catch (Exception e) {
            System.out.println("Tables already exist (MAIN)");
        }



        ServerBackend serverBackend = new ServerBackend();
        FXMLLoader fxmlLoader = new FXMLLoader(MainServer.class.getResource("chatServerGUI.fxml"));
        fxmlLoader.setController(new chatServerGUIController());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}