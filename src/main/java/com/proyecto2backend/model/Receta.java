package com.proyecto2backend.model;

import java.time.LocalDate;

public class Receta {
    private String id;                           // Id unico consecutivo de la receta
    private Paciente paciente;                   // Paciente que se le atribuye la receta
    private Medicamento medicamento;
    private int cantidad;
    private String indicaciones;
    private int diasDuracion;
    private LocalDate fechaEntrega;
    private String estado;                       // Estado de la receta

    // Constructor sin parametros
    public Receta() {}

    // Constructor con parametros
    public Receta(String id, Paciente paciente, LocalDate fechaEntrega, Medicamento medicamento, int cantidad, String indicaciones, int diasDuracion) {
        this.id = id;
        this.paciente = paciente;
        this.fechaEntrega = fechaEntrega;
        this.estado = "Confeccionada";
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.diasDuracion = diasDuracion;
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

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDiasDuracion() { return diasDuracion; }
    public void setDiasDuracion(int diasDuracion) { this.diasDuracion = diasDuracion; }

}
