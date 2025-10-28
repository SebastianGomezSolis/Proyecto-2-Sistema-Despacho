package com.proyecto2backend.servicios;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatUser {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void conectar(String host, int port, String nombre, Consumer<String> onMsg) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Comenzamos a escuchar los mensajes del servidor
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    onMsg.accept(line);
                }
            } catch (IOException e) {
                onMsg.accept("[SISTEMA] Error, Desconectado del servidor: " + e.getMessage());
            }
        }).start();

        out.println(nombre);
    }

    public void enviarMensaje(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    public void cerrar() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
