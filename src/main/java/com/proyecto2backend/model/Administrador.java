package com.proyecto2backend.model;

public class Administrador extends Usuario {

    // Constructor con parametros
    public Administrador(String id, String clave) {
        super(id, clave);
    }

    // Getters y Setters
    @Override public String getId() { return id; }
    @Override public String getClave() { return clave; }

    @Override public void setId(String id) { this.id = id; }
    @Override public void setClave(String clave) { this.clave = clave; }

    @Override public String getTipoUsuario() { return "Administrador"; }

}
