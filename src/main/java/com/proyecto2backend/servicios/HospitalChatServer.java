package com.proyecto2backend.servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class HospitalChatServer {
    private final int port;
    private final Set<UserHandler> users = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, UserHandler> usersByName = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(HospitalChatServer.class.getName());

    public HospitalChatServer(int port) {
        this.port = port;
        configureLogger();
    }

    public void configureLogger() {
        try {
            LOGGER.setUseParentHandlers(false);
            var fileHandler = new FileHandler("hospital-server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Error al tratar de generar la bitacora del servidor: " + e.getMessage());
        }
    }

    public void start() {
        LOGGER.info("Iniciando el servidor en el puerto: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                UserHandler userHandler = new UserHandler(socket, this);
                users.add(userHandler);
                userHandler.start();
                LOGGER.info("Conexion aceptada desde: " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            LOGGER.info("Error en el servidor: " + e);
        }
    }

    // Registra un usuario con nombre único. Devuelve el nombre asignado
    public String register(UserHandler handler, String proposed) {
        String unique = ensureUniqueName(proposed == null || proposed.isBlank() ? "anonimo" : proposed.trim());
        handler.setNombre(unique);
        usersByName.put(unique, handler);
        broadcast("[SISTEMA] " + unique + " se unió al chat");
        sendUsersList(); // Enviar la lista a todos los usuarios
        LOGGER.info("Usuario registrado: " + unique);
        return unique;
    }

    private String ensureUniqueName(String base) {
        if (!usersByName.containsKey(base)) return base;
        int i = 2;
        while (usersByName.containsKey(base + "(" + i + ")")) i++;
        return base + "(" + i + ")";
    }

    public void remove(UserHandler userHandler) {
        users.remove(userHandler);
        if (userHandler.getNombre() != null) {
            usersByName.remove(userHandler.getNombre());
            broadcast("[SISTEMA] " + userHandler.getNombre() + " salió del chat");
            sendUsersList();
        }
        LOGGER.info("Cliente eliminado de la conexion: " + userHandler.getNombre());
    }

    public void broadcast(String message) {
        synchronized (users) {
            for (UserHandler user : users) {
                user.send(message);
            }
        }
    }

    // Metodo para poder enviar un mensaje privado, retorna true si se pudo enviar
    public boolean sendPrivate(String from, String to, String message) {
        UserHandler target = usersByName.get(to);
        if (target != null) {
            target.send("[PRIVADO] " + from + ": " + message);
            // Confirmación al emisor
            UserHandler src = usersByName.get(from);
            if (src != null && src != target) {
                src.send("[PRIVADO → " + to + "] " + message);
            }
            return true;
        }
        UserHandler src = usersByName.get(from);
        if (src != null) {
            src.send("[SISTEMA] Usuario '" + to + "' no encontrado.");
        }
        return false;
    }

    // Muestra la lista de usuarios
    public void sendUsersList() {
        String line = "[USERS] " + String.join(",", usersByName.keySet());
        broadcast(line);
    }

    public static void main(String[] args) {
        new HospitalChatServer(6000).start();
    }
}
