package org.example.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class UpdateUtil {

    public static void update(String tabla, Map<String, Object> datos, String condicion) throws Exception {
        StringBuilder set = new StringBuilder();

        for (String col : datos.keySet()) {
            set.append(col).append(" = ?,");
        }
        set.setLength(set.length() - 1); // quitar última coma

        String sql = "UPDATE " + tabla + " SET " + set + " WHERE " + condicion;

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            for (Object val : datos.values()) {
                ps.setObject(i++, val);
            }
            ps.executeUpdate();
        }
    }
}