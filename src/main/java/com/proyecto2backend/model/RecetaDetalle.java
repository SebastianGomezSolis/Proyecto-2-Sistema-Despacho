package com.proyecto2backend.model;

public class RecetaDetalle {
    private Medicamento medicamento;
    private int cantidad;
    private String indicaciones;
    private int diasDuracion;

    public RecetaDetalle() {}

    public RecetaDetalle(Medicamento medicamento, int cantidad, String indicaciones, int diasDuracion) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.diasDuracion = diasDuracion;
    }

    // Getters y setters
    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDiasDuracion() { return diasDuracion; }
    public void setDiasDuracion(int diasDuracion) { this.diasDuracion = diasDuracion; }

}
