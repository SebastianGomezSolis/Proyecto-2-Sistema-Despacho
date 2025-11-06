package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.model.Administrador;
import com.proyecto2backend.servicios.GsonProvider;

import java.lang.reflect.Type;
import java.util.List;

public class AdministradorSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public Administrador create(Administrador a) throws Exception {
        String json = """
        {
          "tipo":"administrador",
          "op":"create",
          "data": %s
        }
        """.formatted(gson.toJson(a));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Administrador.class);
    }

    public Administrador update(Administrador a) throws Exception {
        String json = """
        {
          "tipo":"administrador",
          "op":"update",
          "data": %s
        }
        """.formatted(gson.toJson(a));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Administrador.class);
    }

    public boolean delete(int id) throws Exception {
        String json = """
        {
          "tipo":"administrador",
          "op":"deleteById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return respuesta.contains("true");
    }

    public List<Administrador> findAll() throws Exception {
        String json = """
        {
          "tipo":"administrador",
          "op":"findAll"
        }
        """;

        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Administrador>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Administrador findById(int id) throws Exception {
        String json = """
        {
          "tipo":"administrador",
          "op":"findById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Administrador.class);
    }

    public Administrador findByIdentificacion(String identificacion) throws Exception {
        String json = """
        {
          "tipo":"administrador",
          "op":"findByIdentificacion",
          "identificacion": "%s"
        }
        """.formatted(identificacion);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Administrador.class);
    }
}
