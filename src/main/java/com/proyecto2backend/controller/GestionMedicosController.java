package com.proyecto2backend.controller;

import com.proyecto2backend.model.*;
import com.proyecto2backend.servicios.service.*;
import com.proyecto2backend.utilitarios.Sesion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionMedicosController implements Initializable {
    private static final String PREFIJO_ID = "MED";
    private static final String PREFIJO_ID_FARMACEUTA = "FAR";
    private static final String PREFIJO_ID_PACIENTE = "PAC";

    // Tabla y columnas
    @FXML private TableView<Medico> tablaMedicos;
    @FXML private TableColumn<Medico, String> colIdMedico;
    @FXML private TableColumn<Medico, String> colNombreMedico;
    @FXML private TableColumn<Medico, String> colEspecialidadMedico;
    @FXML private ProgressIndicator progressMedicos;
    @FXML private Button btnGuardarMedico;
    @FXML private Button btnBorrarMedico;

    @FXML private TableView<Farmaceuta> tablaFarmaceutas;
    @FXML private TableColumn<Farmaceuta, String> colIdFarmaceutas;
    @FXML private TableColumn<Farmaceuta, String> colNombreFarmaceutas;
    @FXML private ProgressIndicator progressFarmaceutas;
    @FXML private Button btnGuardarFamaceutas;
    @FXML private Button btnBorrarFarmaceutas;

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, String> colIdPaciente;
    @FXML private TableColumn<Paciente, String> colNombrePaciente;
    @FXML private TableColumn<Paciente, String> colTelefonoPaciente;
    @FXML private TableColumn<Paciente, String> colFechaNacimientoPaciente;
    @FXML private ProgressIndicator progressPacientes;
    @FXML private Button btnGuardarPaciente;
    @FXML private Button btnBorrarPaciente;

    @FXML private TableView<Medicamento> tablaMedicamentos;
    @FXML private TableColumn<Medicamento, String> colCodigoMedicamento;
    @FXML private TableColumn<Medicamento, String> colNombreMedicamento;
    @FXML private TableColumn<Medicamento, String> colDescripcionMedicamento;
    @FXML private ProgressIndicator progressMedicamentos;
    @FXML private Button btnGuardarMedicamento;
    @FXML private Button btnBorrarMedicamento;

    @FXML private TableView<RecetaDetalle> tablaPrescripcion;
    @FXML private TableColumn<RecetaDetalle, String> colMedicamento;
    @FXML private TableColumn<RecetaDetalle, String> colPresentacion;
    @FXML private TableColumn<RecetaDetalle, String> colCantidad;
    @FXML private TableColumn<RecetaDetalle, String> colIndicaciones;
    @FXML private TableColumn<RecetaDetalle, String> colDuracion;
    @FXML private ProgressIndicator progressPrescripcion;
    @FXML private Button btnGuardarPrescripcion;
    @FXML private Button btnLimpiarMedico1112;

    // Campos de texto
    @FXML private TextField txtIdMedico;
    @FXML private TextField txtNombreMedico;
    @FXML private TextField txtEspecialidadMedico;
    @FXML private TextField txtBuscarMedico;

    @FXML private TextField txtIdFarmaceutas;
    @FXML private TextField txtNombreFarmaceutas;
    @FXML private TextField txtBuscarFarmaceutas;

    @FXML private TextField txtIdPaciente;
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtTelefonoPaciente;
    @FXML private DatePicker dtpFechaNacimientoPaciente;
    @FXML private TextField txtBuscarPaciente;

    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private TextField txtDescripcionMedicamneto;
    @FXML private TextField txtBuscarMedicamento;

    @FXML private DatePicker dtpFechaRetiro;

    // Tabs
    @FXML private TabPane tabPane;
    @FXML private Tab tabMedicos;
    @FXML private Tab tabPacientes;
    @FXML private Tab tabMedicamentos;
    @FXML private Tab tabDashboard;
    @FXML private Tab tabPrescribir;
    @FXML private Tab tabHistorico;
    @FXML private Tab tabAcercaDe;
    @FXML private Tab tabFarmaceutas;
    @FXML private Tab tabDespacho;

    // DashBoard
    @FXML private Label lblTotalRecetas;
    @FXML private BarChart<String, Number> chartEstado;
    @FXML private CategoryAxis rangosXAxis;
    @FXML private NumberAxis rangosYAxis;
    @FXML private PieChart chartEstadoPie;
    @FXML private ProgressIndicator progressDashBoard;

    //Historico
    @FXML private TableView<Receta> TV_Historico;
    @FXML private TableColumn<Receta, String> colIdHistorico;
    @FXML private TableColumn<Receta, String> colPacienteHistorico;
    @FXML private TableColumn<Receta, String> colFechaHistorico;
    @FXML private TableColumn<Receta, String> colEstadoHistorico;
    @FXML private ComboBox<String> CB_Receta;
    @FXML private TextField TXT_RecetaHistorico;
    @FXML private ProgressIndicator progressHistorico;

    //Despacho
    @FXML private TableView<Receta> TV_Despacho;
    @FXML private TableColumn<Receta, String> colIDRecetaDespacho;
    @FXML private TableColumn<Receta, String> colPacienteDespacho;
    @FXML private TableColumn<Receta, String> colFechaRetiroDespacho;
    @FXML private TableColumn<Receta, String> colEstadoDespacho;
    @FXML private ComboBox<String> CB_RecetaDespacho;
    @FXML private ComboBox<String> CB_NuevoEstadoDespacho;
    @FXML private TextField TXT_RecetaDespacho;
    @FXML private ProgressIndicator progressDespacho;
    @FXML private Button btn_ModificarDespacho;

    @FXML private Label LBL_Nombre;

    private Paciente pacienteSeleccionado;
    private Receta recetaActual;

    // Alamacenamiento de datos
    private final ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();
    private final ObservableList<Farmaceuta> listaFarmaceutas = FXCollections.observableArrayList();
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private final ObservableList<Medicamento> listaMedicamentos = FXCollections.observableArrayList();
    private final ObservableList<RecetaDetalle> listaRecetaDetalles = FXCollections.observableArrayList();
    private final ObservableList<Receta> listaHistoricoRecetas = FXCollections.observableArrayList();
    private final ObservableList<Receta> listaDespachoRecetas = FXCollections.observableArrayList();

    // private final MedicoLogica medicoLogica = new MedicoLogica();
    // private final FarmaceutaLogica farmaceutaLogica = new FarmaceutaLogica();
    // private final PacienteLogica pacienteLogica = new PacienteLogica();
    // private final MedicamentoLogica medicamentoLogica = new MedicamentoLogica();
    // private final RecetaLogica recetaLogica = new RecetaLogica();
    // private DashBoardLogica dashBoardLogica;

    private final DashBoardSocketService dashBoardSocketService = new DashBoardSocketService();
    private final FarmaceutaSocketService farmaceutaSocketService = new FarmaceutaSocketService();
    private final MedicoSocketService medicoSocketService = new MedicoSocketService();
    private final PacienteSocketService pacienteSocketService = new PacienteSocketService();
    private final MedicamentoSocketService medicamentoSocketService = new MedicamentoSocketService();
    private final RecetaSocketService recetaSocketService = new RecetaSocketService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("[DEBUG] Iniciando GestionMedicosController...");

            // Medicos
            colIdMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
            colNombreMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            colEspecialidadMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecialidad()));

            // Farmaceutas
            colIdFarmaceutas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
            colNombreFarmaceutas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

            // Pacientes
            colIdPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
            colNombrePaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            colTelefonoPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
            colFechaNacimientoPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaNacimiento() != null ? cellData.getValue().getFechaNacimiento().toString() : ""));

            // Medicamentos
            colCodigoMedicamento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
            colNombreMedicamento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            colDescripcionMedicamento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));

            colMedicamento.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue() != null && cd.getValue().getMedicamento() != null ? cd.getValue().getMedicamento().getNombre() : ""));

            colPresentacion.setCellValueFactory(cd ->
                    new SimpleStringProperty(
                            cd.getValue() != null && cd.getValue().getMedicamento() != null
                                    ? cd.getValue().getMedicamento().getDescripcion()
                                    : ""
                    )
            );

            colCantidad.setCellValueFactory(cd ->
                    new SimpleStringProperty(String.valueOf(cd.getValue().getCantidad())));

            colIndicaciones.setCellValueFactory(cd ->
                    new SimpleStringProperty(
                            cd.getValue().getIndicaciones() != null ? cd.getValue().getIndicaciones() : ""
                    )
            );

            colDuracion.setCellValueFactory(cd ->
                    new SimpleStringProperty(String.valueOf(cd.getValue().getDiasDuracion())));

            colIdHistorico.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getIdentificacion() != null ? r.getValue().getIdentificacion() : "")
            );
            colPacienteHistorico.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getPaciente() != null ? r.getValue().getPaciente().getNombre() : "")
            );
            colFechaHistorico.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getFechaEntrega() != null ? r.getValue().getFechaEntrega().toString() : "")
            );
            colEstadoHistorico.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getEstado() != null ? r.getValue().getEstado() : "")
            );


            colIDRecetaDespacho.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getIdentificacion() != null ? r.getValue().getIdentificacion() : "")
            );
            colPacienteDespacho.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getPaciente() != null ? r.getValue().getPaciente().getNombre() : "")
            );
            colFechaRetiroDespacho.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getFechaEntrega() != null ? r.getValue().getFechaEntrega().toString() : "")
            );
            colEstadoDespacho.setCellValueFactory(r ->
                    new SimpleStringProperty(r.getValue().getEstado() != null ? r.getValue().getEstado() : "")
            );

            // Inicializar campo ID
            txtIdMedico.setText(PREFIJO_ID);
            txtIdMedico.positionCaret(PREFIJO_ID.length());

            txtIdFarmaceutas.setText(PREFIJO_ID_FARMACEUTA);
            txtIdFarmaceutas.positionCaret(PREFIJO_ID_FARMACEUTA.length());

            txtIdPaciente.setText(PREFIJO_ID_PACIENTE);
            txtIdPaciente.positionCaret(PREFIJO_ID_PACIENTE.length());

            // Listener para prefijo
            txtIdMedico.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.startsWith(PREFIJO_ID)) {
                    txtIdMedico.setText(PREFIJO_ID);
                    txtIdMedico.positionCaret(PREFIJO_ID.length());
                }
            });

            txtIdFarmaceutas.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.startsWith(PREFIJO_ID_FARMACEUTA)) {
                    txtIdFarmaceutas.setText(PREFIJO_ID_FARMACEUTA);
                    txtIdFarmaceutas.positionCaret(PREFIJO_ID_FARMACEUTA.length());
                }
            });

            txtIdPaciente.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.startsWith(PREFIJO_ID_PACIENTE)) {
                    txtIdPaciente.setText(PREFIJO_ID_PACIENTE);
                    txtIdPaciente.positionCaret(PREFIJO_ID_PACIENTE.length());
                }
            });


            // listaMedicos.addAll(medicoLogica.findAll());
            tablaMedicos.setItems(listaMedicos);
            cargarMedicosAsync();

            // listaFarmaceutas.addAll(farmaceutaLogica.findAll());
            tablaFarmaceutas.setItems(listaFarmaceutas);
            cargarFarmaceutasAsync();

            // listaPacientes.addAll(pacienteLogica.findAll());
            tablaPacientes.setItems(listaPacientes);
            cargarPacientesAsync();

            // listaMedicamentos.addAll(medicamentoLogica.findAll());
            tablaMedicamentos.setItems(listaMedicamentos);
            cargarMedicamentoAsync();

            // listaHistoricoRecetas.setAll(recetaLogica.findAll());
            TV_Historico.setItems(listaHistoricoRecetas);
            cargarHistoricoAsync();

            // listaDespachoRecetas.setAll(recetaLogica.findAll());
            TV_Despacho.setItems(listaDespachoRecetas);
            cargarDespachoAsync();

            // List<Receta> recetas = recetaLogica.findAll();
            // listaHistoricoRecetas.setAll(recetas);
            // TV_Historico.setItems(listaHistoricoRecetas);

            // listaDespachoRecetas.setAll(recetas);
            // TV_Despacho.setItems(listaDespachoRecetas);

            // Cargar IDs únicos en el ComboBox
            // List<String> ids = recetas.stream()
            //        .map(Receta::getIdentificacion)
            //        .distinct()
            //        .collect(Collectors.toList());

            // CB_Receta.setItems(FXCollections.observableArrayList(ids));
            // CB_RecetaDespacho.setItems(FXCollections.observableArrayList(ids));

            CB_NuevoEstadoDespacho.setItems(FXCollections.observableArrayList("Proceso", "Lista", "Entregada"));


            //listaRecetas.addAll(recetaLogica.findAll());
            refrescarTablaPrescripcion();

            // this.dashBoardLogica = new DashBoardLogica(recetaLogica);
            cargarGraficosAsync();
        } catch (Exception e) {
            Logger.getLogger(GestionMedicosController.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    private void ocultarSiNoTienePermiso(Tab tab, String codigo) {
        if (!Sesion.puedeAccederModulo(codigo)) {
            tabPane.getTabs().remove(tab);
        }
    }

    public void configurarSegunPermisos() {
        ocultarSiNoTienePermiso(tabPrescribir, "PRESCRIBIR");
        ocultarSiNoTienePermiso(tabMedicos, "GESTION_MEDICOS");
        ocultarSiNoTienePermiso(tabHistorico, "HISTORICO");
        ocultarSiNoTienePermiso(tabDashboard, "DASHBOARD");
        ocultarSiNoTienePermiso(tabAcercaDe, "ACERCA");
        ocultarSiNoTienePermiso(tabPacientes, "GESTION_PACIENTES");
        ocultarSiNoTienePermiso(tabMedicamentos, "GESTION_MEDICAMENTOS");
        ocultarSiNoTienePermiso(tabFarmaceutas, "GESTION_FARMACEUTAS");
        ocultarSiNoTienePermiso(tabDespacho, "GESTION_DESPACHO");
    }

    public void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) tabPane.getScene().getWindow();
            ventanaActual.close();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver al menú de login: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // =========================== MEDICOS ===========================
    @FXML
    private void agregarMedico() {
        try {
            String identificacion = txtIdMedico.getText().trim();
            String nombre = txtNombreMedico.getText().trim();
            String especialidad = txtEspecialidadMedico.getText().trim();
            String numero = identificacion.substring(PREFIJO_ID.length());

            if (identificacion.isEmpty() || nombre.isEmpty() || especialidad.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
                return;
            }

            if (!numero.matches("\\d+")) {
                mostrarAlerta("Error", "El ID debe contener solo números después de " + PREFIJO_ID, Alert.AlertType.ERROR);
                return;
            }

            Medico nuevoMedico = new Medico(0, identificacion, identificacion, nombre, especialidad);

            agregarMedicoAsync(nuevoMedico, tablaMedicos);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void agregarMedicoAsync(Medico medico, TableView<Medico> tablaMedicos) {
        btnGuardarMedico.setDisable(true);
        progressMedicos.setVisible(true);
        final boolean esUpdate = medico.getId() > 0;

        Async.run(
                () -> {
                    try {
                        Medico existente = medicoSocketService.findByIdentificacion(medico.getIdentificacion());
                        if (existente != null) {
                            medico.setId(existente.getId());
                        }
                        if (esUpdate) {
                            medicoSocketService.update(medico);
                            return medico;
                        } else {
                            Medico creado = medicoSocketService.create(medico);
                            if (creado != null && creado.getId() > 0) {
                                medico.setId(creado.getId());
                            }
                            return medico;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    btnGuardarMedico.setDisable(false);
                    progressMedicos.setVisible(false);

                    if (tablaMedicos != null) {
                        if (esUpdate) {
                            tablaMedicos.refresh();
                        } else {
                            tablaMedicos.getItems().add(guardado);
                        }
                    }
                    new Alert(Alert.AlertType.INFORMATION,
                            (esUpdate ? "Medico actualizado (ID: " : "Medico guardado (ID: ")
                                    + guardado.getId() + ")").showAndWait();
                    limpiarCamposMedico();
                },
                ex -> {
                    btnGuardarMedico.setDisable(false);
                    progressMedicos.setVisible(false);
                    mostrarAlerta("Error", "No se pudo guardar el médico: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void eliminarMedico() {
        try {
            Medico seleccionado = tablaMedicos.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                mostrarAlerta(
                        "Selección requerida",
                        "Por favor, seleccione un médico de la tabla para eliminar.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Desea eliminar al médico seleccionado?");
            confirmacion.setContentText("Médico: " + seleccionado.getNombre() + " (" + seleccionado.getId() + ")");

            // Mostrar y esperar confirmación
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    eliminarMedicoAsync(seleccionado, tablaMedicos);
                }
            });

        } catch (Exception error) {
            mostrarAlerta("Error inesperado", "Ocurrió un error al intentar eliminar al médico. Inténtelo de nuevo.", Alert.AlertType.ERROR);
            error.printStackTrace();
        }
    }

    private void eliminarMedicoAsync(Medico seleccionado, TableView<Medico> tabla) {
        btnBorrarMedico.setDisable(true);
        progressMedicos.setVisible(true);

        Async.run(
                () -> {
                    try {
                        medicoSocketService.deleteById(seleccionado.getId());
                        return seleccionado.getId();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                idEliminado -> {
                    btnBorrarMedico.setDisable(false);
                    progressMedicos.setVisible(false);
                    listaMedicos.removeIf(m -> m.getId() == idEliminado);
                    if (tabla != null) tabla.refresh();
                    mostrarAlerta("Éxito", "El médico ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    limpiarCamposMedico();
                },
                ex -> {
                    btnBorrarMedico.setDisable(false);
                    progressMedicos.setVisible(false);
                    mostrarAlerta("Error al eliminar", ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void buscarMedico() {
        try {
            String criterio = txtBuscarMedico.getText().trim().toLowerCase();
            if (criterio.isEmpty()) {
                tablaMedicos.setItems(listaMedicos);
                return;
            }

            ObservableList<Medico> filtrados =
                    FXCollections.observableArrayList(
                            listaMedicos.stream()
                                    .filter(m -> m.getIdentificacion().toLowerCase().contains(criterio)
                                            || m.getNombre().toLowerCase().contains(criterio)
                                            || m.getEspecialidad().toLowerCase().contains(criterio))
                                    .collect(Collectors.toList())
                    );

            tablaMedicos.setItems(filtrados);
        } catch (Exception error) {
            mostrarAlerta("Error al buscar el medico", "Volver a intentarlo", Alert.AlertType.ERROR);
        }
    }

    public void cargarMedicosAsync() {
        progressMedicos.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return medicoSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaMedicos.setAll(lista);
                    progressMedicos.setVisible(false);
                },
                ex -> {
                    progressMedicos.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar a los medicos");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    @FXML
    private void mostrarReporteMedico() {
        try {
            limpiarCamposMedico();
            refrescarTablaMedico();
        } catch (Exception e) {
            Logger.getLogger(GestionMedicosController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void limpiarCamposMedico() {
        txtIdMedico.setText(PREFIJO_ID);
        txtIdMedico.positionCaret(PREFIJO_ID.length());
        txtNombreMedico.clear();
        txtEspecialidadMedico.clear();
        txtBuscarMedico.clear();
    }

    private void refrescarTablaMedico() {
        progressMedicos.setVisible(true);

        Async.run(
                () -> {
                    try {
                        return medicoSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                medicos -> {
                    progressMedicos.setVisible(false);
                    listaMedicos.setAll(medicos);
                    tablaMedicos.setItems(listaMedicos);
                    tablaMedicos.refresh();
                },
                ex -> {
                    progressMedicos.setVisible(false);
                    mostrarAlerta("Error", "No se pudo refrescar la tabla de médicos: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    // ======================== FARMACEUTAS =========================
    @FXML
    private void agregarFarmaceuta() {
        try {
            String identificacion = txtIdFarmaceutas.getText().trim();
            String nombre = txtNombreFarmaceutas.getText().trim();

            if (identificacion.isEmpty() || nombre.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
                return;
            }

            // Mismo estilo que usaste en Médicos: valida que tras el prefijo haya números
            if (!identificacion.startsWith(PREFIJO_ID_FARMACEUTA) || identificacion.length() <= PREFIJO_ID_FARMACEUTA.length()) {
                mostrarAlerta("Error", "El ID debe iniciar con " + PREFIJO_ID_FARMACEUTA + " y tener números después.", Alert.AlertType.ERROR);
                return;
            }
            String numero = identificacion.substring(PREFIJO_ID_FARMACEUTA.length());
            if (!numero.matches("\\d+")) {
                mostrarAlerta("Error", "El ID debe contener solo números después de " + PREFIJO_ID_FARMACEUTA, Alert.AlertType.ERROR);
                return;
            }

            // Construcción siguiendo tu firma de modelo (id, identificacion, usuario?, nombre)
            Farmaceuta nuevoFarmaceuta = new Farmaceuta(0, identificacion, identificacion, nombre);

            agregarFarmaceutaAsync(nuevoFarmaceuta, tablaFarmaceutas);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void agregarFarmaceutaAsync(Farmaceuta farmaceuta, TableView<Farmaceuta> tablaFarmaceutas) {
        btnGuardarFamaceutas.setDisable(true);
        progressFarmaceutas.setVisible(true);
        final boolean esUpdate = farmaceuta.getId() > 0;

        Async.run(
                () -> {
                    try {
                        Farmaceuta existente = farmaceutaSocketService.findByIdentificacion(farmaceuta.getIdentificacion());
                        if (existente != null) {
                            farmaceuta.setId(existente.getId());
                        }
                        if (esUpdate) {
                            farmaceutaSocketService.update(farmaceuta);
                            return farmaceuta;
                        } else {
                            Farmaceuta creado = farmaceutaSocketService.create(farmaceuta);
                            if (creado != null && creado.getId() > 0) {
                                farmaceuta.setId(creado.getId());
                            }
                            return farmaceuta;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    btnGuardarFamaceutas.setDisable(false);
                    progressFarmaceutas.setVisible(false);

                    if (tablaFarmaceutas != null) {
                        if (esUpdate) {
                            tablaFarmaceutas.refresh();
                        } else {
                            tablaFarmaceutas.getItems().add(guardado);
                        }
                    }

                    new Alert(Alert.AlertType.INFORMATION,
                            (esUpdate ? "Farmaceuta modificado (ID: " : "Farmaceuta agregado (ID: ")
                                    + guardado.getId() + ")").showAndWait();

                    limpiarCamposFarmaceuta();
                },
                ex -> {
                    btnGuardarFamaceutas.setDisable(false);
                    progressFarmaceutas.setVisible(false);
                    mostrarAlerta("Error", "No se pudo guardar el farmaceuta: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void eliminarFarmaceuta() {
        try {
            Farmaceuta seleccionado = tablaFarmaceutas.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                mostrarAlerta(
                        "Selección requerida",
                        "Por favor, seleccione un farmaceuta de la tabla para eliminar.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Desea eliminar al médico seleccionado?");
            confirmacion.setContentText("Farmaceuta: " + seleccionado.getNombre() + " (" + seleccionado.getId() + ")");

            // Mostrar y esperar confirmacion
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    eliminarFarmaceutaAsync(seleccionado, tablaFarmaceutas);
                }
            });

        } catch (Exception error) {
            mostrarAlerta("Error inesperado", "Ocurrió un error al intentar eliminar al farmaceuta. Inténtelo de nuevo.", Alert.AlertType.ERROR);
            error.printStackTrace();
        }
    }

    private void eliminarFarmaceutaAsync(Farmaceuta seleccionado, TableView<Farmaceuta> tabla) {
        btnBorrarFarmaceutas.setDisable(true);
        progressFarmaceutas.setVisible(true);

        Async.run(
                () -> {
                    try {
                        farmaceutaSocketService.deleteById(seleccionado.getId());
                        return seleccionado.getId();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                idEliminado -> {
                    btnBorrarFarmaceutas.setDisable(false);
                    progressFarmaceutas.setVisible(false);
                    listaFarmaceutas.removeIf(f -> f.getId() == idEliminado);
                    if (tabla != null) tabla.refresh();
                    mostrarAlerta("Éxito", "El farmaceuta ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    limpiarCamposFarmaceuta();
                },
                ex -> {
                    btnBorrarFarmaceutas.setDisable(false);
                    progressFarmaceutas.setVisible(false);
                    mostrarAlerta("Error al eliminar", ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void buscarFarmaceuta() {
        try {
            String criterio = txtBuscarFarmaceutas.getText().trim().toLowerCase();
            if (criterio.isEmpty()) {
                tablaFarmaceutas.setItems(listaFarmaceutas);
                return;
            }

            ObservableList<Farmaceuta> filtrados =
                    FXCollections.observableArrayList(
                            listaFarmaceutas.stream()
                                    .filter(f -> f.getIdentificacion().toLowerCase().contains(criterio)
                                            || f.getNombre().toLowerCase().contains(criterio))
                                    .collect(Collectors.toList())
                    );

            tablaFarmaceutas.setItems(filtrados);
        } catch (Exception error) {
            mostrarAlerta("Error al buscar el farmaceuta", "Volver a intentarlo", Alert.AlertType.ERROR);
        }
    }

    public void cargarFarmaceutasAsync() {
        progressFarmaceutas.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return farmaceutaSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaFarmaceutas.setAll(lista);
                    progressFarmaceutas.setVisible(false);
                },
                ex -> {
                    progressFarmaceutas.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar a los farmaceutas");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    @FXML
    private void mostrarReporteFarmaceuta() {
        try {
            limpiarCamposFarmaceuta();
            refrescarTablaFarmaceuta();
        } catch (Exception error) {
            Logger.getLogger(GestionMedicosController.class.getName()).log(Level.SEVERE, null, error);
        }
    }

    @FXML
    private void limpiarCamposFarmaceuta() {
        txtIdFarmaceutas.setText(PREFIJO_ID_FARMACEUTA);
        txtIdFarmaceutas.positionCaret(PREFIJO_ID_FARMACEUTA.length());
        txtNombreFarmaceutas.clear();
        txtBuscarFarmaceutas.clear();
    }

    private void refrescarTablaFarmaceuta() {
        progressFarmaceutas.setVisible(true);

        Async.run(
                () -> {
                    try {
                        return farmaceutaSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                farmaceutas -> {
                    progressFarmaceutas.setVisible(false);
                    listaFarmaceutas.setAll(farmaceutas);
                    tablaFarmaceutas.setItems(listaFarmaceutas);
                    tablaFarmaceutas.refresh();
                },
                ex -> {
                    progressFarmaceutas.setVisible(false);
                    mostrarAlerta("Error", "No se pudo refrescar la tabla de farmaceutas: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    // =========================== PACIENTES ===========================
    @FXML
    private void agregarPaciente() {
        try {
            String identificacion = txtIdPaciente.getText().trim();
            String nombre = txtNombrePaciente.getText().trim();
            String telefono = txtTelefonoPaciente.getText().trim();
            LocalDate fechaNacimiento = dtpFechaNacimientoPaciente.getValue();

            if (identificacion.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || fechaNacimiento == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
                return;
            }

            Paciente nuevoPaciente = new Paciente(0, identificacion, nombre, fechaNacimiento, telefono);

            agregarPacienteAsync(nuevoPaciente, tablaPacientes);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void agregarPacienteAsync(Paciente paciente, TableView<Paciente> tablaPacientes) {
        btnGuardarPaciente.setDisable(true);
        progressPacientes.setVisible(true);
        final boolean esUpdate = paciente.getId() > 0;

        Async.run(
                () -> {
                    try {
                        Paciente existente = pacienteSocketService.findByIdentificacion(paciente.getIdentificacion());
                        if (existente != null) {
                            paciente.setId(existente.getId());
                        }
                        if (esUpdate) {
                            pacienteSocketService.update(paciente);
                        } else {
                            Paciente creado = pacienteSocketService.create(paciente);
                            if (creado != null && creado.getId() > 0) {
                                paciente.setId(creado.getId());
                            }
                        }
                        return paciente;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    btnGuardarPaciente.setDisable(false);
                    progressPacientes.setVisible(false);

                    if (tablaPacientes != null) {
                        if (esUpdate) {
                            tablaPacientes.refresh();
                        } else {
                            tablaPacientes.getItems().add(guardado);
                        }
                    }
                    new Alert(Alert.AlertType.INFORMATION,
                            (esUpdate ? "Paciente modificado (ID: " : "Paciente agregado (ID: ")
                                    + guardado.getId() + ")").showAndWait();
                    limpiarCamposPaciente();
                },
                ex -> {
                    btnGuardarPaciente.setDisable(false);
                    progressPacientes.setVisible(false);
                    mostrarAlerta("Error", "No se pudo guardar el paciente: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void eliminarPaciente() {
        try {
            Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                mostrarAlerta(
                        "Selección requerida",
                        "Por favor, seleccione un paciente de la tabla para eliminar.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Desea eliminar al paciente seleccionado?");
            confirmacion.setContentText("Paciente: " + seleccionado.getNombre() + " (" + seleccionado.getId() + ")");

            // Mostrar y esperar confirmacion
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    eliminarPacienteAsync(seleccionado, tablaPacientes);
                }
            });

        } catch (Exception error) {
            mostrarAlerta("Error inesperado", "Ocurrió un error al intentar eliminar al paciente. Inténtelo de nuevo.", Alert.AlertType.ERROR);
            error.printStackTrace();
        }
    }

    private void eliminarPacienteAsync(Paciente seleccionado, TableView<Paciente> tabla) {
        btnBorrarPaciente.setDisable(true);
        progressPacientes.setVisible(true);

        Async.run(
                () -> {
                    try {
                        pacienteSocketService.deleteById(seleccionado.getId());
                        return seleccionado.getId();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                idEliminado -> {
                    btnBorrarPaciente.setDisable(false);
                    progressPacientes.setVisible(false);
                    listaPacientes.removeIf(p -> p.getId() == idEliminado);
                    if (tabla != null) tabla.refresh();
                    mostrarAlerta("Éxito", "El paciente ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    limpiarCamposPaciente();
                },
                ex -> {
                    btnBorrarPaciente.setDisable(false);
                    progressPacientes.setVisible(false);
                    mostrarAlerta("Error al eliminar", ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }


    @FXML
    private void buscarPaciente() {
        try {
            String criterio = txtBuscarPaciente.getText().trim().toLowerCase();
            if (criterio.isEmpty()) {
                tablaPacientes.setItems(listaPacientes);
                return;
            }

            ObservableList<Paciente> filtrados =
                    FXCollections.observableArrayList(
                            listaPacientes.stream()
                                    .filter(p -> p.getIdentificacion().toLowerCase().contains(criterio)
                                            || p.getNombre().toLowerCase().contains(criterio))
                                    .collect(Collectors.toList())
                    );
            tablaPacientes.setItems(filtrados);
        } catch (Exception error) {
            mostrarAlerta("Error al buscar el paciente", "Volver a intentarlo", Alert.AlertType.ERROR);
        }
    }

    public void cargarPacientesAsync() {
        progressPacientes.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return pacienteSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaPacientes.setAll(lista);
                    progressPacientes.setVisible(false);
                },
                ex -> {
                    progressPacientes.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar a los pacientes");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    @FXML
    private void mostrarReportePaciente() {
        try {
            limpiarCamposPaciente();
            refrescarTablaPaciente();
        } catch (Exception error) {
            Logger.getLogger(GestionMedicosController.class.getName()).log(Level.SEVERE, null, error);
        }
    }

    @FXML
    private void limpiarCamposPaciente() {
        txtIdPaciente.setText(PREFIJO_ID_PACIENTE);
        txtIdPaciente.positionCaret(PREFIJO_ID_PACIENTE.length());
        txtNombrePaciente.clear();
        txtTelefonoPaciente.clear();
        dtpFechaNacimientoPaciente.setValue(null);
        txtBuscarPaciente.clear();
    }

    private void refrescarTablaPaciente() {
        progressPacientes.setVisible(true);

        Async.run(
                () -> {
                    try {
                        return pacienteSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                pacientes -> {
                    progressPacientes.setVisible(false);
                    listaPacientes.setAll(pacientes);
                    tablaPacientes.setItems(listaPacientes);
                    tablaPacientes.refresh();
                },
                ex -> {
                    progressPacientes.setVisible(false);
                    mostrarAlerta("Error", "No se pudo refrescar la tabla de pacientes: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }


    // =========================== MEDICAMENTOS ===========================
    @FXML
    private void agregarMedicamento() {
        try {
            String codigo = txtCodigoMedicamento.getText().trim();
            String nombre = txtNombreMedicamento.getText().trim();
            String descripcion = txtDescripcionMedicamneto.getText().trim(); // (mantengo tu fx:id)

            if (codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
                return;
            }

            Medicamento nuevoMedicamento = new Medicamento(0, codigo, nombre, descripcion);

            agregarMedicamentoAsync(nuevoMedicamento, tablaMedicamentos);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void agregarMedicamentoAsync(Medicamento medicamento, TableView<Medicamento> tablaMedicamentos) {
        btnGuardarMedicamento.setDisable(true);
        progressMedicamentos.setVisible(true);
        final boolean esUpdate = medicamento.getId() > 0;

        Async.run(
                () -> {
                    try {
                        Medicamento existente = medicamentoSocketService.findByCodigo(medicamento.getCodigo());
                        if (existente != null) {
                            medicamento.setId(existente.getId());
                        }
                        if (esUpdate) {
                            medicamentoSocketService.update(medicamento);
                        } else {
                            Medicamento creado = medicamentoSocketService.create(medicamento);
                            if (creado != null && creado.getId() > 0) {
                                medicamento.setId(creado.getId());
                            }
                        }
                        return medicamento;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    btnGuardarMedicamento.setDisable(false);
                    progressMedicamentos.setVisible(false);

                    if (tablaMedicamentos != null) {
                        if (esUpdate) {
                            tablaMedicamentos.refresh();
                        } else {
                            tablaMedicamentos.getItems().add(guardado);
                        }
                    }
                    new Alert(Alert.AlertType.INFORMATION,
                            (esUpdate ? "Medicamento modificado (ID: " : "Medicamento agregado (ID: ")
                                    + guardado.getId() + ")").showAndWait();
                    limpiarCampoMedicamento();
                },
                ex -> {
                    btnGuardarMedicamento.setDisable(false);
                    progressMedicamentos.setVisible(false);
                    mostrarAlerta("Error", "No se pudo guardar el medicamento: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void eliminarMedicamento() {
        try {
            Medicamento seleccionado = tablaMedicamentos.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                mostrarAlerta(
                        "Selección requerida",
                        "Por favor, seleccione un medicamento de la tabla para eliminar.",
                        Alert.AlertType.WARNING
                );
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Desea eliminar al medicamento seleccionado?");
            confirmacion.setContentText("Medicamento: " + seleccionado.getNombre() + " (" + seleccionado.getCodigo() + ")");

            // Mostrar y esperar confirmación
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    eliminarMedicamentoAsync(seleccionado, tablaMedicamentos);
                }
            });

        } catch (Exception error) {
            mostrarAlerta("Error inesperado", "Ocurrió un error al intentar eliminar el medicamento. Inténtelo de nuevo.", Alert.AlertType.ERROR);
            error.printStackTrace();
        }
    }

    private void eliminarMedicamentoAsync(Medicamento seleccionado, TableView<Medicamento> tabla) {
        btnBorrarMedicamento.setDisable(true);
        progressMedicamentos.setVisible(true);

        Async.run(
                () -> {
                    try {
                        medicamentoSocketService.deleteById(seleccionado.getId());
                        return seleccionado.getId();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                idEliminado -> {
                    btnBorrarMedicamento.setDisable(false);
                    progressMedicamentos.setVisible(false);
                    listaMedicamentos.removeIf(m -> m.getId() == idEliminado);
                    if (tabla != null) tabla.refresh();
                    mostrarAlerta("Éxito", "El medicamento ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
                    limpiarCampoMedicamento();
                },
                ex -> {
                    btnBorrarMedicamento.setDisable(false);
                    progressMedicamentos.setVisible(false);
                    mostrarAlerta("Error al eliminar", ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }


    @FXML
    private void buscarMedicamento() {
        try {
            String criterio = txtBuscarMedicamento.getText().trim().toLowerCase();
            if (criterio.isEmpty()) {
                tablaMedicamentos.setItems(listaMedicamentos);
                return;
            }

            ObservableList<Medicamento> filtrados =
                    FXCollections.observableArrayList(
                            listaMedicamentos.stream()
                                    .filter(m -> m.getCodigo().toLowerCase().contains(criterio)
                                            || m.getNombre().toLowerCase().contains(criterio)
                                            || m.getDescripcion().toLowerCase().contains(criterio))
                                    .collect(Collectors.toList())
                    );

            tablaMedicamentos.setItems(filtrados);
        } catch (Exception error) {
            mostrarAlerta("Error al buscar el medicamento", "Volver a intentarlo", Alert.AlertType.ERROR);
        }
    }

    public void cargarMedicamentoAsync() {
        progressMedicamentos.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return medicamentoSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaMedicamentos.setAll(lista);
                    progressMedicamentos.setVisible(false);
                },
                ex -> {
                    progressMedicamentos.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar a los medicamentos");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    @FXML
    private void mostrarReporteMedicamento() {
        try {
            limpiarCampoMedicamento();
            refrescarTablaMedicamento();
        } catch (Exception error) {
            Logger.getLogger(GestionMedicosController.class.getName()).log(Level.SEVERE, null, error);
        }
    }

    @FXML
    private void limpiarCampoMedicamento() {
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
        txtDescripcionMedicamneto.clear();
        txtBuscarMedicamento.clear();
    }

    private void refrescarTablaMedicamento() {
        progressMedicamentos.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return medicamentoSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                medicamentos -> {
                    progressMedicamentos.setVisible(false);
                    listaMedicamentos.setAll(medicamentos);
                    tablaMedicamentos.setItems(listaMedicamentos);
                    tablaMedicamentos.refresh();
                },
                ex -> {
                    progressMedicamentos.setVisible(false);
                    mostrarAlerta("Error", "No se pudo refrescar la tabla de medicamentos: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    // =========================== ALERTAS ===========================
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    // =========================== PRESCRIBIR =======================
    @FXML
    private void buscarPacientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/BuscarPacientes.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Buscar Paciente");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com.proyecto2backend/images/paciente-busqueda.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            BuscarPacientesController controller = loader.getController();
            Paciente seleccionado = controller.getPacienteSeleccionado();

            if (seleccionado != null) {
                this.pacienteSeleccionado = seleccionado;
                this.recetaActual = new Receta();
                recetaActual.setPaciente(pacienteSeleccionado);
                recetaActual.setFechaEntrega(LocalDate.now());
                recetaActual.setEstado("Confeccionada");
                recetaActual.setDetalles(new RecetaDetalle());

                LBL_Nombre.setText(pacienteSeleccionado.getNombre());
                refrescarTablaPrescripcion();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el formulario de búsqueda.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void agregarMediPrescripcion() {
        try {
            if (pacienteSeleccionado == null) {
                mostrarAlerta("Error", "Debe seleccionar un paciente primero antes de agregar medicamentos.", Alert.AlertType.WARNING);
                return;
            }
            if (recetaActual == null) {
                recetaActual = new Receta();
                recetaActual.setPaciente(pacienteSeleccionado);
                recetaActual.setFechaEntrega(LocalDate.now());
                recetaActual.setEstado("Confeccionada");
                recetaActual.setDetalles(new RecetaDetalle());
            }


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/AgregarMedicamento.fxml"));
            Parent root = loader.load();

            AgregarMedicamentoController controller = loader.getController();
            controller.setPacienteSeleccionado(pacienteSeleccionado);
            controller.setRecetaActual(recetaActual);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Medicamento");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com.proyecto2backend/images/medicamento-busqueda.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Object userData = stage.getUserData();
            if (userData instanceof Receta) {
                recetaActual = (Receta) userData;
                refrescarTablaPrescripcion();
            }
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar formulario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Medico obtenerMedicoActual() {
        return (Medico) Sesion.getUsuarioActual();
    }

    @FXML
    private void guardarReceta() {
        try {
            if (recetaActual == null || recetaActual.getDetalles() == null) {
                mostrarAlerta("Error", "No hay medicamentos en la receta.", Alert.AlertType.WARNING);
                return;
            }
            LocalDate fechaRetiro = dtpFechaRetiro.getValue();
            if (fechaRetiro != null) {
                recetaActual.setFechaEntrega(fechaRetiro);
            }
            guardarRecetaAsync(recetaActual);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar receta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void guardarRecetaAsync(Receta receta) {
        btnGuardarPrescripcion.setDisable(true);
        progressPrescripcion.setVisible(true);

        Async.run(
                () -> {
                    try {
                        Receta creada = recetaSocketService.create(receta);
                        if (creada != null) {
                            receta.setId(creada.getId());
                            receta.setIdentificacion(creada.getIdentificacion());
                            receta.setEstado(creada.getEstado());
                            receta.setFechaEntrega(creada.getFechaEntrega());
                        }
                        return receta;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                guardada -> {
                    progressPrescripcion.setVisible(false);
                    btnGuardarPrescripcion.setDisable(false);

                    mostrarAlerta("Éxito", "Receta guardada correctamente.", Alert.AlertType.INFORMATION);

                    recetaActual = null;
                    refrescarTablaPrescripcion();
                    cargarGraficosAsync();
                    refrescarTablaHistorico();
                    dtpFechaRetiro.setValue(LocalDate.now());
                    LBL_Nombre.setText("Nombre");
                },
                ex -> {
                    progressPrescripcion.setVisible(false);
                    btnGuardarPrescripcion.setDisable(false);
                    mostrarAlerta("Error", "Error al guardar receta: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }

    @FXML
    private void descartarMedicamento() {
        RecetaDetalle sel = tablaPrescripcion.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione un medicamento para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Desea eliminar el medicamento seleccionado?");
        confirmacion.setContentText(
                sel.getMedicamento() != null ? "Medicamento: " + sel.getMedicamento().getNombre() : ""
        );

        confirmacion.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK && recetaActual != null) {
                // Si tu Receta solo guarda UN RecetaDetalle:
                recetaActual.setDetalles(null);
                refrescarTablaPrescripcion();
            }
        });
    }

    @FXML
    private void modificarDetalles() {
        RecetaDetalle seleccionado = tablaPrescripcion.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un medicamento para modificar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/Receta.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Detalle de Receta");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com.proyecto2backend/images/Detalle-medicamento-busqueda.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            RecetaController controller = loader.getController();
            controller.setRecetaActual(recetaActual);
            controller.setReceta(seleccionado, true);

            stage.showAndWait();

            Object userData = stage.getUserData();
            if (userData instanceof Receta) {
                recetaActual = (Receta) userData;
                refrescarTablaPrescripcion();
            }
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al abrir formulario de modificación: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void limpiarM() {
        LBL_Nombre.setText("");
        dtpFechaRetiro.setValue(null);
        if (recetaActual != null) {
            recetaActual.setDetalles(null); // elimina el detalle, no lo dejes con medicamento=null
        }
        refrescarTablaPrescripcion();
    }

    private void refrescarTablaPrescripcion() {
        tablaPrescripcion.getItems().clear();
        if (recetaActual != null && recetaActual.getDetalles() != null) {
            tablaPrescripcion.getItems().add(recetaActual.getDetalles());
        }
    }

    // DASHBOARD
    public void cargarGraficosAsync() {
        Async.run(
                () -> {
                    try {
                        // Se obtiene la información desde el servicio (NO desde la lógica)
                        int total = dashBoardSocketService.totalRecetas();
                        LinkedHashMap<String, Long> estados = dashBoardSocketService.recetasPorEstado();
                        return new Object[]{ total, estados };
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                result -> {
                    // progressDashboard.setVisible(false);
                    int total = (int) result[0];
                    @SuppressWarnings("unchecked")
                    LinkedHashMap<String, Long> estados = (LinkedHashMap<String, Long>) result[1];

                    try {
                        chartEstado.setLegendVisible(false);
                        chartEstadoPie.setLegendVisible(false);
                        lblTotalRecetas.setText(String.valueOf(total));

                        // --- BARRAS ---
                        if (chartEstado != null) {
                            chartEstado.getData().clear();
                            XYChart.Series<String, Number> serie = new XYChart.Series<>();

                            for (Map.Entry<String, Long> e : estados.entrySet()) {
                                XYChart.Data<String, Number> data = new XYChart.Data<>(e.getKey(), e.getValue());
                                serie.getData().add(data);

                                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                                    if (newNode != null) {
                                        switch (e.getKey()) {
                                            case "Confeccionada" ->
                                                    newNode.setStyle("-fx-bar-fill: #7B2CBF;");
                                            case "Proceso" ->
                                                    newNode.setStyle("-fx-bar-fill: #9D4EDD;");
                                            case "Lista" ->
                                                    newNode.setStyle("-fx-bar-fill: #C77DFF;");
                                            case "Entregada" ->
                                                    newNode.setStyle("-fx-bar-fill: #E0AAFF;");
                                        }
                                    }
                                });
                            }
                            chartEstado.getData().add(serie);
                        }

                        // --- PIE ---
                        if (chartEstadoPie != null) {
                            chartEstadoPie.getData().clear();
                            for (Map.Entry<String, Long> e : estados.entrySet()) {
                                if (e.getValue() > 0) {
                                    PieChart.Data data = new PieChart.Data(e.getKey(), e.getValue());
                                    chartEstadoPie.getData().add(data);

                                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                                        if (newNode != null) {
                                            switch (e.getKey()) {
                                                case "Confeccionada" ->
                                                        newNode.setStyle("-fx-pie-color: #7B2CBF;");
                                                case "Proceso" ->
                                                        newNode.setStyle("-fx-pie-color: #9D4EDD;");
                                                case "Lista" ->
                                                        newNode.setStyle("-fx-pie-color: #C77DFF;");
                                                case "Entregada" ->
                                                        newNode.setStyle("-fx-pie-color: #E0AAFF;");
                                            }
                                        }
                                    });

                                    if (data.getNode() != null) {
                                        switch (e.getKey()) {
                                            case "Confeccionada" ->
                                                    data.getNode().setStyle("-fx-pie-color: #7B2CBF;");
                                            case "Proceso" ->
                                                    data.getNode().setStyle("-fx-pie-color: #9D4EDD;");
                                            case "Lista" ->
                                                    data.getNode().setStyle("-fx-pie-color: #C77DFF;");
                                            case "Entregada" ->
                                                    data.getNode().setStyle("-fx-pie-color: #E0AAFF;");
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        System.err.println("Error al pintar los gráficos: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                ex -> {
                    // progressDashboard.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar los gráficos del dashboard");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    // HISTORICO
    @FXML
    private void limpiarHistorico() {
        TXT_RecetaHistorico.clear();
        TV_Historico.setItems(listaHistoricoRecetas);
    }

    @FXML
    private void buscarRecetaHistorico() {
        try {
            String criterioIdCombo = CB_Receta.getValue() != null ? CB_Receta.getValue().trim().toLowerCase() : "";
            String criterioTexto = TXT_RecetaHistorico.getText().trim().toLowerCase();

            List<Receta> resultados = listaHistoricoRecetas.stream()
                    .filter(r -> {
                        if (r.getIdentificacion() == null) return false;

                        boolean coincideCombo = criterioIdCombo.isEmpty() || r.getIdentificacion().toLowerCase().contains(criterioIdCombo);
                        boolean coincideTexto = criterioTexto.isEmpty() || r.getIdentificacion().toLowerCase().contains(criterioTexto);
                        return coincideCombo && coincideTexto;
                    })
                    .collect(Collectors.toList());

            TV_Historico.setItems(FXCollections.observableArrayList(resultados));
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al buscar receta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void verDetallesReceta() {
        Receta seleccionada = TV_Historico.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Debe seleccionar una receta de la tabla.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.proyecto2backend/view/Receta.fxml"));
            Parent root = loader.load();

            RecetaController controller = loader.getController();

            // Llamar al metodo para mostrar la receta completa
            controller.setRecetaH(seleccionada, false);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalle de Receta");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la receta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void cargarHistoricoAsync() {
        progressHistorico.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return recetaSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                recetas -> {
                    listaHistoricoRecetas.setAll(recetas);
                    List<String> ids = recetas.stream()
                            .map(Receta::getIdentificacion)
                            .distinct()
                            .collect(Collectors.toList());
                    CB_Receta.setItems(FXCollections.observableArrayList(ids));
                    progressHistorico.setVisible(false);
                },
                ex -> {
                    progressHistorico.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar el historico de recetas");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    private void refrescarTablaHistorico() {
        progressHistorico.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return recetaSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                recetas -> {
                    progressHistorico.setVisible(false);
                    listaHistoricoRecetas.clear();
                    listaHistoricoRecetas.addAll(recetas);
                    TV_Historico.refresh();
                },
                ex -> {
                    progressHistorico.setVisible(false);
                    mostrarAlerta("Error", "Error al refrescar la tabla de histórico: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }


    // DESPACHO
    @FXML
    private void limpiarDespacho() {
        TXT_RecetaDespacho.clear();
        TV_Despacho.setItems(listaDespachoRecetas);
    }

    @FXML
    private void buscarRecetaDespacho() {
        String criterioIdCombo = CB_RecetaDespacho.getValue() != null ? CB_RecetaDespacho.getValue().trim().toLowerCase() : "";
        String criterioTexto = TXT_RecetaDespacho.getText().trim().toLowerCase();

        List<Receta> resultados = listaDespachoRecetas.stream()
                .filter(r -> {
                    if (r.getIdentificacion() == null) return false;

                    boolean coincideCombo = criterioIdCombo.isEmpty() || r.getIdentificacion().toLowerCase().contains(criterioIdCombo);
                    boolean coincideTexto = criterioTexto.isEmpty() || r.getIdentificacion().toLowerCase().contains(criterioTexto);
                    return coincideCombo && coincideTexto;
                })
                .collect(Collectors.toList());

        TV_Despacho.setItems(FXCollections.observableArrayList(resultados));
    }

    @FXML
    private void guardarDespacho() {
        String estado = CB_NuevoEstadoDespacho.getValue();
        Receta seleccionada = TV_Despacho.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Error", "Seleccione una receta.", Alert.AlertType.WARNING);
            return;
        }
        if (estado == null || estado.isBlank()) {
            mostrarAlerta("Error", "Seleccione un nuevo estado para la receta.", Alert.AlertType.WARNING);
            return;
        }

        guardarDespachoAsync(seleccionada, estado);
    }

    private void guardarDespachoAsync(Receta receta, String nuevoEstado) {
        progressDespacho.setVisible(true);
        btn_ModificarDespacho.setDisable(true);

        Async.run(
                () -> {
                    try {
                        receta.setEstado(nuevoEstado);
                        recetaSocketService.update(receta);
                        return recetaSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                recetasActualizadas -> {
                    progressDespacho.setVisible(false);
                    btn_ModificarDespacho.setDisable(false);

                    listaDespachoRecetas.setAll(recetasActualizadas);
                    CB_NuevoEstadoDespacho.setValue(null);
                    limpiarDespacho();
                    refrescarTablaHistorico();
                    cargarGraficosAsync();

                    mostrarAlerta("Éxito", "Receta actualizada correctamente.", Alert.AlertType.INFORMATION);
                },
                ex -> {
                    progressDespacho.setVisible(false);
                    btn_ModificarDespacho.setDisable(false);
                    mostrarAlerta("Error al actualizar el despacho", ex.getMessage(), Alert.AlertType.ERROR);
                }
        );
    }


    public void cargarDespachoAsync() {
        progressDespacho.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return recetaSocketService.findAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                recetas -> {
                    listaDespachoRecetas.setAll(recetas);
                    TV_Despacho.setItems(listaDespachoRecetas);
                    List<String> ids = recetas.stream()
                            .map(Receta::getIdentificacion)
                            .distinct()
                            .collect(Collectors.toList());
                    CB_RecetaDespacho.setItems(FXCollections.observableArrayList(ids));
                    progressDespacho.setVisible(false);
                },
                ex -> {
                    progressDespacho.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar despacho");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    @FXML
    public void abrirChat() {
        try {
            var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com.proyecto2backend/view/Chat.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.initOwner(progressHistorico.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
