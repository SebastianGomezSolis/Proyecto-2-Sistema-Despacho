package com.proyecto2backend.controller;

import com.proyecto2backend.servicios.UserChat;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class ChatController {
    @FXML private TextField txtNombre;
    @FXML private Button btnConectar;

    @FXML private TextArea txtArea;

    @FXML private TextField txtInput;
    @FXML private Button btnEnviar;

    @FXML private ListView<String> listaUsuarios;
    @FXML private RadioButton rbGeneral;
    @FXML private RadioButton rbPrivado;
    @FXML private ToggleGroup tgModo;
    @FXML private Label lblDestino; // muestra el destino seleccionado para privado

    private final UserChat chat = new UserChat();
    private volatile boolean conectado = false;

    @FXML
    public void initialize() {
        // Label por defecto
        lblDestino.setText("(General)");

        // Si selecciona un usuario y esta en modo Privado, actualiza etiqueta
        listaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (rbPrivado.isSelected()) {
                lblDestino.setText(sel != null ? sel : "(Elige a un usuario conectado)");
            }
        });

        // Cambio de modo actualiza etiqueta destino
        tgModo.selectedToggleProperty().addListener((obs, old, t) -> {
            if (rbGeneral.isSelected()) {
                lblDestino.setText("(General)");
            } else {
                String sel = listaUsuarios.getSelectionModel().getSelectedItem();
                lblDestino.setText(sel != null ? sel : "(Elige a un usuario conectado)");
            }
        });

        txtInput.setOnAction(e -> onSend());
    }

    @FXML
    public void onConnect() {
        if (conectado) return;

        try {
            chat.conectar("localhost", 6000, txtNombre.getText(), this::onIncomingLine, this::onUsersList);
            conectado = true;
            btnConectar.setDisable(true);
            txtNombre.setDisable(true);
        } catch (IOException e) {
            append("[SISTEMA] No se pudo conectar: " + e.getMessage());
        }
    }

    @FXML
    public void onSend() {
        if (!conectado) {
            append("[SISTEMA] Primero conéctate al servidor.");
            return;
        }
        String msg = txtInput.getText();
        if (msg == null || msg.isBlank()) return;

        if (rbPrivado.isSelected()) {
            String destino = listaUsuarios.getSelectionModel().getSelectedItem();
            if (destino == null || destino.isBlank()) {
                append("[SISTEMA] Elige un usuario para enviar un mensaje privado.");
                return;
            }
            chat.enviarPrivado(destino, msg);
        } else {
            // Si el usuario escribe manualmente @destino, se respeta
            chat.enviarMensaje(msg);
        }
        txtInput.clear();
    }

    private void onIncomingLine(String line) {
        Platform.runLater(() -> append(line));
    }

    private void onUsersList(List<String> usuarios) {
        Platform.runLater(() -> {
            String yo = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
            listaUsuarios.getItems().setAll(usuarios);
            listaUsuarios.getItems().removeIf(u -> u.equalsIgnoreCase(yo));
        });
    }

    private void append(String s) {
        if (txtArea.getText().isEmpty()) {
            txtArea.setText(s);
        } else {
            txtArea.appendText("\n" + s);
        }
    }
}
