package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.model.Farmaceuta;
import com.proyecto2backend.servicios.GsonProvider;

import java.lang.reflect.Type;
import java.util.List;

public class FarmaceutaSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public Farmaceuta create(Farmaceuta f) throws Exception {
        String json = """
        {
          "tipo":"farmaceuta",
          "op":"create",
          "data": %s
        }
        """.formatted(gson.toJson(f));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Farmaceuta.class);
    }

    public Farmaceuta update(Farmaceuta f) throws Exception {
        String json = """
        {
          "tipo":"farmaceuta",
          "op":"update",
          "data": %s
        }
        """.formatted(gson.toJson(f));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Farmaceuta.class);
    }

    public boolean deleteById(int id) throws Exception {
        String json = """
        {
          "tipo":"farmaceuta",
          "op":"deleteById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return respuesta.contains("true");
    }

    public List<Farmaceuta> findAll() throws Exception {
        String json = """
        {
          "tipo":"farmaceuta",
          "op":"findAll"
        }
        """;

        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Farmaceuta>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Farmaceuta findById(int id) throws Exception {
        String json = """
        {
          "tipo":"farmaceuta",
          "op":"findById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Farmaceuta.class);
    }

    public Farmaceuta findByIdentificacion(String identificacion) throws Exception {
        String json = """
        {
          "tipo":"farmaceuta",
          "op":"findByIdentificacion",
          "identificacion": "%s"
        }
        """.formatted(identificacion);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Farmaceuta.class);
    }
}
