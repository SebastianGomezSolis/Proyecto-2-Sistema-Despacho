package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.proyecto2backend.model.Receta;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.servicios.GsonProvider;

public class DashBoardSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public List<Receta> cargarRecetas() throws Exception {
        String json = """
        {
          "tipo":"dashboard",
          "op":"cargarRecetas"
        }
        """;
        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Receta>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public int totalRecetas() throws Exception {
        String json = """
        {
          "tipo":"dashboard",
          "op":"totalRecetas"
        }
        """;
        String respuesta = socket.enviar(json);
        return Integer.parseInt(respuesta);
    }

    public LinkedHashMap<String, Long> recetasPorEstado() throws Exception {
        String json = """
        {
          "tipo":"dashboard",
          "op":"recetasPorEstado"
        }
        """;
        String respuesta = socket.enviar(json);
        Type tipoMapa = new TypeToken<LinkedHashMap<String, Long>>() {}.getType();
        return gson.fromJson(respuesta, tipoMapa);
    }
}
