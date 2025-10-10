package com.proyecto2backend.model;

public class Medicamento {
    private String codigo;
    private String nombre;
    private String descripcion;

    // Constructor sin parametros
    public Medicamento() {}

    // Constructor con parametros
    public Medicamento(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

}
