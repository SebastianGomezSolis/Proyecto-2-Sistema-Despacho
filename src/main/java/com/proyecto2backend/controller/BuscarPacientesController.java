package com.proyecto2backend.controller;

import com.proyecto2backend.logic.PacienteLogica;
import com.proyecto2backend.model.Paciente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuscarPacientesController {
    @FXML private ComboBox<String>  CB_Nombre;
    @FXML private TextField TXF_Nombre;
    @FXML TableView<Paciente>  TV_Pacientes;
    @FXML private TableColumn<Paciente, String> colId;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colFechaNacimiento;

    private final ObservableList<Paciente> listaObservable = FXCollections.observableArrayList();
    private PacienteLogica pacienteLogica;
    private Paciente pacienteSeleccionado;

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    @FXML
    public void initialize() {
        try {
            pacienteLogica = new PacienteLogica();

            CB_Nombre.getItems().addAll("ID", "Nombre");
            CB_Nombre.getSelectionModel().select("Nombre");

            colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdentificacion()));
            colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
            colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
            colFechaNacimiento.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            );

            TV_Pacientes.setItems(listaObservable);
            cargarPacientes();

            TXF_Nombre.textProperty().addListener((obs, oldVal, newVal) -> filtrar());
        } catch (Exception e) {
            Logger.getLogger(BuscarPacientesController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void cargarPacientes() {
        try {
            listaObservable.setAll(pacienteLogica.findAll());
        } catch (Exception e) {
            Logger.getLogger(BuscarPacientesController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void filtrar() {
        try {
            String texto = TXF_Nombre.getText().trim();

            if (texto.isEmpty()) {
                cargarPacientes();
                return;
            }

            String criterio = CB_Nombre.getSelectionModel().getSelectedItem();

            if ("ID".equals(criterio)) {
                listaObservable.setAll(
                        pacienteLogica.findAll().stream()
                                .filter(p -> p.getIdentificacion().toLowerCase().contains(texto.toLowerCase()))
                                .toList()
                );
            } else {
                if (texto.length() == 1) {
                    // Con una letra: buscar nombres que EMPIECEN por esa letra
                    listaObservable.setAll(
                            pacienteLogica.findAll().stream()
                                    .filter(p -> p.getNombre().toLowerCase().startsWith(texto.toLowerCase()))
                                    .toList()
                    );
                } else {
                    // Con 2+ letras: buscar nombres que CONTENGAN el texto
                    listaObservable.setAll(
                            pacienteLogica.findAll().stream()
                                    .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase()))
                                    .toList()
                    );
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BuscarPacientesController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void Aceptar() {
        pacienteSeleccionado = TV_Pacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            TV_Pacientes.getScene().getWindow().hide();
        } else {
            mostrarAlerta("Debe seleccionar un paciente.");
        }
    }

    @FXML
    private void Cancelar() {
        pacienteSeleccionado = null;
        TV_Pacientes.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
