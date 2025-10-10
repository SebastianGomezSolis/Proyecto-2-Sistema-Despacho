package com.proyecto2backend.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receta {
    private String id;                           // Id unico consecutivo de la receta
    private Paciente paciente;                   // Paciente que se le atribuye la receta
    private List<RecetaDetalle> medicamentos;;             // Medicamento que se receta
    private LocalDate fechaEntrega;
    private String estado;                       // Estado de la receta
    private Medico medico;                       // Médico que la prescribe

    // Constructor sin parametros
    public Receta() {
        this.medicamentos = new ArrayList<>();
    }

    // Constructor con parametros
    public Receta(String id, Paciente paciente, LocalDate fechaEntrega, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.fechaEntrega = fechaEntrega;
        this.estado = "Confeccionada";
        this.medicamentos = new ArrayList<>();
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<RecetaDetalle> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<RecetaDetalle> medicamentos) { this.medicamentos = medicamentos; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }


    // Metodo para agregar un detalle (medicamento con cantidad e indicaciones)
    public void agregarMedicamento(RecetaDetalle detalle) { this.medicamentos.add(detalle); }
}
