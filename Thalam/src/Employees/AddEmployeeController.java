package Employees;

import Employees.Roles.RoleDao;
import Employees.Roles.RoleModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import thalam.TableSearch;
import thalam.Thalam;

public class AddEmployeeController implements Initializable {

    private static EmployeesModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private ComboBox<RoleModel> roleCombo;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea addressField;

    private EmployeesModel editingEmployee;

    public static void prepareEdit(EmployeesModel employee) {
        pendingEdit = employee;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingEmployee = pendingEdit;
        pendingEdit = null;

        roleCombo.setConverter(new StringConverter<RoleModel>() {
            @Override
            public String toString(RoleModel role) {
                return role == null ? "" : role.getName();
            }

            @Override
            public RoleModel fromString(String string) {
                return null;
            }
        });

        try {
            RoleDao dao = new RoleDao();
            roleCombo.setItems(FXCollections.observableArrayList(dao.view()));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (editingEmployee != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Employee");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the employee details.");
            }
            nameField.setText(TableSearch.safe(editingEmployee.getName()));
            phoneField.setText(TableSearch.safe(editingEmployee.getPhone()));
            emailField.setText(TableSearch.safe(editingEmployee.getEmail()));
            addressField.setText(TableSearch.safe(editingEmployee.getAddress()));

            for (RoleModel role : roleCombo.getItems()) {
                if (role.getId() == editingEmployee.getRoleId()) {
                    roleCombo.setValue(role);
                    break;
                }
            }
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Employees.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        RoleModel selectedRole = roleCombo.getValue();

        if (selectedRole == null) {
            return;
        }

        EmployeesModel model = new EmployeesModel(
                name,
                phone,
                email,
                address,
                selectedRole.getId(),
                selectedRole.getName());

        EmployeesService service = new EmployeesService();

        try {
            if (editingEmployee != null) {
                model.setId(editingEmployee.getId());
                service.updateService(model);
            } else {
                service.insertService(model);
            }
            Thalam.open((Node) event.getSource(), "/Employees/Employees.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Employee");
            alert.setHeaderText(null);
            alert.setContentText("Could not save employee.\n" + ex.getMessage());
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
