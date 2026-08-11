package Employees;

import Projects.ProjectDao;
import Projects.ProjectsModel;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import thalam.DetailsRefs;
import thalam.TableSearch;
import thalam.Thalam;

public class EmployeeDetailsController implements Initializable {

    private static final int REF_LIMIT = 5;
    private static EmployeesModel pendingEmployee;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label nameValue;
    @FXML
    private Label roleValue;
    @FXML
    private Label phoneValue;
    @FXML
    private Hyperlink emailLink;
    @FXML
    private Label addressValue;
    @FXML
    private VBox projectsRefBox;

    private EmployeesModel employee;

    public static void prepare(EmployeesModel model) {
        pendingEmployee = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employee = pendingEmployee;
        pendingEmployee = null;

        if (employee == null) {
            return;
        }

        String name = TableSearch.safe(employee.getName());
        displayNameLabel.setText(employee.getId() == null ? "EMP" : "EMP-" + employee.getId());
        nameValue.setText(name);
        roleValue.setText(TableSearch.safe(employee.getRole()));
        phoneValue.setText(TableSearch.safe(employee.getPhone()));
        addressValue.setText(TableSearch.safe(employee.getAddress()));

        String email = TableSearch.safe(employee.getEmail());
        emailLink.setText(email);
        emailLink.setDisable(email.isEmpty());
        emailLink.setVisited(false);

        loadRelated();
    }

    private void loadRelated() {
        List<String> projectLines = new ArrayList<>();
        if (employee.getId() != null && employee.getId() > 0) {
            try {
                for (ProjectsModel p : new ProjectDao().viewRecentByEmployee(employee.getId(), REF_LIMIT)) {
                    projectLines.add("PRO-" + p.getId() + " · "
                            + TableSearch.safe(p.getProjectName()));
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(EmployeeDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        DetailsRefs.fill(projectsRefBox, projectLines);
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Employees/Employees.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddEmployeeController.prepareEdit(employee);
        Thalam.open((Node) event.getSource(), "/Employees/AddEmployee.fxml");
    }

    @FXML
    private void onDeleteEmployee(ActionEvent event) throws IOException {
        if (employee == null || employee.getId() == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Employee");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete employee \"" + TableSearch.safe(employee.getName()) + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new EmployeesService().deleteService(employee.getId());
            Thalam.open((Node) event.getSource(), "/Employees/Employees.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Employee");
            error.setHeaderText(null);
            error.setContentText("Could not delete employee.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onViewAllProjects(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
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
