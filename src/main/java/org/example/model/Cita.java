package org.example.model;

import java.sql.Date;
import java.sql.Time;

public class Cita {

    private int idCita;
    private Date fecha;
    private Time hora;
    private String motivo;
    private String estado;
    private int idPaciente;
    private int idVeterinario;
    private String pacienteNombre;
    private String veterinarioNombre;

    public Cita() {}

    public Cita(int idCita, Date fecha, Time hora, String motivo, String estado,
                int idPaciente, int idVeterinario, String pacienteNombre, String veterinarioNombre) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
        this.idPaciente = idPaciente;
        this.idVeterinario = idVeterinario;
        this.pacienteNombre = pacienteNombre;
        this.veterinarioNombre = veterinarioNombre;
    }

    public Cita(Date fecha, Time hora, String motivo, String estado, int idPaciente, int idVeterinario) {
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
        this.idPaciente = idPaciente;
        this.idVeterinario = idVeterinario;
    }

    // Getters y setters
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Time getHora() { return hora; }
    public void setHora(Time hora) { this.hora = hora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getVeterinarioNombre() { return veterinarioNombre; }
    public void setVeterinarioNombre(String veterinarioNombre) { this.veterinarioNombre = veterinarioNombre; }
}
