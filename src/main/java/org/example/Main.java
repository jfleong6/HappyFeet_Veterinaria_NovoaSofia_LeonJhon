package org.example;
import org.example.utils.Conexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 📌 Cargar FXML
        String fxmlPath = "/view/menu_principal.fxml";
        URL fxmlUrl = getClass().getResource(fxmlPath);

        if (fxmlUrl == null) {
            System.err.println("❌ ERROR: No se encontró el archivo FXML en: " + fxmlPath);
            throw new RuntimeException("No se encontró el archivo FXML: " + fxmlPath);
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root, 800, 400);
        primaryStage.setMaximized(true);

        primaryStage.setTitle("Happy Feet 🐾");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
