package org.example.model;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.controller.ItemCombo;

public class Paciente {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private ItemCombo especie;
    private ItemCombo raza;
    private final ObjectProperty<LocalDate> fechaNacimiento;
    private final IntegerProperty edad;
    private final StringProperty sexo;
    private final ObjectProperty<BigDecimal> peso;
    private ItemCombo propietario;
    private final StringProperty alergias;
    private final StringProperty condicionesPreexistentes;
    private final StringProperty microchip;
    private final StringProperty fotoUrl;

    public Paciente() {
        this(0, "", new ItemCombo(0, ""), new ItemCombo(0, ""), null, 0, "", BigDecimal.ZERO, new ItemCombo(0, ""), "", "", "", "");
    }

    public Paciente(int id, String nombre, ItemCombo especie, ItemCombo raza,
                    LocalDate fechaNacimiento, int edad, String sexo, BigDecimal peso,
                    ItemCombo propietario, String alergias, String condicionesPreexistentes,
                    String microchip, String fotoUrl) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = new SimpleObjectProperty<>(fechaNacimiento);
        this.edad = new SimpleIntegerProperty(edad);
        this.sexo = new SimpleStringProperty(mapearSexo(sexo));
        this.peso = new SimpleObjectProperty<>(peso);
        this.propietario = propietario;
        this.alergias = new SimpleStringProperty(alergias);
        this.condicionesPreexistentes = new SimpleStringProperty(condicionesPreexistentes);
        this.microchip = new SimpleStringProperty(microchip);
        this.fotoUrl = new SimpleStringProperty(fotoUrl);
    }
    private String mapearSexo(String valor) {
        if (valor == null) {
            return "DESCONOCIDO";
        }
        switch (valor.toLowerCase()) {
            case "Macho":
            case "m":
                return "M";
            case "Hembra":
            case "f":
                return "F";
            default:
                return "DESCONOCIDO";
        }
    }
    // Getters y setters JavaFX
    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public ObjectProperty<LocalDate> fechaNacimientoProperty() { return fechaNacimiento; }
    public StringProperty sexoProperty() { return sexo; }
    public ObjectProperty<BigDecimal> pesoProperty() { return peso; }
    public StringProperty alergiasProperty() { return alergias; }
    public StringProperty condicionesPreexistentesProperty() { return condicionesPreexistentes; }
    public StringProperty microchipProperty() { return microchip; }
    public StringProperty fotoUrlProperty() { return fotoUrl; }

    // Métodos normales
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public ItemCombo getEspecie() { return especie; }
    public void setEspecie(ItemCombo especie) { this.especie = especie; }

    public ItemCombo getRaza() { return raza; }
    public void setRaza(ItemCombo raza) { this.raza = raza; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento.get(); }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento.set(fechaNacimiento); }

    public String getSexo() {return sexo.get();}
    public void setSexo(String valor) {this.sexo.set(mapearSexo(valor));}

    public BigDecimal getPeso() { return peso.get(); }
    public void setPeso(BigDecimal peso) { this.peso.set(peso); }

    public ItemCombo getPropietario() { return propietario; }
    public void setPropietario(ItemCombo propietario) { this.propietario = propietario; }

    public String getAlergias() { return alergias.get(); }
    public void setAlergias(String alergias) { this.alergias.set(alergias); }

    public String getCondicionesPreexistentes() { return condicionesPreexistentes.get(); }
    public void setCondicionesPreexistentes(String condicionesPreexistentes) { this.condicionesPreexistentes.set(condicionesPreexistentes); }

    public String getMicrochip() { return microchip.get(); }
    public void setMicrochip(String microchip) { this.microchip.set(microchip); }

    public String getFotoUrl() { return fotoUrl.get(); }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl.set(fotoUrl); }

    public int getEdad() {
        if (fechaNacimiento.get() != null) {
            return LocalDate.now().getYear() - fechaNacimiento.get().getYear();
        }
        return 0;
    }
}
