module com.example.projekt_60134_io {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.projekt_60134_io to javafx.fxml;
    exports com.example.projekt_60134_io;
}