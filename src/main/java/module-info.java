module com.proyecto2backend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.proyecto2backend to javafx.fxml;
    exports com.proyecto2backend;
}