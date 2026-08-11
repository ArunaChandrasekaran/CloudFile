package Projects;

import Clients.ClientsDao;
import Clients.ClientsModel;
import Employees.EmployeesDao;
import Employees.EmployeesModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import thalam.Thalam;

public class AddProjectController implements Initializable {

    private static ProjectsModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private TextField projectNameField;
    @FXML
    private ComboBox<ClientsModel> clientCombo;
    @FXML
    private ListView<EmployeesModel> employeesList;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextArea addressField;
    @FXML
    private TextField contractAmountField;
    @FXML
    private TextArea notes;

    private ProjectsModel editingProject;

    public static void prepareEdit(ProjectsModel project) {
        pendingEdit = project;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingProject = pendingEdit;
        pendingEdit = null;

        clientCombo.setConverter(new StringConverter<ClientsModel>() {
            @Override
            public String toString(ClientsModel client) {
                return client == null ? "" : client.getName();
            }

            @Override
            public ClientsModel fromString(String string) {
                return null;
            }
        });

        employeesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        employeesList.setCellFactory(list -> new ListCell<EmployeesModel>() {
            @Override
            protected void updateItem(EmployeesModel employee, boolean empty) {
                super.updateItem(employee, empty);
                setText(empty || employee == null ? null : employee.getName());
            }
        });

        try {
            ClientsDao clientsDao = new ClientsDao();
            clientCombo.setItems(FXCollections.observableArrayList(clientsDao.view()));

            EmployeesDao employeesDao = new EmployeesDao();
            employeesList.setItems(FXCollections.observableArrayList(employeesDao.view()));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (editingProject != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Project");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the project details.");
            }
            projectNameField.setText(editingProject.getProjectName());
            startDatePicker.setValue(editingProject.getStarDate());
            endDatePicker.setValue(editingProject.getExpectedEndDate());
            addressField.setText(editingProject.getAddress());
            if (editingProject.getContractAmount() != 0) {
                contractAmountField.setText(String.valueOf(editingProject.getContractAmount()));
            }
            notes.setText(editingProject.getNotes());

            for (ClientsModel client : clientCombo.getItems()) {
                if (client.getId() == editingProject.getClientId()) {
                    clientCombo.setValue(client);
                    break;
                }
            }

            try {
                List<EmployeesModel> associated =
                        new ProjectDao().viewAssociatedEmployees(editingProject.getId());
                Set<Integer> associatedIds = new HashSet<>();
                for (EmployeesModel emp : associated) {
                    if (emp.getId() != null) {
                        associatedIds.add(emp.getId());
                    }
                }
                employeesList.getSelectionModel().clearSelection();
                for (int i = 0; i < employeesList.getItems().size(); i++) {
                    EmployeesModel emp = employeesList.getItems().get(i);
                    if (emp.getId() != null && associatedIds.contains(emp.getId())) {
                        employeesList.getSelectionModel().select(i);
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(AddProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        String projectName = projectNameField.getText() == null
                ? "" : projectNameField.getText().trim();
        ClientsModel selectedClient = clientCombo.getValue();
        List<EmployeesModel> selectedEmployees = new ArrayList<>(
                employeesList.getSelectionModel().getSelectedItems());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String address = addressField.getText() == null ? "" : addressField.getText().trim();
        String amountText = contractAmountField.getText();
        String notesText = notes.getText() == null ? "" : notes.getText().trim();

        double contractAmount = 0;
        if (amountText != null && !amountText.isBlank()) {
            try {
                contractAmount = Double.parseDouble(amountText.replace(",", ""));
            } catch (NumberFormatException ex) {
                showError("Contract amount must be a valid number.");
                return;
            }
        }

        if (projectName.isEmpty()) {
            showError("Project name is required.");
            return;
        }
        if (selectedClient == null || selectedEmployees.isEmpty()
                || startDate == null || endDate == null) {
            showError("Please fill client, associated employees, start date and end date.");
            return;
        }
        if (endDate.isBefore(startDate)) {
            showError("End date cannot be before start date.");
            return;
        }

        List<Integer> employeeIds = new ArrayList<>();
        for (EmployeesModel employee : selectedEmployees) {
            employeeIds.add(employee.getId());
        }

        ProjectsModel model = new ProjectsModel();
        model.setProjectName(projectName);
        model.setClientId(selectedClient.getId());
        model.setEmployeeIds(employeeIds);
        model.setStarDate(startDate);
        model.setExpectedEndDate(endDate);
        model.setAddress(address);
        model.setContractAmount(contractAmount);
        model.setNotes(notesText);

        ProjectsService service = new ProjectsService();

        try {
            if (editingProject != null) {
                model.setId(editingProject.getId());
                model.setStatus(editingProject.getStatus());
                model.applyResolvedStatus();
                service.updateService(model);
            } else {
                model.applyResolvedStatus();
                service.insertService(model);
            }
            Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddProjectController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Could not save project.\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Project");
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
