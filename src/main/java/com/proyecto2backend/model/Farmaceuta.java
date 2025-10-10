package com.proyecto2backend.model;

public class Farmaceuta  extends Usuario {
    private String nombre;

    // Constructor con parametros
    public Farmaceuta(String id, String clave, String nombre) {
        super(id, clave);
        this.nombre = nombre;
    }

    // Getters y Setters
    @Override public String getId() { return id; }
    @Override public String getClave() { return clave; }
    public String getNombre() { return nombre; }

    @Override public void setId(String id) { this.id = id; }
    @Override public void setClave(String clave) { this.clave = clave; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override public String getTipoUsuario() { return "Farmaceuta"; }

}
