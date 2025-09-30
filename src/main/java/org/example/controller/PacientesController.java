package org.example.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import javafx.util.StringConverter;
import org.example.model.Paciente;
import org.example.utils.CreateUtil;
import org.example.utils.ReadUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PacientesController {

    // Campos del formulario
    @FXML private TextField txtNombre;
    @FXML private ComboBox<ItemCombo> cbEspecie;
    @FXML private ComboBox<ItemCombo> cbRaza;
    @FXML private DatePicker dpfetchNacimiento;
    @FXML private ComboBox<String> cbSexo;
    @FXML private TextField txtPeso;
    @FXML private TextField txtAlergias;
    @FXML private TextField txtCondiciones;
    @FXML private TextField txtMicrochip;
    @FXML private ComboBox<ItemCombo> cbPropietario;

    // Barra de búsqueda
    @FXML private TextField txtBuscarPropietario;

    // Tabla y columnas
    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, Number> colId;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, String> colEspecie;
    @FXML private TableColumn<Paciente, String> colRaza;
    @FXML private TableColumn<Paciente, LocalDate> colNacimiento;
    @FXML private TableColumn<Paciente, Number> colEdad;
    @FXML private TableColumn<Paciente, String> colSexo;
    @FXML private TableColumn<Paciente, Number> colPeso;
    @FXML private TableColumn<Paciente, String> colDueno;
    @FXML private TableColumn<Paciente, Void> colAcciones;

    // Lista observable de pacientes
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {// Formato AAAA-MM-DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dpfetchNacimiento.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty())
                        ? LocalDate.parse(string, formatter)
                        : null;
            }
        });
        // Asociar columnas con propiedades
        colId.setCellValueFactory(data -> data.getValue().idProperty());
        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colEspecie.setCellValueFactory(data -> data.getValue().getEspecie() != null ? new SimpleStringProperty(data.getValue().getEspecie().getNombre()) : new SimpleStringProperty(""));
        colRaza.setCellValueFactory(data -> data.getValue().getRaza() != null ? new SimpleStringProperty(data.getValue().getRaza().getNombre()) : new SimpleStringProperty(""));
        colNacimiento.setCellValueFactory(data -> data.getValue().fechaNacimientoProperty());
        colEdad.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEdad()));
        colSexo.setCellValueFactory(data -> data.getValue().sexoProperty());
        colPeso.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPeso().doubleValue()));
        colDueno.setCellValueFactory(data -> data.getValue().getPropietario() != null ? new SimpleStringProperty(data.getValue().getPropietario().getNombre()) : new SimpleStringProperty(""));

        tablaPacientes.setItems(listaPacientes);

        configurarColumnaAcciones();
        inicializarCombos();
        inicializarDueno();
        cargarPacientes();
    }

    private void cargarPacientes() {
        listaPacientes.clear();
        try {
            List<Map<String, Object>> filas = ReadUtil.select(
                    "vista_pacientes_completa",
                    null,
                    null,
                    null,
                    "nombre ASC",
                    null
            );

            for (Map<String, Object> fila : filas) {
                Paciente p = new Paciente(
                        (int) fila.get("id_paciente"),
                        (String) fila.get("nombre"),
                        new ItemCombo((int) fila.get("id_especie"), (String) fila.get("especie")),
                        new ItemCombo((int) fila.get("id_raza"), (String) fila.get("raza")),
                        (fila.get("fecha_nacimiento") != null) ? ((java.sql.Date) fila.get("fecha_nacimiento")).toLocalDate() : null,
                        (fila.get("edad") != null) ? ((Number) fila.get("edad")).intValue() : 0,
                        (fila.get("sexo") != null) ? fila.get("sexo").toString() : "",
                        (fila.get("peso") != null) ? new BigDecimal(fila.get("peso").toString()) : BigDecimal.ZERO,
                        new ItemCombo((int) fila.get("id_dueno"), (String) fila.get("propietario")),
                        (fila.get("alergias") != null) ? fila.get("alergias").toString() : "",
                        (fila.get("condiciones_preexistentes") != null) ? fila.get("condiciones_preexistentes").toString() : "",
                        (fila.get("microchip") != null) ? fila.get("microchip").toString() : "",
                        (fila.get("foto_url") != null) ? fila.get("foto_url").toString() : ""
                );

                listaPacientes.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarCombos() {
        // --- Cargar sexo ---
        cbSexo.getItems().addAll("Macho", "Hembra");
        // --- Cargar especies ---
        List<Map<String, Object>> filasEspecies;
        try {
            filasEspecies = ReadUtil.select(
                    "vista_especies_razas",
                    Arrays.asList("DISTINCT id_especie", "especie"),
                    null,
                    null,
                    "especie ASC",
                    null
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        cbEspecie.getItems().clear();
        for (Map<String, Object> fila : filasEspecies) {
            cbEspecie.getItems().add(new ItemCombo((int) fila.get("id_especie"), fila.get("especie").toString()));
        }

        cbEspecie.setOnAction(e -> {
            cbRaza.getItems().clear();
            ItemCombo especieSeleccionada = cbEspecie.getSelectionModel().getSelectedItem();
            if (especieSeleccionada != null) {
                List<Map<String, Object>> filasRazas;
                try {
                    filasRazas = ReadUtil.select(
                            "razas",
                            Arrays.asList("id_raza", "nombre"),
                            "id_especie=" + especieSeleccionada.getId(),
                            null,
                            "nombre ASC",
                            null
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                for (Map<String, Object> fila : filasRazas) {
                    cbRaza.getItems().add(new ItemCombo((int) fila.get("id_raza"), fila.get("nombre").toString()));
                }
            }
        });
    }

    private void inicializarDueno() {
        List<Map<String, Object>> filas;
        try {
            filas = ReadUtil.select("vista_duenos_basica", null, null, null, null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        cbPropietario.getItems().clear();
        for (Map<String, Object> fila : filas) {
            cbPropietario.getItems().add(new ItemCombo((int) fila.get("id_dueno"), fila.get("nombre_completo").toString()));
        }
    }

    @FXML
    private void buscarPorPropietario() {
        String filtro = txtBuscarPropietario.getText().toLowerCase();
        if (filtro.isEmpty()) {
            tablaPacientes.setItems(listaPacientes);
        } else {
            ObservableList<Paciente> filtrados = FXCollections.observableArrayList();
            for (Paciente p : listaPacientes) {
                if (p.getPropietario() != null && p.getPropietario().getNombre().toLowerCase().contains(filtro)) {
                    filtrados.add(p);
                }
            }
            tablaPacientes.setItems(filtrados);
        }
    }

    @FXML
    private void limpiarFiltro() {
        txtBuscarPropietario.clear();
        tablaPacientes.setItems(listaPacientes);
    }

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<Paciente, Void>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(5, btnVer);//, btnEditar, btnEliminar);

            {
                contenedor.setAlignment(Pos.CENTER);

                btnVer.setOnAction(e -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());

                    String detalles =
                            "🐾 Mascota: " + paciente.getNombre() +
                                    "\nEspecie: " + paciente.getEspecie() +
                                    "\nRaza: " + paciente.getRaza() +
                                    "\nEdad: " + paciente.getEdad() +
                                    "\nSexo: " + (paciente.getSexo() != null ? paciente.getSexo() : "Desconocido") +
                                    "\nFecha Nacimiento: " + (paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().toString() : "No registrada") +
                                    "\nDueño: " + (paciente.getPropietario() != null ? paciente.getPropietario().getNombre() : "No registrado");

                    mostrarAlerta("Detalles del Paciente", detalles);
                });


                btnEditar.setOnAction(e -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    txtNombre.setText(paciente.getNombre());
                    cbEspecie.getSelectionModel().select(paciente.getEspecie());
                    cbRaza.getSelectionModel().select(paciente.getRaza());
                    cbPropietario.getSelectionModel().select(paciente.getPropietario());
                    // txtEdad.setText(String.valueOf(paciente.getEdad()));
                    listaPacientes.remove(paciente);
                });

                btnEliminar.setOnAction(e -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    listaPacientes.remove(paciente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        cbEspecie.getSelectionModel().clearSelection();
        cbRaza.getItems().clear();
        cbRaza.getSelectionModel().clearSelection();
        cbPropietario.getSelectionModel().clearSelection();
        cbSexo.getSelectionModel().clearSelection();
        dpfetchNacimiento.setValue(null);
        txtPeso.clear();
        txtAlergias.clear();
        txtCondiciones.clear();
        txtMicrochip.clear();
        // txtEdad.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    public void agregarPaciente(ActionEvent actionEvent) {
        try {
            Paciente paciente = new Paciente();
            paciente.setNombre(txtNombre.getText());
            paciente.setEspecie(cbEspecie.getValue());
            paciente.setRaza(cbRaza.getValue());
            paciente.setFechaNacimiento(dpfetchNacimiento.getValue());
            paciente.setSexo(cbSexo.getSelectionModel().getSelectedItem() != null
                    ? cbSexo.getSelectionModel().getSelectedItem()
                    : "DESCONOCIDO");
            paciente.setPeso(txtPeso.getText() != null && !txtPeso.getText().isEmpty()
                    ? new BigDecimal(txtPeso.getText())
                    : BigDecimal.ZERO);
            paciente.setAlergias(txtAlergias.getText());
            paciente.setCondicionesPreexistentes(txtCondiciones.getText());
            paciente.setMicrochip(txtMicrochip.getText());
            paciente.setPropietario(cbPropietario.getValue());



            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", paciente.getNombre());
            datos.put("id_raza", paciente.getRaza() != null ? paciente.getRaza().getId() : null);
            datos.put("fecha_nacimiento", paciente.getFechaNacimiento());
            datos.put("sexo", paciente.getSexo());
            datos.put("alergias", paciente.getAlergias());
            datos.put("condiciones_preexistentes", paciente.getCondicionesPreexistentes());
            datos.put("peso", paciente.getPeso());
            datos.put("microchip", paciente.getMicrochip());
            datos.put("id_dueno", paciente.getPropietario() != null ? paciente.getPropietario().getId() : null);

            try {
                CreateUtil.insert("pacientes", datos);
                listaPacientes.add(paciente);
                mostrarAlerta("Éxito", "Paciente agregado correctamente.");
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo guardar en la base de datos.\n" + e.getMessage());
                return;
            }


            limpiarFormulario();
            System.out.println("Paciente agregado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo agregar el paciente: " + e.getMessage());
        }
    }
}
