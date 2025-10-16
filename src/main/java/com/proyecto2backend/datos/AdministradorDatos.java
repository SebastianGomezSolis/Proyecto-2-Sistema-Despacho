package com.proyecto2backend.datos;

import com.proyecto2backend.model.Administrador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDatos {
    public List<Administrador> findAll() throws SQLException {
        String sql = "SELECT * FROM administrador ORDER BY id";

        try (Connection con = DataBase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Administrador> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Administrador(
                        rs.getString("id"),
                        rs.getString("clave")
                ));
            }
            return list;
        }
    }

    public Administrador findById(String id) throws SQLException {
        String sql = "SELECT * FROM administrador WHERE id = " + id;

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Administrador encontrado = null;
            if (rs.next()) {
                encontrado = new Administrador(
                        rs.getString("id"),
                        rs.getString("clave")
                );
            }
            return encontrado;
        }
    }

    public Administrador insert(Administrador administrador) throws SQLException {
        String sql = "INSERT INTO administrador (id, clave) VALUES (?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, administrador.getId());
            ps.setString(2, administrador.getClave());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return administrador;
                }
            }
            return null;
        }
    }

    public Administrador update(Administrador administrador) throws SQLException {
        String sql = "UPDATE administrador SET clave = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, administrador.getClave());
            ps.setString(2, administrador.getId());
            if (ps.executeUpdate() > 0) {
                return administrador;
            } else {
                return null;
            }
        }
    }

    public int delete(String id) throws SQLException {
        String sql = "DELETE FROM administrador WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
