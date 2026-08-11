package Projects.DailyWorklog;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
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

public class DailyWorklogController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<DailyWorklogModel> recordsTable;
    @FXML
    private TableColumn<DailyWorklogModel, Integer> serialColumn;
    @FXML
    private TableColumn<DailyWorklogModel, String> idColumn;
    @FXML
    private TableColumn<DailyWorklogModel, String> projectNameColumn;
    @FXML
    private TableColumn<DailyWorklogModel, LocalDate> workDateColumn;
    @FXML
    private TableColumn<DailyWorklogModel, String> employeeColumn;
    @FXML
    private TableColumn<DailyWorklogModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<DailyWorklogModel> master = FXCollections.observableArrayList();
    private final DailyWorklogDao worklogDao = new DailyWorklogDao();
    private final DailyWorklogService worklogService = new DailyWorklogService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cell -> {
            DailyWorklogModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "WOR-" + row.getId());
        });
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        workDateColumn.setCellValueFactory(new PropertyValueFactory<>("workDate"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        pageState = TableSearch.bind(searchField, recordsTable, master, w ->
                "WOR-" + w.getId() + " " + TableSearch.safe(w.getProjectName())
                        + " " + TableSearch.safe(w.getEmployeeName())
                        + " " + (w.getWorkDate() == null ? "" : w.getWorkDate().toString()),
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

    private void onOpenDetails(DailyWorklogModel worklog) {
        try {
            WorklogDetailsController.prepare(worklog);
            Thalam.open(recordsTable, "/Projects/DailyWorklog/WorklogDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(DailyWorklogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(worklogDao.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DailyWorklogController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(DailyWorklogModel worklog) {
        try {
            AddWorklogController.prepareEdit(worklog);
            Thalam.open(recordsTable, "/Projects/DailyWorklog/AddWorklog.fxml");
        } catch (IOException ex) {
            Logger.getLogger(DailyWorklogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(DailyWorklogModel worklog) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Worklog");
        confirm.setHeaderText(null);
        String label = worklog.getProjectName() == null
                ? ("#" + worklog.getId())
                : worklog.getProjectName();
        if (worklog.getWorkDate() != null) {
            label = label + " (" + worklog.getWorkDate() + ")";
        }
        confirm.setContentText("Delete worklog \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            worklogService.deleteService(worklog.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DailyWorklogController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Worklog");
            error.setHeaderText(null);
            error.setContentText("Could not delete worklog.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddWorklogController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/AddWorklog.fxml");
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
