package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.model.Medico;
import com.proyecto2backend.servicios.GsonProvider;

import java.lang.reflect.Type;
import java.util.List;

public class MedicoSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public Medico create(Medico medico) throws Exception {
        String json = """
        {
          "tipo":"medico",
          "op":"create",
          "data": %s
        }
        """.formatted(gson.toJson(medico));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medico.class);
    }

    public Medico update(Medico medico) throws Exception {
        String json = """
        {
          "tipo":"medico",
          "op":"update",
          "data": %s
        }
        """.formatted(gson.toJson(medico));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medico.class);
    }

    public boolean deleteById(int id) throws Exception {
        String json = """
        {
          "tipo":"medico",
          "op":"deleteById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return respuesta.contains("true");
    }

    public List<Medico> findAll() throws Exception {
        String json = """
        {
          "tipo":"medico",
          "op":"findAll"
        }
        """;

        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Medico>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Medico findById(int id) throws Exception {
        String json = """
        {
          "tipo":"medico",
          "op":"findById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medico.class);
    }

    public Medico findByIdentificacion(String identificacion) throws Exception {
        String json = """
        {
          "tipo":"medico",
          "op":"findByIdentificacion",
          "identificacion": "%s"
        }
        """.formatted(identificacion);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medico.class);
    }
}

