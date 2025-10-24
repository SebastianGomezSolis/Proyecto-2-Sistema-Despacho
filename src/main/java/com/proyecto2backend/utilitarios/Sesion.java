package com.proyecto2backend.utilitarios;

import com.proyecto2backend.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Sesion {
    private static String usuarioIdentificacion;
    private static Usuario usuarioActual;
    private static List<String> codigosPermitidos = new ArrayList<>();

    public static void iniciarSesion(Usuario usuario, List<String> codigos) {
        usuarioActual = usuario;
        usuarioIdentificacion = usuario.getIdentificacion();
        codigosPermitidos = codigos;
    }

    public static boolean puedeAccederModulo(String codigoModulo) {
        return codigosPermitidos.contains(codigoModulo);
    }

    public static String getUsuarioIdentificacion() {
        return usuarioIdentificacion;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    //Actualizar el usuario en la sesión actual
    public static void actualizarUsuario(Usuario usuarioActualizado) {
        if (usuarioActual != null && usuarioActual.getIdentificacion().equals(usuarioActualizado.getId())) {
            usuarioActual = usuarioActualizado;
        }
    }

    // Cerrar sesión
    public static void cerrarSesion() {
        usuarioActual = null;
        usuarioIdentificacion = null;
        codigosPermitidos.clear();
    }

    //  Verificar si hay sesión activa
    public static boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }
}