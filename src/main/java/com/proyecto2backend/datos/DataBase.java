package com.proyecto2backend.datos;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataBase {
    private static HikariDataSource ds;

    private DataBase() {}

    // Aqui vamos a configurar la conexion de la aplicacion a la base de datos
    static {
        try (InputStream in = DataBase.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            prop.load(in);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(prop.getProperty("db.url"));
            config.setUsername(prop.getProperty("db.user"));
            config.setPassword(prop.getProperty("db.password"));

            config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("db.pool.size")));
            config.setPoolName("hotelPool");

            // Configuracion opcional pero recomendada por el profe
            config.setMinimumIdle(2);
            config.setConnectionTimeout(10000);
            config.setMaxLifetime(1800000);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo iniciar el pool de conexiones", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
