package Projects;

import Employees.EmployeesModel;
import Expenses.ExpensesDao;
import Expenses.ExpensesModel;
import Invoices.InvoicesDao;
import Invoices.InvoicesModel;
import Projects.DailyWorklog.DailyWorklogDao;
import Projects.DailyWorklog.DailyWorklogModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import thalam.DetailsRefs;
import thalam.TableSearch;
import thalam.Thalam;

public class ProjectDetailsController implements Initializable {

    private static final int REF_LIMIT = 5;
    private static ProjectsModel pendingProject;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label nameValue;
    @FXML
    private Label clientValue;
    @FXML
    private Label startDateValue;
    @FXML
    private Label endDateValue;
    @FXML
    private Label addressValue;
    @FXML
    private Label contractAmountValue;
    @FXML
    private Label statusValue;
    @FXML
    private Button markCompletedButton;
    @FXML
    private Label notesValue;
    @FXML
    private VBox employeesListBox;
    @FXML
    private VBox worklogsRefBox;
    @FXML
    private VBox invoicesRefBox;
    @FXML
    private VBox expensesRefBox;
    @FXML
    private VBox clientRefBox;

    private ProjectsModel project;

    public static void prepare(ProjectsModel model) {
        pendingProject = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        project = pendingProject;
        pendingProject = null;

        if (project == null) {
            return;
        }

        String name = TableSearch.safe(project.getProjectName());
        displayNameLabel.setText("PRO-" + project.getId());
        nameValue.setText(name);
        clientValue.setText(TableSearch.safe(project.getClient()));
        startDateValue.setText(formatDate(project.getStarDate()));
        endDateValue.setText(formatDate(project.getExpectedEndDate()));
        addressValue.setText(TableSearch.safe(project.getAddress()));
        contractAmountValue.setText(formatAmount(project.getContractAmount()));
        project.applyResolvedStatus();
        applyStatusBadge(project.getStatus(), project.getStatusKey());
        notesValue.setText(TableSearch.safe(project.getNotes()));
        DetailsRefs.fill(employeesListBox, loadEmployeeLines(project.getId()));
        if (markCompletedButton != null) {
            boolean completed = ProjectsModel.STATUS_COMPLETED.equalsIgnoreCase(project.getStatus());
            markCompletedButton.setDisable(completed);
            markCompletedButton.setVisible(!completed);
            markCompletedButton.setManaged(!completed);
        }

        loadRelated();
    }

    @FXML
    private void onMarkCompleted(ActionEvent event) {
        if (project == null) {
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Mark Completed");
        confirm.setHeaderText(null);
        confirm.setContentText("Mark project \"" + TableSearch.safe(project.getProjectName())
                + "\" as Completed?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        try {
            new ProjectsService().markCompletedService(project.getId());
            project.setStatus(ProjectsModel.STATUS_COMPLETED);
            applyStatusBadge(ProjectsModel.STATUS_COMPLETED, "completed");
            if (markCompletedButton != null) {
                markCompletedButton.setDisable(true);
                markCompletedButton.setVisible(false);
                markCompletedButton.setManaged(false);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Mark Completed");
            error.setHeaderText(null);
            error.setContentText("Could not update project status.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    private void applyStatusBadge(String status, String key) {
        statusValue.setText(TableSearch.safe(status));
        statusValue.getStyleClass().setAll("project-status-badge", "project-status-" + key);
    }

    private void loadRelated() {
        List<String> worklogLines = new ArrayList<>();
        List<String> invoiceLines = new ArrayList<>();
        List<String> expenseLines = new ArrayList<>();
        List<String> clientLines = new ArrayList<>();
        try {
            for (DailyWorklogModel w : new DailyWorklogDao().viewRecentByProject(project.getId(), REF_LIMIT)) {
                String label = formatDate(w.getWorkDate());
                if (label.isEmpty()) {
                    label = TableSearch.safe(w.getEmployeeName());
                }
                if (label.isEmpty()) {
                    label = TableSearch.safe(w.getWorkDescription());
                }
                worklogLines.add("WOR-" + w.getId() + " · " + label);
            }
            for (InvoicesModel i : new InvoicesDao().viewRecentByProject(project.getId(), REF_LIMIT)) {
                invoiceLines.add("INV-" + i.getId() + " · "
                        + TableSearch.safe(i.getInvoicePurpose()));
            }
            for (ExpensesModel e : new ExpensesDao().viewRecentByProject(project.getId(), REF_LIMIT)) {
                expenseLines.add("EXP-" + e.getId() + " · "
                        + TableSearch.safe(e.getCategory()));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (project.getClientId() > 0) {
            clientLines.add("CLI-" + project.getClientId() + " · "
                    + TableSearch.safe(project.getClient()));
        }
        DetailsRefs.fill(worklogsRefBox, worklogLines);
        DetailsRefs.fill(invoicesRefBox, invoiceLines);
        DetailsRefs.fill(expensesRefBox, expenseLines);
        DetailsRefs.fill(clientRefBox, clientLines.isEmpty() ? Collections.emptyList() : clientLines);
    }

    private static List<String> loadEmployeeLines(int projectId) {
        try {
            List<EmployeesModel> employees = new ProjectDao().viewAssociatedEmployees(projectId);
            if (employees == null || employees.isEmpty()) {
                return Collections.emptyList();
            }
            List<String> lines = new ArrayList<>();
            for (EmployeesModel employee : employees) {
                String name = employee.getName() == null ? "" : employee.getName().trim();
                if (name.isEmpty()) {
                    continue;
                }
                if (employee.getId() != null && employee.getId() > 0) {
                    lines.add("EMP-" + employee.getId() + " · " + name);
                } else {
                    lines.add(name);
                }
            }
            return lines;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
    }

    private static String formatDate(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    private static String formatAmount(double amount) {
        return String.format("₹ %.2f", amount);
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddProjectController.prepareEdit(project);
        Thalam.open((Node) event.getSource(), "/Projects/AddProject.fxml");
    }

    @FXML
    private void onDeleteProject(ActionEvent event) throws IOException {
        if (project == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Project");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete project \"" + TableSearch.safe(project.getProjectName()) + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new ProjectsService().deleteService(project.getId());
            Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Project");
            error.setHeaderText(null);
            error.setContentText("Could not delete project.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onViewAllWorklogs(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
    }

    @FXML
    private void onViewAllInvoices(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onViewAllExpenses(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Expenses/Expenses.fxml");
    }

    @FXML
    private void onViewAllClients(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Clients/Clients.fxml");
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
