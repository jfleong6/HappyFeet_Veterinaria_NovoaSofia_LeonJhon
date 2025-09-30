package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import org.example.model.Duenos;
import org.example.utils.CreateUtil;
import org.example.utils.ReadUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuenosController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtContactoEmergencia;

    @FXML private TableView<Duenos> tablaDuenos;
    @FXML private TableColumn<Duenos, Number> colId;
    @FXML private TableColumn<Duenos, String> colNombre;
    @FXML private TableColumn<Duenos, String> colDocumento;
    @FXML private TableColumn<Duenos, String> colDireccion;
    @FXML private TableColumn<Duenos, String> colTelefono;
    @FXML private TableColumn<Duenos, String> colEmail;
    @FXML private TableColumn<Duenos, Number> colMascotas;
    @FXML private TableColumn<Duenos, Void> colAcciones;

    private final ObservableList<Duenos> listaDuenos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Asociar columnas con propiedades
        colId.setCellValueFactory(data -> data.getValue().idProperty());
        colNombre.setCellValueFactory(data -> data.getValue().nombreCompletoProperty());
        colDocumento.setCellValueFactory(data -> data.getValue().documentoProperty());
        colDireccion.setCellValueFactory(data -> data.getValue().direccionProperty());
        colTelefono.setCellValueFactory(data -> data.getValue().telefonoProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colMascotas.setCellValueFactory(data -> data.getValue().cantidadMascotasProperty());

        tablaDuenos.setItems(listaDuenos);

        configurarColumnaAcciones();
        cargarDuenos();
    }

    private void cargarDuenos() {
        listaDuenos.clear();
        try {
            List<Map<String, Object>> filas = ReadUtil.select(
                    "vista_duenos_con_mascotas", // vista con cantidad de mascotas
                    null, null, null, "nombre_completo ASC", null
            );

            for (Map<String, Object> fila : filas) {
                Duenos d = new Duenos(
                        (int) fila.get("id_dueno"),
                        (String) fila.get("nombre_completo"),
                        (String) fila.get("documento_identidad"),
                        (fila.get("direccion") != null) ? fila.get("direccion").toString() : "",
                        (fila.get("telefono") != null) ? fila.get("telefono").toString() : "",
                        (fila.get("email") != null) ? fila.get("email").toString() : "",
                        (fila.get("contacto_emergencia") != null) ? fila.get("contacto_emergencia").toString() : "",
                        (fila.get("cantidad_mascotas") != null) ? ((Number) fila.get("cantidad_mascotas")).intValue() : 0
                );
                listaDuenos.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<Duenos, Void>() {
            private final Button btnVer = new Button("Ver");
            {
                btnVer.getStyleClass().add("btn-ver");  // ✅ agrega clase CSS
            }
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(5, btnVer); //, btnEliminar);

            {
                contenedor.setAlignment(Pos.CENTER);

                btnVer.setOnAction(e -> {
                    Duenos d = getTableView().getItems().get(getIndex());
                    mostrarAlerta("Detalle Dueño",
                            "Nombre: " + d.getNombreCompleto() +
                                    "\nDocumento: " + d.getDocumento() +
                                    "\nMascotas: " + d.getCantidadMascotas()
                    );
                });

                btnEliminar.setOnAction(e -> {
                    Duenos d = getTableView().getItems().get(getIndex());
                    listaDuenos.remove(d);
                    // Aquí agregar lógica para eliminar de BD si quieres
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
    private void agregarDueno() {
        try {
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre_completo", txtNombre.getText());
            datos.put("documento_identidad", txtDocumento.getText());
            datos.put("direccion", txtDireccion.getText());
            datos.put("telefono", txtTelefono.getText());
            datos.put("email", txtEmail.getText());
            datos.put("contacto_emergencia", txtContactoEmergencia.getText());

            // Intentar insertar en la base de datos
            CreateUtil.insert("duenos", datos);

            // Limpiar formulario
            limpiarFormulario();

            // Mostrar alerta de éxito
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Éxito");
            alerta.setHeaderText(null);
            alerta.setContentText("Dueño guardado correctamente.");
            alerta.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();

            // Mostrar alerta de error
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo guardar el dueño: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtDocumento.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtContactoEmergencia.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
