package org.example.utils;
import java.sql.*;
import java.util.*;

public class ReadUtil {

    public static List<Map<String, Object>> select(
            String tabla,
            List<String> columnas,
            String condicion,
            String groupBy,
            String orderBy,
            String join
    ) throws Exception {

        String cols = (columnas == null || columnas.isEmpty()) ? "*" : String.join(",", columnas);

        StringBuilder sql = new StringBuilder("SELECT " + cols + " FROM " + tabla);

        if (join != null) sql.append(" ").append(join);
        if (condicion != null) sql.append(" WHERE ").append(condicion);
        if (groupBy != null) sql.append(" GROUP BY ").append(groupBy);
        if (orderBy != null) sql.append(" ORDER BY ").append(orderBy);

        List<Map<String, Object>> resultado = new ArrayList<>();

        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnasCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= columnasCount; i++) {
                    fila.put(meta.getColumnName(i), rs.getObject(i));
                }
                resultado.add(fila);
            }
        }
        return resultado;
    }
}
