package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.model.Veterinario;
import org.example.utils.CreateUtil;
import org.example.utils.ReadUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VeterinariosController {

    // Formulario
    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEspecialidad;

    // Tabla
    @FXML private TableView<Veterinario> tablaVeterinarios;
    @FXML private TableColumn<Veterinario, Number> colId;
    @FXML private TableColumn<Veterinario, String> colNombre;
    @FXML private TableColumn<Veterinario, String> colDocumento;
    @FXML private TableColumn<Veterinario, String> colTelefono;
    @FXML private TableColumn<Veterinario, String> colEmail;
    @FXML private TableColumn<Veterinario, String> colEspecialidad;
    @FXML private TableColumn<Veterinario, String> colEstado;

    private final ObservableList<Veterinario> listaVeterinarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar tabla
        colId.setCellValueFactory(data -> data.getValue().idProperty());
        colNombre.setCellValueFactory(data -> data.getValue().nombreCompletoProperty());
        colDocumento.setCellValueFactory(data -> data.getValue().documentoProperty());
        colTelefono.setCellValueFactory(data -> data.getValue().telefonoProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colEspecialidad.setCellValueFactory(data -> data.getValue().especialidadProperty());

        tablaVeterinarios.setItems(listaVeterinarios);


        cargarVeterinarios();
    }

    private void cargarVeterinarios() {
        listaVeterinarios.clear();
        try {
            List<Map<String, Object>> filas = ReadUtil.select("veterinarios", null, null, null, "nombre_completo ASC", null);

            for (Map<String, Object> fila : filas) {
                Veterinario v = new Veterinario(
                        (int) fila.get("id_veterinario"),
                        (String) fila.get("nombre_completo"),
                        (String) fila.get("documento_identidad"),
                        (fila.get("telefono") != null) ? fila.get("telefono").toString() : "",
                        (fila.get("email") != null) ? fila.get("email").toString() : "",
                        (fila.get("especialidad") != null) ? fila.get("especialidad").toString() : "",
                        (fila.get("estado") != null) ? fila.get("estado").toString() : "ACTIVO"
                );
                listaVeterinarios.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarVeterinario() {
        try {
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre_completo", txtNombreCompleto.getText());
            datos.put("documento_identidad", txtDocumento.getText());
            datos.put("telefono", txtTelefono.getText());
            datos.put("email", txtEmail.getText());
            datos.put("especialidad", txtEspecialidad.getText());

            CreateUtil.insert("veterinarios", datos);

            limpiarFormulario();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Éxito");
            alerta.setHeaderText(null);
            alerta.setContentText("Veterinario guardado correctamente.");
            alerta.showAndWait();

            cargarVeterinarios();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo guardar el veterinario: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombreCompleto.clear();
        txtDocumento.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtEspecialidad.clear();
    }
}
