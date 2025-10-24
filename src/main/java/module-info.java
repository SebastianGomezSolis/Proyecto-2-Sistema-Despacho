module com.proyecto2backend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.logging;
    requires javafx.graphics;


    // Opens para JavaFX
    opens com.proyecto2backend to javafx.fxml;
    opens com.proyecto2backend.controller to javafx.fxml;
    opens com.proyecto2backend.model to javafx.fxml;


    // Exports
    exports com.proyecto2backend;
    exports com.proyecto2backend.controller;
    exports com.proyecto2backend.model;
    exports com.proyecto2backend.datos;
    exports com.proyecto2backend.logic;
}