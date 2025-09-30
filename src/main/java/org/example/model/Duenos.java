package org.example.model;

import javafx.beans.property.*;

public class Duenos {

    private final IntegerProperty id;
    private final StringProperty nombreCompleto;
    private final StringProperty documento;
    private final StringProperty direccion;
    private final StringProperty telefono;
    private final StringProperty email;
    private final StringProperty contactoEmergencia;
    private final IntegerProperty cantidadMascotas;

    public Duenos() {
        this(0, "", "", "", "", "", "", 0);
    }

    public Duenos(int id, String nombreCompleto, String documento, String direccion, String telefono,
                  String email, String contactoEmergencia, int cantidadMascotas) {
        this.id = new SimpleIntegerProperty(id);
        this.nombreCompleto = new SimpleStringProperty(nombreCompleto);
        this.documento = new SimpleStringProperty(documento);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
        this.contactoEmergencia = new SimpleStringProperty(contactoEmergencia);
        this.cantidadMascotas = new SimpleIntegerProperty(cantidadMascotas);
    }

    // GETTERS Y SETTERS
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNombreCompleto() { return nombreCompleto.get(); }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto.set(nombreCompleto); }
    public StringProperty nombreCompletoProperty() { return nombreCompleto; }

    public String getDocumento() { return documento.get(); }
    public void setDocumento(String documento) { this.documento.set(documento); }
    public StringProperty documentoProperty() { return documento; }

    public String getDireccion() { return direccion.get(); }
    public void setDireccion(String direccion) { this.direccion.set(direccion); }
    public StringProperty direccionProperty() { return direccion; }

    public String getTelefono() { return telefono.get(); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }
    public StringProperty telefonoProperty() { return telefono; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getContactoEmergencia() { return contactoEmergencia.get(); }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia.set(contactoEmergencia); }
    public StringProperty contactoEmergenciaProperty() { return contactoEmergencia; }

    public int getCantidadMascotas() { return cantidadMascotas.get(); }
    public void setCantidadMascotas(int cantidadMascotas) { this.cantidadMascotas.set(cantidadMascotas); }
    public IntegerProperty cantidadMascotasProperty() { return cantidadMascotas; }
}
