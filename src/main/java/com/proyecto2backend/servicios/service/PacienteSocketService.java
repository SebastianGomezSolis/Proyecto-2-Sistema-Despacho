package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.model.Paciente;
import com.proyecto2backend.servicios.GsonProvider;

import java.lang.reflect.Type;
import java.util.List;

public class PacienteSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public Paciente create(Paciente p) throws Exception {
        String json = """
        {
          "tipo":"paciente",
          "op":"create",
          "data": %s
        }
        """.formatted(gson.toJson(p));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Paciente.class);
    }

    public Paciente update(Paciente p) throws Exception {
        String json = """
        {
          "tipo":"paciente",
          "op":"update",
          "data": %s
        }
        """.formatted(gson.toJson(p));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Paciente.class);
    }

    public boolean deleteById(int id) throws Exception {
        String json = """
        {
          "tipo":"paciente",
          "op":"deleteById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return respuesta.contains("true");
    }

    public List<Paciente> findAll() throws Exception {
        String json = """
        {
          "tipo":"paciente",
          "op":"findAll"
        }
        """;

        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Paciente>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Paciente findById(int id) throws Exception {
        String json = """
        {
          "tipo":"paciente",
          "op":"findById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Paciente.class);
    }

    public Paciente findByIdentificacion(String identificacion) throws Exception {
        String json = """
        {
          "tipo":"paciente",
          "op":"findByIdentificacion",
          "identificacion": "%s"
        }
        """.formatted(identificacion);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Paciente.class);
    }
}
