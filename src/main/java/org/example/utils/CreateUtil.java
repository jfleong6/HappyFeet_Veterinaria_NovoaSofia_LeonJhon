package org.example.utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class CreateUtil {

    public static void insert(String tabla, Map<String, Object> datos) throws Exception {
        StringBuilder columnas = new StringBuilder();
        StringBuilder valores = new StringBuilder();

        for (String col : datos.keySet()) {
            columnas.append(col).append(",");
            valores.append("?,");
        }

        // Quitar la última coma
        columnas.setLength(columnas.length() - 1);
        valores.setLength(valores.length() - 1);

        String sql = "INSERT INTO " + tabla + " (" + columnas + ") VALUES (" + valores + ")";

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
