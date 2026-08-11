package Employees.Roles;

import Employees.EmployeesDao;
import Employees.EmployeesModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import thalam.DetailsRefs;
import thalam.TableSearch;
import thalam.Thalam;

public class RoleDetailsController implements Initializable {

    private static final int REF_LIMIT = 5;
    private static RoleModel pendingRole;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label nameValue;
    @FXML
    private Label descriptionValue;
    @FXML
    private VBox employeesRefBox;

    private RoleModel role;

    public static void prepare(RoleModel model) {
        pendingRole = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        role = pendingRole;
        pendingRole = null;

        if (role == null) {
            return;
        }

        String name = TableSearch.safe(role.getName());
        displayNameLabel.setText("ROL-" + role.getId());
        nameValue.setText(name);
        descriptionValue.setText(TableSearch.safe(role.getDescription()));

        loadRelated();
    }

    private void loadRelated() {
        List<String> employeeLines = new ArrayList<>();
        try {
            for (EmployeesModel e : new EmployeesDao().viewRecentByRole(role.getId(), REF_LIMIT)) {
                employeeLines.add("EMP-" + e.getId() + " · "
                        + TableSearch.safe(e.getName()));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RoleDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DetailsRefs.fill(employeesRefBox, employeeLines);
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Roles/Roles.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddRoleController.prepareEdit(role);
        Thalam.open((Node) event.getSource(), "/Employees/Roles/AddRole.fxml");
    }

    @FXML
    private void onDeleteRole(ActionEvent event) throws IOException {
        if (role == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Role");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete role \"" + TableSearch.safe(role.getName()) + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new RoleService().deleteService(role.getId());
            Thalam.open((Node) event.getSource(), "/Employees/Roles/Roles.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Role");
            error.setHeaderText(null);
            error.setContentText("Could not delete role.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onViewAllEmployees(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Employees.fxml");
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
