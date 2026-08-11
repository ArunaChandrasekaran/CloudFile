package Employees;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import thalam.TableActionButtons;
import thalam.TableRowDetails;
import thalam.TableSearch;
import thalam.Thalam;

public class EmployeesController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<EmployeesModel> recordsTable;
    @FXML
    private TableColumn<EmployeesModel, Integer> serialColumn;
    @FXML
    private TableColumn<EmployeesModel, String> id;
    @FXML
    private TableColumn<EmployeesModel, String> name;
    @FXML
    private TableColumn<EmployeesModel, String> role;
    @FXML
    private TableColumn<EmployeesModel, String> phone;
    @FXML
    private TableColumn<EmployeesModel, String> email;
    @FXML
    private TableColumn<EmployeesModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<EmployeesModel> master = FXCollections.observableArrayList();
    private final EmployeesDao employeesDao = new EmployeesDao();
    private final EmployeesService employeesService = new EmployeesService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(cell -> {
            EmployeesModel row = cell.getValue();
            return new SimpleStringProperty(row == null || row.getId() == null
                    ? "" : "EMP-" + row.getId());
        });
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        pageState = TableSearch.bind(searchField, recordsTable, master, e ->
                "EMP-" + e.getId() + " " + TableSearch.safe(e.getName())
                        + " " + TableSearch.safe(e.getRole())
                        + " " + TableSearch.safe(e.getPhone())
                        + " " + TableSearch.safe(e.getEmail()),
                recordsSummaryLabel, prevPageButton, pageNumberButton, nextPageButton);

        serialColumn.setSortable(false);
        serialColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(pageState.firstRowNumber() + getIndex()));
            }
        });

        TableActionButtons.attach(actionsColumn, this::onEdit, this::onDelete);
        TableRowDetails.attach(recordsTable, this::onOpenDetails);
        reloadTable();
    }

    private void onOpenDetails(EmployeesModel employee) {
        try {
            EmployeeDetailsController.prepare(employee);
            Thalam.open(recordsTable, "/Employees/EmployeeDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(EmployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(employeesDao.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeesController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(EmployeesModel employee) {
        try {
            AddEmployeeController.prepareEdit(employee);
            Thalam.open(recordsTable, "/Employees/AddEmployee.fxml");
        } catch (IOException ex) {
            Logger.getLogger(EmployeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(EmployeesModel employee) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Employee");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete employee \"" + TableSearch.safe(employee.getName()) + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            employeesService.deleteService(employee.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeesController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Employee");
            error.setHeaderText(null);
            error.setContentText("Could not delete employee.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddEmployeeController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Employees/AddEmployee.fxml");
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
