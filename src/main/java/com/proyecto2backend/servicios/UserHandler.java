package com.proyecto2backend.servicios;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler extends Thread {
    private final Socket socket;
    private final HospitalChatServer server;
    private PrintWriter out;
    private BufferedReader in;

    private String name = "anonimo";
    private String role = "usuario";

    private static final Logger LOGGER = Logger.getLogger("HospitalChatServer");

    public UserHandler(Socket socket, HospitalChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            out.println("Bienvenido al chat del hospital. De preferencia , escribe tu nombre para poder identificarte.");

            // Comienza a leer las entradas del cliente
            String line = in.readLine();
            if (line != null && !line.isBlank()) {
                name = line.trim();
                // Aqui vamos a necesitar un metodo en el que el servidor procese el mensaje
                server.broadcast("[SISTEMA] " + name + " se unio al chat");
            }

            // Procesamos el resto de los mensajes
            while ((line = in.readLine()) != null) {
                server.broadcast(name + ": " + line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Conexion finalizada: " + e);
        }
        finally {
            try {
                socket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Error al cerrar el socket: " + ex);
            }
            server.remove(this);
        }
    }
}
