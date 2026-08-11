package Login;

import Onboarding.OnboardingDao;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import thalam.Thalam;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void onLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing details",
                    "Enter username and password.");
            return;
        }

        try {
            boolean ok = new OnboardingDao().validateLogin(username, password);
            if (!ok) {
                showAlert(Alert.AlertType.ERROR, "Login failed",
                        "Invalid username or password.");
                return;
            }
            Thalam.open((Node) event.getSource(), "/Dashboard/Dashboard.fxml");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Login failed",
                    "Could not sign in.\n" + ex.getMessage());
        }
    }

    @FXML
    private void onForgotPassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Forgot password");
        dialog.setHeaderText("Recover password");
        dialog.setContentText("Username:");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) {
            return;
        }

        String username = result.get().trim();
        TextInputDialog phraseDialog = new TextInputDialog();
        phraseDialog.setTitle("Forgot password");
        phraseDialog.setHeaderText("Security phrase");
        phraseDialog.setContentText("Forgot-password phrase:");
        Optional<String> phraseResult = phraseDialog.showAndWait();
        if (phraseResult.isEmpty() || phraseResult.get().trim().isEmpty()) {
            return;
        }

        try {
            String recovered = new OnboardingDao()
                    .findPasswordByPhrase(username, phraseResult.get().trim());
            if (recovered == null) {
                showAlert(Alert.AlertType.ERROR, "Recovery failed",
                        "Username or phrase is incorrect.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Password recovered",
                    "Your password is:\n" + recovered);
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Recovery failed",
                    "Could not recover password.\n" + ex.getMessage());
        }
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
