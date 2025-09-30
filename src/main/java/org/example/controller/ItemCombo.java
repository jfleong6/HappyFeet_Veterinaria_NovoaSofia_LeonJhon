package org.example.controller;

// Clase interna o externa
public class ItemCombo {
    private final int id;
    private final String nombre;

    public ItemCombo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre; // Esto es lo que se mostrará en el ComboBox
    }
}
