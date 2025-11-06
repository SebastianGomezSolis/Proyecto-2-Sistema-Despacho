package com.proyecto2backend.servicios.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proyecto2backend.model.Medicamento;
import com.proyecto2backend.servicios.GsonProvider;

import java.lang.reflect.Type;
import java.util.List;

public class MedicamentoSocketService {
    private final SocketService socket = new SocketService();
    private final Gson gson = GsonProvider.get();

    public Medicamento create(Medicamento m) throws Exception {
        String json = """
        {
          "tipo":"medicamento",
          "op":"create",
          "data": %s
        }
        """.formatted(gson.toJson(m));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medicamento.class);
    }

    public Medicamento update(Medicamento m) throws Exception {
        String json = """
        {
          "tipo":"medicamento",
          "op":"update",
          "data": %s
        }
        """.formatted(gson.toJson(m));

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medicamento.class);
    }

    public boolean deleteById(int id) throws Exception {
        String json = """
        {
          "tipo":"medicamento",
          "op":"deleteById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return respuesta.contains("true");
    }

    public List<Medicamento> findAll() throws Exception {
        String json = """
        {
          "tipo":"medicamento",
          "op":"findAll"
        }
        """;

        String respuesta = socket.enviar(json);
        Type tipoLista = new TypeToken<List<Medicamento>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Medicamento findById(int id) throws Exception {
        String json = """
        {
          "tipo":"medicamento",
          "op":"findById",
          "id": %d
        }
        """.formatted(id);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medicamento.class);
    }

    public Medicamento findByCodigo(String codigo) throws Exception {
        String json = """
        {
          "tipo":"medicamento",
          "op":"findByCodigo",
          "codigo": "%s"
        }
        """.formatted(codigo);

        String respuesta = socket.enviar(json);
        return gson.fromJson(respuesta, Medicamento.class);
    }
}

