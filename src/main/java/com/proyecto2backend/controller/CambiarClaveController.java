package com.proyecto2backend.controller;

import com.proyecto2backend.servicios.*;
import com.proyecto2backend.model.Administrador;
import com.proyecto2backend.model.Farmaceuta;
import com.proyecto2backend.model.Medico;
import com.proyecto2backend.model.Usuario;
import com.proyecto2backend.servicios.service.AdministradorSocketService;
import com.proyecto2backend.servicios.service.FarmaceutaSocketService;
import com.proyecto2backend.servicios.service.MedicoSocketService;
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

    private final AdministradorSocketService administradorSocketService = new AdministradorSocketService();
    private final FarmaceutaSocketService farmaceutaSocketService = new FarmaceutaSocketService();
    private final MedicoSocketService medicoSocketService = new MedicoSocketService();

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
            mostrarAlerta("Advertencia", "Debe ingresar un identificador válido.", Alert.AlertType.WARNING);
            return null;
        }

        Async.run(
                () -> {
                    try {
                        if (id.startsWith("MED")) {
                            return medicoSocketService.findByIdentificacion(id);
                        } else if (id.startsWith("FAR")) {
                            return farmaceutaSocketService.findByIdentificacion(id);
                        } else if (id.startsWith("ADM")) {
                            return administradorSocketService.findByIdentificacion(id);
                        } else {
                            return null;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                usuario -> {
                    if (usuario != null) {
                        System.out.println("Usuario encontrado: " + usuario.getIdentificacion());
                        mostrarAlerta("Éxito", "Usuario encontrado: " + usuario.getIdentificacion(), Alert.AlertType.INFORMATION);
                    } else {
                        mostrarAlerta("No encontrado", "No se encontró un usuario con el ID especificado.", Alert.AlertType.INFORMATION);
                    }
                },
                ex -> {
                    mostrarAlerta("Error", "Ocurrió un error al buscar usuario: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );

        return null;
    }

    private void actualizarClave(Usuario usuario, String nuevaClave) throws Exception {
        if (usuario == null || nuevaClave == null || nuevaClave.isBlank()) {
            mostrarAlerta("Error", "Usuario o clave inválidos.", Alert.AlertType.WARNING);
            return;
        }

        Async.run(
                () -> {
                    try {
                        String id = usuario.getIdentificacion();
                        usuario.setClave(nuevaClave);

                        if (id.startsWith("MED") && usuario instanceof Medico medico) {
                            medicoSocketService.update(medico);
                        } else if (id.startsWith("FAR") && usuario instanceof Farmaceuta farma) {
                            farmaceutaSocketService.update(farma);
                        } else if (id.startsWith("ADM") && usuario instanceof Administrador admin) {
                            administradorSocketService.update(admin);
                        } else {
                            throw new Exception("Tipo de usuario no reconocido: " + id);
                        }

                        // Actualizar sesión si corresponde
                        if (Sesion.getUsuarioActual() != null &&
                                Sesion.getUsuarioActual().getIdentificacion().equals(id)) {
                            Sesion.actualizarUsuario(usuario);
                        }

                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                ok -> {
                    mostrarAlerta("Éxito", "Clave actualizada correctamente.", Alert.AlertType.INFORMATION);
                },
                ex -> {
                    mostrarAlerta("Error", "No se pudo actualizar la clave: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
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