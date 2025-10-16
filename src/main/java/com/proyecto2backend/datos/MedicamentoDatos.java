package com.proyecto2backend.datos;

import com.proyecto2backend.model.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDatos {
    public List<Medicamento> findAll() throws SQLException {
        String sql = "Select * from medicamento ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Medicamento> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Medicamento(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
            return list;
        }
    }

    public Medicamento findById(String id) throws SQLException {
        String sql = "SELECT * FROM medicamento WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Medicamento encontrado = null;
            if (rs.next()) {
                encontrado = new Medicamento(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }
            return encontrado;
        }
    }

    public Medicamento insert(Medicamento medicamento) throws SQLException {
        String sql = "INSERT INTO medicamento (codigo, nombre, descripcion) VALUES (?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medicamento.getCodigo());
            ps.setString(2, medicamento.getNombre());
            ps.setString(3, medicamento.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return medicamento;
                }
            }
            return null;
        }
    }

    public Medicamento update(Medicamento medicamento) throws SQLException {
        String sql = "UPDATE medicamento set nombre = ?, descripcion = ? WHERE codigo = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, medicamento.getNombre());
            ps.setString(2, medicamento.getDescripcion());
            ps.setString(3, medicamento.getCodigo());
            if (ps.executeUpdate() > 0) {
                return medicamento;
            } else {
                return null;
            }
        }
    }

    public int delete(String codigo) throws SQLException {
        String sql = "DELETE FROM medicamento WHERE codigo = " + codigo;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }

}
