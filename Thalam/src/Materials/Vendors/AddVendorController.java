package Materials.Vendors;

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

public class AddVendorController implements Initializable {

    private static VendorsModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private TextField vendorNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField altPhoneField;
    @FXML
    private TextArea addressField;

    private VendorsModel editingVendor;

    public static void prepareEdit(VendorsModel vendor) {
        pendingEdit = vendor;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingVendor = pendingEdit;
        pendingEdit = null;

        if (editingVendor != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Vendor");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the vendor details.");
            }
            vendorNameField.setText(TableSearch.safe(editingVendor.getName()));
            phoneField.setText(TableSearch.safe(editingVendor.getPhone()));
            emailField.setText(TableSearch.safe(editingVendor.getEmail()));
            altPhoneField.setText(TableSearch.safe(editingVendor.getAltPhone()));
            addressField.setText(TableSearch.safe(editingVendor.getAddress()));
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Vendors/Vendors.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        String name = vendorNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String altPhone = altPhoneField.getText();
        String address = addressField.getText();

        VendorsModel v = new VendorsModel(name, phone, email, altPhone, address);
        VendorsService vs = new VendorsService();

        try {
            if (editingVendor != null) {
                v.setId(editingVendor.getId());
                vs.updateService(v);
            } else {
                vs.insertService(v);
            }
            Thalam.open((Node) event.getSource(), "/Materials/Vendors/Vendors.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Vendor");
            alert.setHeaderText(null);
            alert.setContentText("Could not save vendor.\n" + ex.getMessage());
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
