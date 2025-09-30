package org.example.model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class Veterinario {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombreCompleto = new SimpleStringProperty();
    private final StringProperty documento = new SimpleStringProperty();
    private final StringProperty telefono = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty especialidad = new SimpleStringProperty();


    public Veterinario() {}

    public Veterinario(int id, String nombreCompleto, String documento,
                       String telefono, String email, String especialidad, String estado) {
        this.id.set(id);
        this.nombreCompleto.set(nombreCompleto);
        this.documento.set(documento);
        this.telefono.set(telefono);
        this.email.set(email);
        this.especialidad.set(especialidad);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreCompletoProperty() { return nombreCompleto; }
    public StringProperty documentoProperty() { return documento; }
    public StringProperty telefonoProperty() { return telefono; }
    public StringProperty emailProperty() { return email; }
    public StringProperty especialidadProperty() { return especialidad; }

    public int getId() { return id.get(); }
    public String getNombreCompleto() { return nombreCompleto.get(); }
    public String getDocumento() { return documento.get(); }
    public String getTelefono() { return telefono.get(); }
    public String getEmail() { return email.get(); }
    public String getEspecialidad() { return especialidad.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto.set(nombreCompleto); }
    public void setDocumento(String documento) { this.documento.set(documento); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }
    public void setEmail(String email) { this.email.set(email); }
    public void setEspecialidad(String especialidad) { this.especialidad.set(especialidad); }

}
