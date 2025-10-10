package com.proyecto2backend.model;

public abstract class Usuario {
    protected String id;
    protected String clave;

    protected Usuario(String id, String clave) {
        this.id = id;
        this.clave = clave;
    }

    public abstract String getId();
    public abstract String getClave();

    public abstract void setId(String id);
    public abstract void setClave(String clave);

    public abstract String getTipoUsuario();
}
