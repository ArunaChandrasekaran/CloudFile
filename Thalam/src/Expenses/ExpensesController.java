package Expenses;

import Materials.Purchases.PurchaseService;
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

public class ExpensesController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<ExpensesModel> recordsTable;
    @FXML
    private TableColumn<ExpensesModel, Integer> serialColumn;
    @FXML
    private TableColumn<ExpensesModel, String> idColumn;
    @FXML
    private TableColumn<ExpensesModel, LocalDate> dateColumn;
    @FXML
    private TableColumn<ExpensesModel, String> expenseTypeColumn;
    @FXML
    private TableColumn<ExpensesModel, String> projectNameColumn;
    @FXML
    private TableColumn<ExpensesModel, String> categoryColumn;
    @FXML
    private TableColumn<ExpensesModel, Double> amountColumn;
    @FXML
    private TableColumn<ExpensesModel, String> paymentColumn;
    @FXML
    private TableColumn<ExpensesModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<ExpensesModel> master = FXCollections.observableArrayList();
    private final ExpensesDao expensesDao = new ExpensesDao();
    private final ExpensesService expensesService = new ExpensesService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cell -> {
            ExpensesModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "EXP-" + row.getId());
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        expenseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("expenseType"));
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        pageState = TableSearch.bind(searchField, recordsTable, master, e ->
                "EXP-" + e.getId() + " " + TableSearch.safe(e.getExpenseType())
                        + " " + TableSearch.safe(e.getCategory())
                        + " " + TableSearch.safe(e.getProjectName())
                        + " " + TableSearch.safe(e.getPaymentStatus()),
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
        try {
            new PurchaseService().backfillLinkedExpenses();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ExpensesController.class.getName()).log(Level.WARNING, null, ex);
        }
        reloadTable();
    }

    private void onOpenDetails(ExpensesModel expense) {
        try {
            ExpenseDetailsController.prepare(expense);
            Thalam.open(recordsTable, "/Expenses/ExpenseDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ExpensesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(expensesDao.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ExpensesController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(ExpensesModel expense) {
        if (expense.getPurchaseId() != null && expense.getPurchaseId() > 0) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Purchase Expense");
            info.setHeaderText(null);
            info.setContentText("This expense is linked to a purchase. Edit it from Purchases.");
            info.showAndWait();
            return;
        }
        try {
            AddExpenseController.prepareEdit(expense);
            Thalam.open(recordsTable, "/Expenses/AddExpense.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ExpensesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(ExpensesModel expense) {
        if (expense.getPurchaseId() != null && expense.getPurchaseId() > 0) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Purchase Expense");
            info.setHeaderText(null);
            info.setContentText("This expense is linked to a purchase. Delete it from Purchases.");
            info.showAndWait();
            return;
        }
        String label = expense.getCategory() != null && !expense.getCategory().isBlank()
                ? expense.getCategory()
                : expense.getExpenseType();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Expense");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete expense \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            expensesService.deleteService(expense.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ExpensesController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Expense");
            error.setHeaderText(null);
            error.setContentText("Could not delete expense.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddExpenseController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Expenses/AddExpense.fxml");
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
