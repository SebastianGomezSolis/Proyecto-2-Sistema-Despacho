package com.proyecto2backend.datos;

import com.proyecto2backend.model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDatos {
    public List<Medico> findAll() throws SQLException {
        String sql = "Select * from medico ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Medico> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Medico(
                        rs.getString("id"),
                        rs.getString("clave"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                ));
            }
            return list;
        }
    }

    public Medico findById(String id) throws SQLException {
        String sql = "Select * from medico where id = " + id;

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Medico encontrado = null;
            while (rs.next()) {
                encontrado = new Medico(
                        rs.getString("id"),
                        rs.getString("clave"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                );
            }
            return encontrado;
        }
    }

    public Medico insert(Medico medico) throws SQLException {
        String sql = "INSERT INTO medico (id, clave, nombre, especialidad) VALUES (?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medico.getId());
            ps.setString(2, medico.getClave());
            ps.setString(3, medico.getNombre());
            ps.setString(4, medico.getEspecialidad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return medico;
                }
            }
            return null;
        }
    }

    public Medico update(Medico medico) throws SQLException {
        String sql = "UPDATE medico set nombre = ?, especialidad = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1,"clave");
            ps.setString(2, medico.getNombre());
            ps.setString(3, medico.getEspecialidad());
            ps.setString(4, medico.getId());
            if (ps.executeUpdate() > 0) {
                return medico;
            } else {
                return null;
            }
        }
    }

    public int delete(String id) throws SQLException {
        String sql = "DELETE FROM medico WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
