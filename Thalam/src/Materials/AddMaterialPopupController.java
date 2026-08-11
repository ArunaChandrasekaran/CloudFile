package Materials;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMaterialPopupController implements Initializable {

    @FXML
    private TextField materialNameField;
    @FXML
    private ComboBox<String> unitCombo;
    @FXML
    private TextArea notesField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onCancel(ActionEvent event) {
        closePopup(event);
    }

    @FXML
    private void onSave(ActionEvent event) {
        String name = materialNameField.getText() == null ? "" : materialNameField.getText().trim();
        String unit = unitCombo.getValue();
        String notes = notesField.getText() == null ? "" : notesField.getText().trim();

        if (name.isEmpty() || unit == null || unit.isEmpty()) {
            showError("Please enter material name and select a unit.");
            return;
        }

        MaterialsModel model = new MaterialsModel(name, unit, notes);

        try {
            MaterialsService service = new MaterialsService();
            service.insertService(model);
            // Close popup → returns to Add Purchase (showAndWait resumes)
            closePopup(event);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            showError("Could not save material.\n" + ex.getMessage());
        }
    }

    private void closePopup(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add Material");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
