package org.example.dao;

import org.example.model.Cita;
import org.example.utils.CreateUtil;
import org.example.utils.ReadUtil;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitasDAO {

    public void insertarCita(Cita cita) throws Exception {
        Map<String,Object> datos = new HashMap<>();
        datos.put("fecha", cita.getFecha());
        datos.put("hora", cita.getHora());
        datos.put("motivo", cita.getMotivo());
        datos.put("estado", cita.getEstado());
        datos.put("id_paciente", cita.getIdPaciente());
        datos.put("id_veterinario", cita.getIdVeterinario());

        CreateUtil.insert("citas", datos);
    }

    public List<Cita> getCitasPorRango(Date inicio, Date fin, int idVeterinario) throws Exception {
        String cond = "c.fecha BETWEEN '" + inicio + "' AND '" + fin + "'";
        if (idVeterinario > 0) cond += " AND c.id_veterinario=" + idVeterinario;

        String join = "INNER JOIN pacientes p ON c.id_paciente=p.id_paciente " +
                "INNER JOIN veterinarios v ON c.id_veterinario=v.id_veterinario";

        List<Map<String,Object>> rows = ReadUtil.select("citas c", null, cond, null, "c.fecha ASC, c.hora ASC", join);

        List<Cita> citas = new ArrayList<>();
        for (Map<String,Object> r : rows) {
            Cita c = new Cita();
            c.setIdCita((int) r.get("id_cita"));
            c.setFecha((Date) r.get("fecha"));
            c.setHora((Time) r.get("hora"));
            c.setMotivo((String) r.get("motivo"));
            c.setEstado((String) r.get("estado"));
            c.setPacienteNombre((String) r.get("nombre"));
            c.setVeterinarioNombre((String) r.get("nombre_completo"));
            c.setIdPaciente((int) r.get("id_paciente"));
            c.setIdVeterinario((int) r.get("id_veterinario"));
            citas.add(c);
        }
        return citas;
    }

    public List<Map<String,Object>> getPacientes() throws Exception {
        return ReadUtil.select("vista_pacientes_basica", null, null, null, "nombre ASC", null);
    }

    public List<Map<String,Object>> getVeterinarios() throws Exception {
        return ReadUtil.select("vista_veterinarios_basica", null, null, null, "nombre_completo ASC", null);
    }


    public List<Cita> getCitasPorSemana(LocalDate inicioSemana) throws Exception {
        List<Cita> citas = new ArrayList<>();

        // Calcular la fecha final de la semana (7 días después)
        LocalDate finSemana = inicioSemana.plusDays(6);

        // Formatear las fechas a java.sql.Date
        Date fechaInicio = Date.valueOf(inicioSemana);
        Date fechaFin = Date.valueOf(finSemana);

        // Hacer JOIN con pacientes y veterinarios para obtener nombres
        String join = "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                "INNER JOIN veterinarios v ON c.id_veterinario = v.id_veterinario";

        // Condición para filtrar por semana
        String condicion = "c.fecha BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "'";

        List<Map<String, Object>> results = ReadUtil.select(
                "citas c",
                null,
                condicion,
                null,
                "c.fecha ASC, c.hora ASC",
                join
        );

        for (Map<String, Object> row : results) {
            int id = (int) row.get("id_cita");
            Date fecha = (Date) row.get("fecha");
            Time hora = (Time) row.get("hora");
            String motivo = (String) row.get("motivo");
            String estado = (String) row.get("estado");

            // Usando IDs directamente, sin instanciar Paciente/Veterinario
            int idPaciente = (int) row.get("id_paciente");
            int idVeterinario = (int) row.get("id_veterinario");

            String nombrePaciente = (String) row.get("nombre"); // desde la vista o join
            String nombreVet = (String) row.get("nombre_completo"); // desde la vista o join

            Cita cita = new Cita(id, fecha, hora, motivo, estado, idPaciente, idVeterinario, nombrePaciente, nombreVet);
            citas.add(cita);
        }

        return citas;
    }
}
