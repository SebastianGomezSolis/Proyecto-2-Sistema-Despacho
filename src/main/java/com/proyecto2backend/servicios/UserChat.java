package com.proyecto2backend.servicios;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class UserChat {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Consumer<String> onMsg = s -> {};
    private Consumer<List<String>> onUsers = list -> {};

    // Nuevo: permite pasar callback de mensajes y de lista de usuarios
    public void conectar(String host, int port, String nombre, Consumer<String> onMsg, Consumer<List<String>> onUsers) throws IOException {

        this.onMsg = onMsg != null ? onMsg : this.onMsg;
        this.onUsers = onUsers != null ? onUsers : this.onUsers;

        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        // Hilo escucha
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("[USERS]")) {
                        // [USERS] a,b,c
                        String data = line.substring(7).trim();
                        if (data.startsWith("]")) data = data.substring(1).trim();
                        if (data.startsWith(":")) data = data.substring(1).trim();
                        data = data.replaceFirst("^\\s+", "");
                        List<String> lista = data.isEmpty()
                                ? List.of()
                                : Arrays.asList(data.split("\\s*,\\s*"));
                        onUsers.accept(lista);
                    } else {
                        onMsg.accept(line);
                    }
                }
            } catch (IOException e) {
                onMsg.accept("[SISTEMA] Error, desconectado del servidor: " + e.getMessage());
            }
        }, "Chat-Listener").start();

        // Enviar nombre propuesto
        out.println(nombre != null ? nombre : "");
    }

    // Compatibilidad con firma antigua (solo mensajes)
    public void conectar(String host, int port, String nombre, Consumer<String> onMsg) throws IOException {
        conectar(host, port, nombre, onMsg, null);
    }

    public void enviarMensaje(String msg) {
        if (out != null) out.println(msg);
    }

    //Atajo: envía en formato @destino mensaje
    public void enviarPrivado(String destino, String msg) {
        if (out != null) out.println("@" + destino + " " + msg);
    }

    public void cerrar() throws IOException {
        if (socket != null) socket.close();
    }
}
