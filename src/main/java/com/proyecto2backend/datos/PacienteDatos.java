package com.proyecto2backend.datos;

import com.proyecto2backend.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDatos {
    public List<Paciente> findAll() throws SQLException {
        String sql = "Select * from paciente ORDER BY id";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Paciente> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Paciente(
                   rs.getString("id"),
                   rs.getString("nombre"),
                   rs.getDate("fechaNacimiento").toLocalDate(),
                   rs.getString("telefono")
                ));
            }
            return list;
        }
    }

    public Paciente findById(String id) throws SQLException {
        String sql = "Select * from paciente where id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Paciente encontrado = null;
            if (rs.next()) {
                encontrado = new Paciente(
                   rs.getString("id"),
                   rs.getString("nombre"),
                   rs.getDate("fechaNacimiento").toLocalDate(),
                   rs.getString("telefono")
                );
            }
            return encontrado;
        }
    }

    public Paciente insert(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (id, nombre, fechaNacimiento, telefono) VALUES (?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paciente.getId());
            ps.setString(2, paciente.getNombre());
            ps.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setString(4, paciente.getTelefono());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return paciente;
                }
            }
            return null;
        }
    }

    public Paciente update(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nombre = ?, fechaNacimiento = ?, telefono = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, paciente.getNombre());
            ps.setDate(2, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setString(3, paciente.getTelefono());
            ps.setString(4, paciente.getId());
            if (ps.executeUpdate() > 0) {
                return paciente;
            } else {
                return null;
            }
        }
    }

    public int delete(String id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
