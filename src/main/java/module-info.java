module com.proyecto2backend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.logging;


    opens com.proyecto2backend to javafx.fxml;
    exports com.proyecto2backend;
}