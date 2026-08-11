package Invoices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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

public class InvoicesController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<InvoicesModel> recordsTable;
    @FXML
    private TableColumn<InvoicesModel, Integer> serialColumn;
    @FXML
    private TableColumn<InvoicesModel, String> idColumn;
    @FXML
    private TableColumn<InvoicesModel, String> projectNameColumn;
    @FXML
    private TableColumn<InvoicesModel, String> purposeColumn;
    @FXML
    private TableColumn<InvoicesModel, LocalDate> invoiceDateColumn;
    @FXML
    private TableColumn<InvoicesModel, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<InvoicesModel, Double> amountColumn;
    @FXML
    private TableColumn<InvoicesModel, LocalDate> paymentDateColumn;
    @FXML
    private TableColumn<InvoicesModel, String> statusColumn;
    @FXML
    private TableColumn<InvoicesModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<InvoicesModel> master = FXCollections.observableArrayList();
    private final InvoicesDao invoicesDao = new InvoicesDao();
    private final InvoicesService invoicesService = new InvoicesService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cell -> {
            InvoicesModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "INV-" + row.getId());
        });
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("invoicePurpose"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceAmount"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        pageState = TableSearch.bind(searchField, recordsTable, master, i ->
                "INV-" + i.getId() + " " + TableSearch.safe(i.getProjectName())
                        + " " + TableSearch.safe(i.getInvoicePurpose())
                        + " " + TableSearch.safe(i.getStatus()),
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

    private void onOpenDetails(InvoicesModel invoice) {
        try {
            InvoiceDetailsController.prepare(invoice);
            Thalam.open(recordsTable, "/Invoices/InvoiceDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(InvoicesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(invoicesDao.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(InvoicesController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(InvoicesModel invoice) {
        try {
            AddInvoiceController.prepareEdit(invoice);
            Thalam.open(recordsTable, "/Invoices/AddInvoice.fxml");
        } catch (IOException ex) {
            Logger.getLogger(InvoicesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(InvoicesModel invoice) {
        String label = invoice.getInvoicePurpose() != null && !invoice.getInvoicePurpose().isBlank()
                ? invoice.getInvoicePurpose()
                : "INV-" + invoice.getId();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Invoice");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete invoice \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            invoicesService.deleteService(invoice.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(InvoicesController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Invoice");
            error.setHeaderText(null);
            error.setContentText("Could not delete invoice.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddInvoiceController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Invoices/AddInvoice.fxml");
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
