package view;

import db.ClienteDAO;
import model.Cliente;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FormularioCliente {

    private VBox view;

    // Campos del formulario
    private TextField nombreField;
    private TextField documentoField;
    private TextField direccionField;
    private TextField correoField;
    private TextField telefonoField;
    private ComboBox<String> estadoCombo;
    private Button guardarButton;

    public FormularioCliente() {
        construirVista();
    }

    private void construirVista() {
        // Crear los campos
        nombreField = new TextField();
        documentoField = new TextField();
        direccionField = new TextField();
        correoField = new TextField();
        telefonoField = new TextField();

        estadoCombo = new ComboBox<>();
        estadoCombo.getItems().addAll("1", "0"); // activo = 1, inactivo = 0
        estadoCombo.setValue("1"); // valor por defecto

        guardarButton = new Button("Guardar");

        // Layout en forma de grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(new Label("Nombre completo:"), 0, 0);
        grid.add(nombreField, 1, 0);

        grid.add(new Label("Documento Identidad:"), 0, 1);
        grid.add(documentoField, 1, 1);

        grid.add(new Label("Dirección:"), 0, 2);
        grid.add(direccionField, 1, 2);

        grid.add(new Label("Correo:"), 0, 3);
        grid.add(correoField, 1, 3);

        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(telefonoField, 1, 4);

        grid.add(new Label("Estado:"), 0, 5);
        grid.add(estadoCombo, 1, 5);

        grid.add(guardarButton, 1, 6);

        view = new VBox(10, grid);
        view.setPadding(new Insets(15));

        // Acción del botón Guardar
        guardarButton.setOnAction(e -> guardarCliente());
    }

    private void guardarCliente() {
        try {
            Cliente cliente = new Cliente(
                    nombreField.getText(),
                    documentoField.getText(),
                    direccionField.getText(),
                    correoField.getText(),
                    telefonoField.getText(),
                    estadoCombo.getValue()
            );

            ClienteDAO dao = new ClienteDAO();
            dao.insertar(cliente);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente guardado correctamente");
            limpiarCampos();

        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombreField.clear();
        documentoField.clear();
        direccionField.clear();
        correoField.clear();
        telefonoField.clear();
        estadoCombo.setValue("1");
    }

    // Este método lo usa Main.java
    public VBox getView() {
        return view;
    }
}
