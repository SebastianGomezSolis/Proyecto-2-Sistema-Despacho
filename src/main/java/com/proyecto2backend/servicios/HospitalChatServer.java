package com.proyecto2backend.servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.*;

public class HospitalChatServer {
    private final int port;
    private final Set<UserHandler> users = Collections.synchronizedSet(new HashSet<>());
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
                LOGGER.info("Conexion aceptado desde: " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            LOGGER.info("Error en el servidor: " + e);
        }
    }

    public void remove(UserHandler userHandler) {
        users.remove(userHandler);
        LOGGER.info("Cliente eliminado de la conexion: " + userHandler.getName());
    }

    public static void main(String[] args) {
        new HospitalChatServer(6000).start();
    }

    public void broadcast(String message) {
        synchronized (users) {
            for (UserHandler user : users) {
                user.send(message);
            }
        }
    }
}
