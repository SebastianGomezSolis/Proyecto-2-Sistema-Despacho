package com.proyecto2backend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("/com.proyecto2backend/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Menu inicio sesion");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com.proyecto2backend/images/Login.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
