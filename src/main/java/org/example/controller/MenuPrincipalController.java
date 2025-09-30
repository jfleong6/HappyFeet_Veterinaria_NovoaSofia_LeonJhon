package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MenuPrincipalController {

    // 🔹 Botones principales
    @FXML private Button btnPacientes;
    @FXML private Button btnDuenos;
    @FXML private Button btnVeterinario;
    @FXML private Button btnCitas;
    @FXML private Button btnInventario;
    @FXML private Button btnFacturacionReportes;
    @FXML private Button btnEspeciales;
    @FXML private Button btnSalir;

    // 🔹 Áreas del layout
    @FXML private StackPane contentArea;   // Donde se cargan las vistas
    @FXML private VBox sideMenu;           // VBox con los botones
    @FXML private BorderPane menuContainer; // Contenedor principal del menú

    // 🔹 Dimensiones del menú
    private final double expandedWidth = 200;   // ancho expandido
    private final double collapsedWidth = 55;   // ancho reducido
    private boolean isExpanded = false;

    @FXML
    private void initialize() {
        // Asignar acciones a cada botón
        btnPacientes.setOnAction(e -> loadView("/view/pacientes.fxml"));
        btnDuenos.setOnAction(e -> loadView("/view/duenos.fxml"));
        btnVeterinario.setOnAction(e -> loadView("/view/veterinarios.fxml"));
        btnCitas.setOnAction(e -> loadView("/view/citas.fxml"));
        btnInventario.setOnAction(e -> loadView("/view/inventario.fxml"));
        btnFacturacionReportes.setOnAction(e -> loadView("/view/facturacion_reportes.fxml"));
        btnEspeciales.setOnAction(e -> loadView("/view/actividades_especiales.fxml"));

        btnSalir.setOnAction(e -> System.exit(0));

        // Configuración inicial (menú reducido)
        menuContainer.setMinWidth(collapsedWidth);
        menuContainer.setPrefWidth(collapsedWidth);
        menuContainer.setMaxWidth(collapsedWidth);

        // Animación de hover
        menuContainer.setOnMouseEntered(e -> expandMenu());
        menuContainer.setOnMouseExited(e -> collapseMenu());
    }

    /**
     * Carga un archivo FXML dentro del área central
     */
    private void loadView(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Expande el menú lateral con animación suave
     */
    private void expandMenu() {
        if (!isExpanded) {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(250),
                            new KeyValue(menuContainer.minWidthProperty(), expandedWidth),
                            new KeyValue(menuContainer.prefWidthProperty(), expandedWidth),
                            new KeyValue(menuContainer.maxWidthProperty(), expandedWidth)
                    )
            );
            timeline.play();
            isExpanded = true;
        }
    }

    /**
     * Colapsa el menú lateral con animación suave
     */
    private void collapseMenu() {
        if (isExpanded) {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(250),
                            new KeyValue(menuContainer.minWidthProperty(), collapsedWidth),
                            new KeyValue(menuContainer.prefWidthProperty(), collapsedWidth),
                            new KeyValue(menuContainer.maxWidthProperty(), collapsedWidth)
                    )
            );
            timeline.play();
            isExpanded = false;
        }
    }
}
