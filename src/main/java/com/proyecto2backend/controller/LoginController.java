package com.proyecto2backend.controller;

import com.proyecto2backend.logic.*;
import com.proyecto2backend.model.*;
import com.proyecto2backend.servicios.service.*;
import com.proyecto2backend.utilitarios.Sesion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML Button btnIniciarSesion;
    @FXML Button btnCambiarClave;
    @FXML ProgressIndicator progressInicioSesion;
    @FXML ProgressIndicator progressCambiarClave;

    private final MedicoSocketService medicoSocketService = new MedicoSocketService();
    private final FarmaceutaSocketService farmaceutaSocketService = new FarmaceutaSocketService();
    private final AdministradorSocketService administradorSocketService = new AdministradorSocketService();

    //Tabs
    @FXML private TabPane tabPane;
    @FXML private Tab tabMedicos;
    @FXML private Tab tabPacientes;
    @FXML private Tab tabMedicamentos;
    @FXML private Tab tabDashboard;
    @FXML private Tab tabPrescribir;
    @FXML private Tab tabHistorico;
    @FXML private Tab tabAcercaDe;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        verificarAdministradorPorDefecto();
    }


    private void ocultarSiNoTienePermiso(Tab tab, String codigo) {
        if (!Sesion.puedeAccederModulo(codigo)) {
            tabPane.getTabs().remove(tab); // lo elimina de la vista
        }
    }


    @FXML
    protected void handleLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        // Mostrar progress y ocultar botón
        btnIniciarSesion.setVisible(false);
        progressInicioSesion.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(1500); // Simula procesamiento
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                validarCredenciales(usuario, password);
            });
        }).start();
    }

    private void validarCredenciales(String id, String password) {
        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos requeridos");
            alert.setHeaderText(null);
            alert.setContentText("Debe ingresar usuario y contraseña.");
            alert.showAndWait();
            return;
        }

        // Mostrar progress y ocultar botón
        btnIniciarSesion.setVisible(false);
        progressInicioSesion.setVisible(true);

        Async.run(
                () -> {
                    try {
                        if (id.startsWith("MED")) {
                            Medico medico = medicoSocketService.findByIdentificacion(id);
                            if (medico != null && medico.getClave().equals(password)) {
                                return (Usuario) medico;
                            }

                        } else if (id.startsWith("FAR")) {
                            Farmaceuta farma = farmaceutaSocketService.findByIdentificacion(id);
                            if (farma != null && farma.getClave().equals(password)) {
                                return (Usuario) farma;
                            }

                        } else if (id.startsWith("ADM")) {
                            Administrador admin = administradorSocketService.findByIdentificacion(id);
                            if (admin != null && admin.getClave().equals(password)) {
                                return (Usuario) admin;
                            }
                        }
                        return null;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                usuarioAutenticado -> {
                    // UI thread
                    progressInicioSesion.setVisible(false);
                    btnIniciarSesion.setVisible(true);

                    if (usuarioAutenticado != null) {
                        // mismos pasos que ya tenías en handleLogin
                        List<String> permisos = asignarPermisosPorPrefijo(usuarioAutenticado.getIdentificacion());
                        Sesion.iniciarSesion(usuarioAutenticado, permisos);

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/GestionMedicos.fxml"));
                            Parent root = loader.load();

                            GestionMedicosController controller = loader.getController();
                            controller.configurarSegunPermisos();

                            Stage stage = (Stage) txtUsuario.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com.proyecto2backend/images/hospital.png")));
                            stage.setTitle("Menú principal");

                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error de sistema");
                            alert.setHeaderText(e.getStackTrace()[0].getClassName());
                            alert.setContentText("No fue posible iniciar sesión, debido a un error de sistema: " + e.getMessage());
                            alert.showAndWait();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error de autenticación");
                        alert.setHeaderText(null);
                        alert.setContentText("Usuario o contraseña incorrectos.");
                        alert.showAndWait();
                    }
                },
                ex -> {
                    progressInicioSesion.setVisible(false);
                    btnIniciarSesion.setVisible(true);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de autenticación");
                    alert.setHeaderText(null);
                    alert.setContentText("Ocurrió un error al validar las credenciales: " + ex.getMessage());
                    alert.showAndWait();
                }
        );
    }

    private List<String> asignarPermisosPorPrefijo(String usuario) {
        if (usuario.length() < 3) {
            return List.of();
        }
        String prefijo = usuario.substring(0, 3);

        List<String> permisos = switch (prefijo) {
            case "MED" -> List.of("PRESCRIBIR", "HISTORICO", "DASHBOARD", "ACERCA"); // Médico
            case "FAR" -> List.of("HISTORICO", "DASHBOARD", "ACERCA", "GESTION_DESPACHO");  // Farmacéutico
            case "ADM" -> List.of("GESTION_MEDICOS", "GESTION_FARMACEUTAS" ,"GESTION_PACIENTES", "GESTION_MEDICAMENTOS", "DASHBOARD", "HISTORICO", "ACERCA"); // Admin
            default -> List.of();
        };
        return permisos;
    }

    @FXML
    protected void funcionCambiarClave() {
        // Validar que hay un usuario ingresado
        String usuario = txtUsuario.getText().trim();
        if (usuario.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usuario requerido");
            alert.setHeaderText(null);
            alert.setContentText("Debe ingresar su ID de usuario antes de cambiar la clave.");
            alert.showAndWait();
            return;
        }

        btnCambiarClave.setVisible(false);
        progressCambiarClave.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                progressCambiarClave.setVisible(false);
                btnCambiarClave.setVisible(true);
                try {
                    // Lista de todas las rutas
                    String[] rutasPosibles = {
                            "/com/sistema/com.proyecto2backend/view/CambiarClave.fxml",
                            "/com.proyecto2backend/view/CambiarClave.fxml",
                            "/view/CambiarClave.fxml",
                            "/CambiarClave.fxml",
                            "view/CambiarClave.fxml",
                            "CambiarClave.fxml",
                            "/fxml/CambiarClave.fxml",
                            "fxml/CambiarClave.fxml"
                    };

                    FXMLLoader loader = null;
                    String rutaEncontrada = null;

                    for (String ruta : rutasPosibles) {
                        try {
                            URL url = getClass().getResource(ruta);
                            if (url != null) {
                                System.out.println("ENCONTRADO en: " + ruta);
                                System.out.println("URL completa: " + url);
                                loader = new FXMLLoader(url);
                                rutaEncontrada = ruta;
                                break;
                            } else {
                                System.out.println("No encontrado en: " + ruta);
                            }
                        } catch (Exception e) {
                            System.out.println("Error en ruta: " + ruta + " - " + e.getMessage());
                        }
                    }

                    if (loader == null) {
                        System.out.println("ARCHIVO NO ENCONTRADO EN NINGUNA UBICACIÓN");
                        System.out.println("Verifica que el archivo CambiarClave.fxml existe en:");
                        System.out.println("src/main/resources/com/sistema/com.proyecto2backend/view/CambiarClave.fxml");

                        throw new Exception("No se pudo encontrar CambiarClave.fxml en ninguna ubicación");
                    }

                    System.out.println("CARGANDO ARCHIVO DESDE: " + rutaEncontrada + "");
                    Parent root = loader.load();

                    // *** PARTE IMPORTANTE: Pasar el usuario al controlador ***
                    CambiarClaveController controller = loader.getController();
                    controller.setUsuarioId(usuario.toUpperCase());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Cambio de clave");
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                    System.out.println("Ventana abierta exitosamente");

                } catch (Exception e) {
                    System.out.println("ERROR FINAL");
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de sistema");
                    alert.setHeaderText("Error al abrir ventana");
                    alert.setContentText("No fue posible abrir el menú de cambio de clave: " + e.getMessage());
                    alert.showAndWait();
                }
            });
        }).start();
    }

    @FXML
    protected void salirLogin() { System.exit(0); }

    private void verificarAdministradorPorDefecto() {
        Async.run(
                () -> {
                    try {
                        Administrador admin = administradorSocketService.findByIdentificacion("ADM001");

                        if (admin == null) {
                            Administrador nuevo = new Administrador(0, "ADM001", "ADM001");
                            administradorSocketService.create(nuevo);
                            return Boolean.TRUE; // creado
                        }
                        return Boolean.FALSE;    // ya existía
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                creado -> {
                    if (Boolean.TRUE.equals(creado)) {
                        System.out.println("Administrador ADM001 creado por defecto.");
                    } else {
                        System.out.println("Administrador ADM001 ya existía.");
                    }
                },
                ex -> {
                    System.err.println("Error al verificar administrador por defecto: " + ex.getMessage());
                    ex.printStackTrace();
                }
        );
    }
}
