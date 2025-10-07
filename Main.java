import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.FormularioCliente; // 👈 importa el formulario desde view

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        FormularioCliente form = new FormularioCliente();

        Scene scene = new Scene(form.getView(), 500, 350);
        primaryStage.setTitle("Formulario Dueños - Happy Feet");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
