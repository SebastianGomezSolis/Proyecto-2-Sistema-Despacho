package com.proyecto2backend.controller;

import com.proyecto2backend.servicios.ChatUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    private TextArea txtArea;
    @FXML private Button btnConectar;
    @FXML private TextField txtInput;
    @FXML private TextField txtNombre;

    private ChatUser user;

    @FXML
    public void onConnect() {
        try {
            user = new ChatUser();
            user.conectar("localhost", 6000, txtNombre.getText(),
                    msg -> Platform.runLater(() -> txtArea.appendText(msg + "\n")));
            btnConectar.setDisable(true);
        } catch (Exception e) {
            txtArea.appendText("Error al conectar al servidor: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void onSend() {
        String msg = txtInput.getText().trim();
        if (msg.isEmpty()) return;
        user.enviarMensaje(msg);
        txtInput.clear();

    }
}
