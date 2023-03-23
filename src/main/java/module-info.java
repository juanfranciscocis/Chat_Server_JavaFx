module com.example.chatserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.chatserver to javafx.fxml;
    exports com.example.chatserver;
}