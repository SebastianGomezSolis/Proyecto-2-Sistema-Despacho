package com.proyecto2backend.datos;

import com.proyecto2backend.model.Farmaceuta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDatos {
    public List<Farmaceuta> findAll() throws SQLException {
        String sql = "Select * from farmaceuta ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Farmaceuta> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Farmaceuta(
                   rs.getString("id"),
                   rs.getString("clave"),
                   rs.getString("nombre")
                ));
            }
            return list;
        }
    }

    public Farmaceuta findById(String id) throws SQLException {
        String sql = "SELECT * FROM farmaceuta WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Farmaceuta encontrado = null;
            if (rs.next()) {
                encontrado = new Farmaceuta(
                        rs.getString("id"),
                        rs.getString("clave"),
                        rs.getString("nombre")
                );
            }
            return encontrado;
        }
    }

    public Farmaceuta insert(Farmaceuta farmaceuta) throws SQLException {
        String sql = "INSERT INTO farmaceuta (id, clave, nombre) VALUES (?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, farmaceuta.getId());
            ps.setString(2, farmaceuta.getClave());
            ps.setString(3, farmaceuta.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return farmaceuta;
                } else {
                    return null;
                }
            }
        }
    }

    public Farmaceuta update(Farmaceuta farmaceuta) throws SQLException {
        String sql = "UPDATE farmaceuta SET nombre = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, farmaceuta.getClave());
            ps.setString(2, farmaceuta.getNombre());
            ps.setString(3, farmaceuta.getId());
            if (ps.executeUpdate() > 0) {
                return farmaceuta;
            } else {
                return null;
            }
        }
    }

    public int delete(String id) throws SQLException {
        String sql = "DELETE FROM farmaceuta WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
