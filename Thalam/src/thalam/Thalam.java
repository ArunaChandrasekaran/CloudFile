package thalam;

import Onboarding.OnboardingDao;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Thalam extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlPath = "/Onboarding/Onboarding.fxml";
        try {
            if (new OnboardingDao().hasOnboarding()) {
                fxmlPath = "/Login/Login.fxml";
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Startup");
            alert.setHeaderText("Could not check onboarding status");
            alert.setContentText(ex.getMessage() == null
                    ? "Ensure the OnboardingDetails table exists."
                    : ex.getMessage());
            alert.showAndWait();
            // Still open onboarding so first-run setup can proceed after table is created.
        }

        URL startFxml = getClass().getResource(fxmlPath);
        if (startFxml == null) {
            startFxml = Thread.currentThread().getContextClassLoader().getResource(
                    fxmlPath.startsWith("/") ? fxmlPath.substring(1) : fxmlPath);
        }
        if (startFxml == null) {
            throw new IOException("FXML not found: " + fxmlPath
                    + " (Clean and Build the project, then Run Project — not Run File)");
        }
        Parent root = FXMLLoader.load(startFxml);

        stage.setTitle("construction management platform");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    public static void open(Node source, String fxmlPath) throws IOException {
        URL url = Thalam.class.getResource(fxmlPath);
        if (url == null) {
            throw new IOException("FXML not found: " + fxmlPath);
        }
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = stage.getScene();

        if (scene == null) {
            scene = new Scene(root);
            stage.setScene(scene);
        } else {
            // Swap content without recreating the scene (avoids maximize flash)
            scene.setRoot(root);
        }

        // Let the root fill the scene; do NOT bind pref size to Infinity-style
        // layout (that collapses ScrollPane / RHS content on Dashboard).
        if (root instanceof Region) {
            Region region = (Region) root;
            region.setMinSize(0, 0);
            region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
