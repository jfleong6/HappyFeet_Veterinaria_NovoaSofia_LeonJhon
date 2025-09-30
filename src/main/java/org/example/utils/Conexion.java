package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/happy_feet";
    private static final String USER = "root";
    private static final String PASSWORD = "jhonm320429";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar: " + e.getMessage());
            return null;
        }
    }
}
