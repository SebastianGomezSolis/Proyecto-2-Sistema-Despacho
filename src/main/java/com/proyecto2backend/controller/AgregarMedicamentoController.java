package com.proyecto2backend.controller;

import com.proyecto2backend.logic.MedicamentoLogica;
import com.proyecto2backend.model.Medicamento;
import com.proyecto2backend.model.Paciente;
import com.proyecto2backend.model.Receta;
import com.proyecto2backend.model.RecetaDetalle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class AgregarMedicamentoController implements Initializable {

    @FXML private TableView<Medicamento> TV_Medicamento;
    @FXML private TableColumn<Medicamento, String> colCodigo;
    @FXML private TableColumn<Medicamento, String> colNombre;
    @FXML private TableColumn<Medicamento, String> colPresentacion;
    @FXML private ProgressIndicator progress;
    @FXML private Button BTT_AceptarMedicamento;

    @FXML private TextField TXF_NombreMedicamento ;

    private Paciente pacienteSeleccionado;
    private Receta recetaActual;

    private ObservableList<Medicamento> listaMedicamentos = FXCollections.observableArrayList();
    MedicamentoLogica logica = new MedicamentoLogica();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            colCodigo.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getCodigo()));
            colNombre.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getNombre()));
            colPresentacion.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getDescripcion()));
            // listaMedicamentos.addAll(logica.findAll());
            TV_Medicamento.setItems(listaMedicamentos);
            cargarMedicamentosAsync();
        } catch (Exception e) {
            Logger.getLogger(AgregarMedicamentoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void cargarMedicamentosAsync() {
        progress.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return logica.findAll();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaMedicamentos.setAll(lista);
                    progress.setVisible(false);
                },
                ex -> {
                    progress.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar los medicamentos");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    public void setPacienteSeleccionado(Paciente paciente) {
        this.pacienteSeleccionado = paciente;
    }

    public void setRecetaActual(Receta receta) {
        this.recetaActual = receta;
    }

    @FXML
    public void aceptarMedicamento() {
        Medicamento seleccionado = TV_Medicamento.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un medicamento.");
            return;
        }

        if (recetaActual == null) {
            recetaActual = new Receta();
            recetaActual.setPaciente(pacienteSeleccionado);
            recetaActual.setFechaEntrega(LocalDate.now());
            recetaActual.setEstado("Confeccionada");
            recetaActual.setDetalles(new RecetaDetalle());
        }

        aceptarMedicamentoAsync(seleccionado, recetaActual);
    }

    private void aceptarMedicamentoAsync(Medicamento seleccionado, Receta recetaCtx) {
        BTT_AceptarMedicamento.setDisable(true);
        progress.setVisible(true);

        Async.run(
                () -> {
                    try {
                        // OPCIONAL: refrescar desde BD por si cambió algo
                        Medicamento full = logica.findByCodigo(seleccionado.getCodigo());
                        return (full != null ? full : seleccionado);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                med -> {
                    BTT_AceptarMedicamento.setDisable(false);
                    progress.setVisible(false);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/Receta.fxml"));
                        Parent root = loader.load();

                        RecetaController controller = loader.getController();
                        controller.setMedicamentoSeleccionado(med);
                        controller.setPacienteSeleccionado(pacienteSeleccionado);
                        controller.setRecetaActual(recetaCtx);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Detalle del Medicamento");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com.proyecto2backend/images/Detalle-medicamento-busqueda.png")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        Object userData = stage.getUserData();
                        if (userData instanceof Receta) {
                            this.recetaActual = (Receta) userData;
                            Stage current = (Stage) TV_Medicamento.getScene().getWindow();
                            current.setUserData(recetaActual);
                            current.close();
                        }
                    } catch (IOException e) {
                        mostrarAlerta("Error", "No se pudo abrir el formulario de detalle: " + e.getMessage());
                    }
                },
                ex -> {
                    BTT_AceptarMedicamento.setDisable(false);
                    progress.setVisible(false);
                    mostrarAlerta("Error", "No se pudo continuar: " + ex.getMessage());
                }
        );
    }
    @FXML
    private void buscarMedicamento(){
        try {
            String criterio = TXF_NombreMedicamento.getText().trim().toLowerCase();
            if(criterio.isEmpty())
            {
                TV_Medicamento.setItems(listaMedicamentos);
                return;
            }

            ObservableList<Medicamento> filtrados =
                    FXCollections.observableArrayList(
                            listaMedicamentos.stream()
                                    .filter(c -> c.getCodigo().toLowerCase().contains(criterio)
                                            || c.getNombre().toLowerCase().contains(criterio)
                                            || c.getDescripcion().toLowerCase().contains(criterio))
                                    .collect(Collectors.toList())
                    );

            TV_Medicamento.setItems(filtrados);
        }
        catch (Exception error)
        {
            mostrarAlerta("Error al buscar el medicamento", error.getMessage());
        }
    }

    @FXML
    public void cancelarMedicamento() {
        Stage stage = (Stage) TV_Medicamento.getScene().getWindow();
        stage.setUserData(null);
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
