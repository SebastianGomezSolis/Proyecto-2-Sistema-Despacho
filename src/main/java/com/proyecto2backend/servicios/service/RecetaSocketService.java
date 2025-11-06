package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.model.Receta;
import com.proyecto2backend.servicios.GsonProvider;

import java.lang.reflect.Type;
import java.util.List;

public class RecetaSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public Receta create(Receta r) throws Exception {
        String json = """
        {
          "tipo":"receta",
          "op":"create",
          "data": %s
        }
        """.formatted(gson.toJson(r));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Receta.class);
    }

    public Receta update(Receta r) throws Exception {
        String json = """
        {
          "tipo":"receta",
          "op":"update",
          "data": %s
        }
        """.formatted(gson.toJson(r));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Receta.class);
    }

    public boolean deleteById(int id) throws Exception {
        String json = """
        {
          "tipo":"receta",
          "op":"deleteById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return respuesta.contains("true");
    }

    public List<Receta> findAll() throws Exception {
        String json = """
        {
          "tipo":"receta",
          "op":"findAll"
        }
        """;

        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Receta>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Receta findById(int id) throws Exception {
        String json = """
        {
          "tipo":"receta",
          "op":"findById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Receta.class);
    }
}
