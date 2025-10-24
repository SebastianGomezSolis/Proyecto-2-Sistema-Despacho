package com.proyecto2backend.controller;

import com.proyecto2backend.logic.AdministradorLogica;
import com.proyecto2backend.logic.FarmaceutaLogica;
import com.proyecto2backend.logic.MedicoLogica;
import com.proyecto2backend.model.Administrador;
import com.proyecto2backend.model.Farmaceuta;
import com.proyecto2backend.model.Medico;
import com.proyecto2backend.model.Usuario;
import com.proyecto2backend.utilitarios.Sesion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CambiarClaveController {

    @FXML private PasswordField pwdClaveActual;
    @FXML private PasswordField pwdClaveNueva;
    @FXML private PasswordField pwdVerificarClaveNueva;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;

    private String usuarioId; // Variable para almacenar el ID del usuario recibido desde LoginController

    // Método para recibir el usuario desde LoginController
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @FXML
    private void initialize() {
        // Enfocar primer campo
        if (pwdClaveActual != null) {
            pwdClaveActual.requestFocus();
        }
    }

    @FXML
    private void cambiarClave() {
        String claveActual = pwdClaveActual.getText();
        String nuevaClave = pwdClaveNueva.getText();
        String verificada = pwdVerificarClaveNueva.getText();

        // Validaciones básicas
        if (usuarioId == null || usuarioId.isBlank() ||
                claveActual.isBlank() || nuevaClave.isBlank() || verificada.isBlank()) {
            mostrarAlerta("Campos vacíos", "Debe completar todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        if (!nuevaClave.equals(verificada)) {
            mostrarAlerta("No coincide", "La nueva clave no coincide con la verificación.", Alert.AlertType.ERROR);
            return;
        }

        if (nuevaClave.length() < 4) {
            mostrarAlerta("Clave débil", "La nueva clave debe tener al menos 4 caracteres.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Buscar usuario por el ID recibido (NO usar sesión)
            Usuario usuarioActual = buscarUsuarioPorId(usuarioId);

            if (usuarioActual == null) {
                mostrarAlerta("Usuario no encontrado", "No se encontró un usuario con ese ID.", Alert.AlertType.ERROR);
                return;
            }

            // Verificar clave actual
            if (!usuarioActual.getClave().equals(claveActual)) {
                mostrarAlerta("Clave incorrecta", "La clave actual no es válida.", Alert.AlertType.ERROR);
                return;
            }

            // Actualizar la clave
            actualizarClave(usuarioActual, nuevaClave);

            mostrarAlerta("Éxito", "La clave ha sido actualizada correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();

        } catch (Exception e) {
            mostrarAlerta("Error al guardar", "Error al actualizar la clave: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Usuario buscarUsuarioPorId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        try {

            if (id.startsWith("MED")) {
                return new MedicoLogica().findByIdentificacion(id);

            } else if (id.startsWith("FAR")) {
                return new FarmaceutaLogica().findByIdentificacion(id);

            } else if (id.startsWith("ADM")) {
                return new AdministradorLogica().findByIdentificacion(id);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrio un error al encontrar usuario: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return null;
    }

    private void actualizarClave(Usuario usuario, String nuevaClave) throws Exception {
        String id = usuario.getIdentificacion();
        usuario.setClave(nuevaClave);

        if (id.startsWith("MED") && usuario instanceof Medico medico) {
            new MedicoLogica().update(medico);

        } else if (id.startsWith("FAR") && usuario instanceof Farmaceuta farma) {
            new FarmaceutaLogica().update(farma);

        } else if (id.startsWith("ADM") && usuario instanceof Administrador admin) {
            new AdministradorLogica().update(admin);

        } else {
            throw new Exception("Tipo de usuario no reconocido: " + id);
        }

        // Actualizar la sesión si el usuario actual cambió su clave
        if (Sesion.getUsuarioActual() != null && Sesion.getUsuarioActual().getIdentificacion().equals(id)) {
            Sesion.actualizarUsuario(usuario);
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        pwdClaveActual.clear();
        pwdClaveNueva.clear();
        pwdVerificarClaveNueva.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}