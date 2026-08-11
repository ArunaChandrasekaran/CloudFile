package Employees.Roles;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import thalam.TableSearch;
import thalam.Thalam;

public class AddRoleController implements Initializable {

    private static RoleModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private TextField roleNameField;
    @FXML
    private TextArea descriptionField;

    private RoleModel editingRole;

    public static void prepareEdit(RoleModel role) {
        pendingEdit = role;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingRole = pendingEdit;
        pendingEdit = null;

        if (editingRole != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Role");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the role details.");
            }
            roleNameField.setText(TableSearch.safe(editingRole.getName()));
            descriptionField.setText(TableSearch.safe(editingRole.getDescription()));
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Roles/Roles.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        String name = roleNameField.getText();
        String description = descriptionField.getText();

        RoleModel modelObject = new RoleModel(name, description);
        RoleService serviceObject = new RoleService();

        try {
            if (editingRole != null) {
                modelObject.setId(editingRole.getId());
                serviceObject.updateService(modelObject);
            } else {
                serviceObject.insertService(modelObject);
            }
            Thalam.open((Node) event.getSource(), "/Employees/Roles/Roles.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Role");
            alert.setHeaderText(null);
            alert.setContentText("Could not save role.\n" + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onNavDashboard(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Dashboard/Dashboard.fxml");
    }

    @FXML
    private void onNavClients(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Clients/Clients.fxml");
    }

    @FXML
    private void onNavProjectsList(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
    }

    @FXML
    private void onNavDailyWorklog(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
    }

    @FXML
    private void onNavExpenses(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Expenses/Expenses.fxml");
    }

    @FXML
    private void onNavInvoices(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onNavVendors(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Vendors/Vendors.fxml");
    }

    @FXML
    private void onNavMaterials(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Materials.fxml");
    }

    @FXML
    private void onNavPurchases(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Purchases/Purchases.fxml");
    }

    @FXML
    private void onNavEmployees(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Employees.fxml");
    }

    @FXML
    private void onNavRoles(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Roles/Roles.fxml");
    }

    @FXML
    private void onNavReports(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Reports/Reports.fxml");
    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Login/Login.fxml");
    }
}
