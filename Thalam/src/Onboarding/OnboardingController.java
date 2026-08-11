package Onboarding;

import Employees.EmployeesDao;
import Employees.EmployeesModel;
import Employees.Roles.RoleDao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import thalam.Thalam;

/**
 *
 * @author aruna
 */
public class OnboardingController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label hintLabel;
    @FXML private VBox companyStep;
    @FXML private VBox adminStep;
    @FXML private TextField companyNameField;
    @FXML private TextArea companyAddressField;
    @FXML private TextField adminNameField;
    @FXML private TextField adminPhoneField;
    @FXML private TextField adminEmailField;
    @FXML private TextArea adminAddressField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField forgotPhraseField;
    @FXML private Button primaryBtn;
    @FXML private Button backBtn;

    private boolean onAdminStep;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showCompanyStep();
    }

    @FXML
    private void onPrimaryAction(ActionEvent event) {
        if (!onAdminStep) {
            if (!validateCompany()) {
                return;
            }
            showAdminStep();
            return;
        }

        if (!validateAdmin()) {
            return;
        }

        try {
            saveOnboarding();
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Onboarding complete");
            ok.setHeaderText(null);
            ok.setContentText("Company and Super Admin created. Please sign in.");
            ok.showAndWait();
            Thalam.open((Node) event.getSource(), "/Login/Login.fxml");
        } catch (Exception ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Onboarding failed");
            error.setHeaderText(null);
            error.setContentText(ex.getMessage() == null ? "Could not save onboarding." : ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onBack() {
        showCompanyStep();
    }

    private void showCompanyStep() {
        onAdminStep = false;
        companyStep.setVisible(true);
        companyStep.setManaged(true);
        adminStep.setVisible(false);
        adminStep.setManaged(false);
        backBtn.setVisible(false);
        backBtn.setManaged(false);
        titleLabel.setText("SET UP YOUR COMPANY");
        hintLabel.setText("Step 1 of 2 — Company details");
        primaryBtn.setText("Next");
    }

    private void showAdminStep() {
        onAdminStep = true;
        companyStep.setVisible(false);
        companyStep.setManaged(false);
        adminStep.setVisible(true);
        adminStep.setManaged(true);
        backBtn.setVisible(true);
        backBtn.setManaged(true);
        titleLabel.setText("CREATE SUPER ADMIN");
        hintLabel.setText("Step 2 of 2 — Super Admin details");
        primaryBtn.setText("Finish");
    }

    private boolean validateCompany() {
        if (isBlank(companyNameField.getText())) {
            showValidation("Company name is required.");
            return false;
        }
        if (isBlank(companyAddressField.getText())) {
            showValidation("Company address is required.");
            return false;
        }
        return true;
    }

    private boolean validateAdmin() {
        if (isBlank(adminNameField.getText())) {
            showValidation("Super Admin name is required.");
            return false;
        }
        if (isBlank(usernameField.getText())) {
            showValidation("Username is required.");
            return false;
        }
        if (isBlank(passwordField.getText())) {
            showValidation("Password is required.");
            return false;
        }
        if (isBlank(forgotPhraseField.getText())) {
            showValidation("Forgot password phrase is required.");
            return false;
        }
        return true;
    }

    private void saveOnboarding() throws Exception {
        int roleId = new RoleDao().ensureSuperAdminRole();

        EmployeesModel employee = new EmployeesModel(
                trim(adminNameField.getText()),
                trim(adminPhoneField.getText()),
                trim(adminEmailField.getText()),
                trim(adminAddressField.getText()),
                roleId,
                "Super Admin");
        int employeeId = new EmployeesDao().insertReturningId(employee);

        OnboardingModel model = new OnboardingModel();
        model.setCompanyName(trim(companyNameField.getText()));
        model.setCompanyAddress(trim(companyAddressField.getText()));
        model.setUsername(trim(usernameField.getText()));
        model.setPassword(passwordField.getText());
        model.setForgotPwdPhrase(trim(forgotPhraseField.getText()));
        model.setEmployeeId(employeeId);

        new OnboardingDao().insert(model);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static void showValidation(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Missing details");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
