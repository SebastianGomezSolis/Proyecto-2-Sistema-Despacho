package com.proyecto2backend.model;

import java.time.LocalDate;

public class Paciente {
    private String id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;

    // Constructor sin parametros
    public Paciente() {}

    // Constructor con parametros
    public Paciente(String id, String nombre, LocalDate fechaNacimiento, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

}
