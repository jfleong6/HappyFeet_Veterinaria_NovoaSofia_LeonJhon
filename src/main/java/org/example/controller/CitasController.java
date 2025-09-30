package org.example.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import org.example.dao.CitasDAO;
import org.example.model.Cita;
import org.example.controller.ItemCombo;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class CitasController {

    @FXML
    private ComboBox<ItemCombo> cbPaciente;

    @FXML
    private ComboBox<ItemCombo> cbVeterinario;

    @FXML private  Label labelSemanaActual;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private ComboBox<String> tfHora;

    @FXML
    private TextArea taMotivo;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private Button btnAgregarCita;

    @FXML
    private GridPane calendarioGrid;

    @FXML
    private Button btnSemanaAnterior, btnSemanaSiguiente;

    private CitasDAO citasDAO;
    private LocalDate inicioSemana;

    @FXML
    public void initialize() {
        citasDAO = new CitasDAO();
        dpFecha.setValue(LocalDate.now());

        cbEstado.getItems().addAll("PROGRAMADA","FINALIZADA","CANCELADA");
        for (int row = 1; row <= 12; row++) {
            String hora = (7 + row) + ":00"; // Horas de 8 a 19
            tfHora.getItems().add(hora);
        }



        cargarPacientes();
        cargarVeterinarios();

        inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
        cargarCitasSemana();

        btnAgregarCita.setOnAction(e -> agregarCita());
        btnSemanaAnterior.setOnAction(e -> cambiarSemana(-1));
        btnSemanaSiguiente.setOnAction(e -> cambiarSemana(1));
    }

    private void cargarPacientes() {
        try {
            List<Map<String, Object>> filas = citasDAO.getPacientes();
            cbPaciente.getItems().clear();
            for (Map<String, Object> fila : filas) {
                cbPaciente.getItems().add(new ItemCombo((int) fila.get("id_paciente"), fila.get("nombre").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarVeterinarios() {
        try {
            List<Map<String, Object>> filas = citasDAO.getVeterinarios();
            cbVeterinario.getItems().clear();
            for (Map<String, Object> fila : filas) {
                cbVeterinario.getItems().add(new ItemCombo((int) fila.get("id_veterinario"), fila.get("nombre_completo").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarCita() {
        try {
            Date fecha = Date.valueOf(dpFecha.getValue());
            String seleccion = tfHora.getValue(); // Ejemplo: "8:00"
            String[] partes = seleccion.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            Time hora = new Time(horas, minutos, 0);

            String motivo = taMotivo.getText();
            String estado = cbEstado.getValue();
            ItemCombo itemPaciente = cbPaciente.getValue();
            ItemCombo itemVet = cbVeterinario.getValue();

            if (itemPaciente == null || itemVet == null || estado == null) {
                mostrarAlerta("Error", "Seleccione paciente, veterinario y estado.", Alert.AlertType.ERROR);
                return;
            }

            int idPaciente = itemPaciente.getId();
            int idVeterinario = itemVet.getId();

            Cita nuevaCita = new Cita(fecha, hora, motivo, estado, idPaciente, idVeterinario);
            citasDAO.insertarCita(nuevaCita);

            mostrarAlerta("Éxito", "Cita agregada correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
            cargarCitasSemana();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo agregar la cita.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormulario() {
        cbPaciente.getSelectionModel().clearSelection();
        cbVeterinario.getSelectionModel().clearSelection();
        dpFecha.setValue(LocalDate.now());
        tfHora.getSelectionModel().clearSelection();
        taMotivo.clear();
        cbEstado.getSelectionModel().clearSelection();
    }

    private StackPane findCell(int col, int row) {
        for (Node node : calendarioGrid.getChildren()) {
            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);
            int colIdx = (c == null) ? 0 : c;
            int rowIdx = (r == null) ? 0 : r;
            if (colIdx == col && rowIdx == row && node instanceof StackPane) {
                return (StackPane) node;
            }
        }
        return null;
    }

    private void cargarCitasSemana() {
        calendarioGrid.getChildren().clear();
        calendarioGrid.getColumnConstraints().clear();
        calendarioGrid.getRowConstraints().clear();

        // Encabezados de días (columnas)
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (int col = 1; col <= dias.length; col++) {
            LocalDate fecha = inicioSemana.plusDays(col - 1);
            Label lblDia = new Label(fecha.getDayOfMonth()+" "+dias[col - 1]);
            lblDia.getStyleClass().add("dia-header");
            lblDia.setMaxWidth(Double.MAX_VALUE);
            lblDia.setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(lblDia, Priority.ALWAYS);
            GridPane.setVgrow(lblDia, Priority.ALWAYS);
            if (fecha.equals(LocalDate.now())) lblDia.getStyleClass().add("dia-actual");
            calendarioGrid.add(lblDia, col, 0);
        }

        // Encabezados de horas (filas) — 8:00 a 19:00
        for (int row = 1; row <= 12; row++) {
            Label lblHora = new Label((7 + row) + ":00");
            lblHora.getStyleClass().add("hora-header");
            lblHora.setMaxWidth(Double.MAX_VALUE);
            lblHora.setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(lblHora, Priority.ALWAYS);
            GridPane.setVgrow(lblHora, Priority.ALWAYS);
            calendarioGrid.add(lblHora, 0, row);
        }

        // Crear celdas vacías (ya con StackPane)
        for (int col = 1; col <= dias.length; col++) {
            for (int row = 1; row <= 12; row++) {
                StackPane pane = new StackPane();
                if (col % 2 == 0) pane.getStyleClass().add("celda-calendario-par");
                else pane.getStyleClass().add("celda-calendario");

                pane.setMaxWidth(Double.MAX_VALUE);
                pane.setMaxHeight(Double.MAX_VALUE);
                GridPane.setHgrow(pane, Priority.ALWAYS);
                GridPane.setVgrow(pane, Priority.ALWAYS);

                // Añadimos un contenedor VBox dentro del StackPane para apilar varias citas
                VBox container = new VBox(6);
                container.setFillWidth(true);
                container.setPadding(new Insets(4));
                container.setMaxWidth(Double.MAX_VALUE);
                container.setMaxHeight(Double.MAX_VALUE);
                StackPane.setAlignment(container, Pos.TOP_LEFT);
                pane.getChildren().add(container);

                calendarioGrid.add(pane, col, row);
            }
        }

        // Configurar columnas (1 hora + 7 días = 8 columnas)
        for (int col = 0; col <= 7; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.ALWAYS);
            colConst.setPercentWidth(100.0 / 8);
            calendarioGrid.getColumnConstraints().add(colConst);
        }

        // Configurar filas (1 encabezado + 12 horas = 13 filas)
        for (int row = 0; row <= 12; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setVgrow(Priority.ALWAYS);
            rowConst.setPercentHeight(100.0 / 13);
            calendarioGrid.getRowConstraints().add(rowConst);
        }

        // Cargar citas de la semana y colocarlas dentro del VBox de la celda correspondiente
        try {
            List<Cita> citas = citasDAO.getCitasPorSemana(inicioSemana);

            for (Cita cita : citas) {
                int dia = cita.getFecha().toLocalDate().getDayOfWeek().getValue(); // 1=Lunes ... 7=Domingo
                int hora = cita.getHora().toLocalTime().getHour();
                int fila = hora - 7; // fila 1 = 8:00
                if (fila < 1 || fila > 12) continue;

                // Construir la "tarjeta" de la cita
                Label lblPaciente = new Label("🐾 "+cita.getPacienteNombre());
                lblPaciente.getStyleClass().add("label-mascota");
                Label lblHora = new Label("🕔 "+cita.getHora().toString());
                Label lblVet = new Label("🩺 "+ cita.getVeterinarioNombre());
                lblPaciente.getStyleClass().add("cita-text-line");
                lblHora.getStyleClass().add("cita-text-line-small");
                lblVet.getStyleClass().add("cita-text-line-small");

                VBox citaBox = new VBox(2, lblPaciente, lblHora, lblVet);
                citaBox.getStyleClass().add("cita"); // usa tu clase .cita del CSS
                citaBox.setMaxWidth(Double.MAX_VALUE);
                citaBox.setPadding(new Insets(6));
                citaBox.setAlignment(Pos.CENTER_LEFT);

                // Cursor mano al pasar
                citaBox.setOnMouseEntered(ev -> citaBox.setCursor(Cursor.HAND));
                // Click simple: mostrar detalles
                citaBox.setOnMouseClicked(ev -> {
                    if (ev.getButton() == MouseButton.PRIMARY) {
                        String detalles =
                                "Paciente: " + cita.getPacienteNombre() +
                                        "\nVeterinario: " + cita.getVeterinarioNombre() +
                                        "\nHora: " + cita.getHora().toString() +
                                        "\nMotivo: " + (cita.getMotivo() != null ? cita.getMotivo() : "") +
                                        "\nEstado: " + (cita.getEstado() != null ? cita.getEstado() : "");
                        mostrarAlerta("Detalles de Cita", detalles, Alert.AlertType.INFORMATION);
                    }
                });

                // Click derecho: menú contextual (editar / eliminar) opcional
                ContextMenu menu = new ContextMenu();
                MenuItem miEditar = new MenuItem("Editar");
                MenuItem miEliminar = new MenuItem("Eliminar");
                // miEditar.setOnAction(ae -> editarCita(cita));    // implementa editarCita(...)
                // miEliminar.setOnAction(ae -> {
                //    eliminarCita(cita); // implementa eliminarCita(...)
                //    cargarCitasSemana(); // refrescar
                // });
                menu.getItems().addAll(miEditar, miEliminar);
                citaBox.setOnContextMenuRequested(ev -> menu.show(citaBox, ev.getScreenX(), ev.getScreenY()));

                // Añadir la citaBox al contenedor interno de la celda (VBox)
                StackPane cell = findCell(dia, fila);
                if (cell != null) {
                    Node first = cell.getChildren().isEmpty() ? null : cell.getChildren().get(0);
                    VBox container;
                    if (first instanceof VBox) container = (VBox) first;
                    else {
                        container = new VBox(6);
                        container.setFillWidth(true);
                        container.setPadding(new Insets(4));
                        cell.getChildren().add(container);
                    }
                    container.getChildren().add(citaBox);
                }
            }

            // Actualizar label de semana
            LocalDate finSemana = inicioSemana.plusDays(6);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            labelSemanaActual.setText("Semana del " + inicioSemana.format(formato) + " al " + finSemana.format(formato));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cambiarSemana(int delta) {
        inicioSemana = inicioSemana.plusWeeks(delta);
        cargarCitasSemana();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
