package com.proyecto2backend.servicios;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SesionManagerService {
    private static final Set<String> usuariosActivos = Collections.synchronizedSet(new HashSet<>());

    // Llamar cuando un usuario hace login correctamente
    public static synchronized boolean registrarSesion(String codigoUsuario) {
        if (usuariosActivos.contains(codigoUsuario)) {
            return false; // ya está logueado
        }
        usuariosActivos.add(codigoUsuario);
        return true;
    }

    // Llamar cuando el usuario cierra sesión o sale
    public static synchronized void cerrarSesion(String codigoUsuario) {
        usuariosActivos.remove(codigoUsuario);
    }

    // Por si quieres verificar desde otras partes
    public static synchronized boolean estaLogueado(String codigoUsuario) {
        return usuariosActivos.contains(codigoUsuario);
    }
}
