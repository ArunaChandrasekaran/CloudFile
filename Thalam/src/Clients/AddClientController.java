package Clients;

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
import thalam.Thalam;

public class AddClientController implements Initializable {

    private static ClientsModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private TextField clientNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField altPhoneField;
    @FXML
    private TextArea addressField;

    private ClientsModel editingClient;

    public static void prepareEdit(ClientsModel client) {
        pendingEdit = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingClient = pendingEdit;
        pendingEdit = null;

        if (editingClient != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Client");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the client details.");
            }
            clientNameField.setText(editingClient.getName());
            phoneField.setText(editingClient.getPhone());
            emailField.setText(editingClient.getEmail());
            altPhoneField.setText(editingClient.getAltPhone());
            addressField.setText(editingClient.getAddress());
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Clients/Clients.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        String name = clientNameField.getText() == null ? "" : clientNameField.getText().trim();
        String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String altPhone = altPhoneField.getText() == null ? "" : altPhoneField.getText().trim();
        String address = addressField.getText() == null ? "" : addressField.getText().trim();

        if (name.isEmpty()) {
            showError("Client name is required.");
            return;
        }

        ClientsModel c = new ClientsModel(name, phone, altPhone, email, address);
        ClientsService cs = new ClientsService();

        try {
            if (editingClient != null) {
                c.setId(editingClient.getId());
                cs.updateService(c);
            } else {
                cs.insertService(c);
            }
            Thalam.open((Node) event.getSource(), "/Clients/Clients.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            showError("Could not save client.\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Client");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
