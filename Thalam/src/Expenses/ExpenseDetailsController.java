package Expenses;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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

public class ExpenseDetailsController implements Initializable {

    private static ExpensesModel pendingExpense;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label dateValue;
    @FXML
    private Label typeValue;
    @FXML
    private Label projectValue;
    @FXML
    private Label categoryValue;
    @FXML
    private Label amountValue;
    @FXML
    private Label paidPaymentValue;
    @FXML
    private Label notesValue;
    @FXML
    private VBox projectRefBox;

    private ExpensesModel expense;

    public static void prepare(ExpensesModel model) {
        pendingExpense = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        expense = pendingExpense;
        pendingExpense = null;

        if (expense == null) {
            return;
        }

        displayNameLabel.setText("EXP-" + expense.getId());
        dateValue.setText(formatDate(expense.getExpenseDate()));
        typeValue.setText(TableSearch.safe(expense.getExpenseType()));
        projectValue.setText(TableSearch.safe(expense.getProjectName()));
        categoryValue.setText(TableSearch.safe(expense.getCategory()));
        amountValue.setText(formatAmount(expense.getAmount()));
        paidPaymentValue.setText(formatPaidPayment(expense));
        notesValue.setText(TableSearch.safe(expense.getNotes()));

        loadRelated();
    }

    private void loadRelated() {
        List<String> projectLines = new ArrayList<>();
        Integer projectId = expense.getProjectId();
        if (projectId != null && projectId > 0) {
            projectLines.add("PRO-" + projectId + " · "
                    + TableSearch.safe(expense.getProjectName()));
        }
        DetailsRefs.fill(projectRefBox, projectLines.isEmpty() ? Collections.emptyList() : projectLines);
    }

    private static String formatPaidPayment(ExpensesModel model) {
        if (model.isPaid()) {
            String mode = TableSearch.safe(model.getPaymentMode());
            return mode.isEmpty() ? "Paid" : "Paid (" + mode + ")";
        }
        String status = TableSearch.safe(model.getPaymentStatus());
        return status.isEmpty() ? "Unpaid" : status;
    }

    private static String formatDate(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    private static String formatAmount(double amount) {
        return String.format("₹ %.2f", amount);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return "";
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Expenses/Expenses.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        if (expense != null && expense.getPurchaseId() != null && expense.getPurchaseId() > 0) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Purchase Expense");
            info.setHeaderText(null);
            info.setContentText("This expense is linked to a purchase. Edit it from Purchases.");
            info.showAndWait();
            return;
        }
        AddExpenseController.prepareEdit(expense);
        Thalam.open((Node) event.getSource(), "/Expenses/AddExpense.fxml");
    }

    @FXML
    private void onDeleteExpense(ActionEvent event) throws IOException {
        if (expense == null) {
            return;
        }
        if (expense.getPurchaseId() != null && expense.getPurchaseId() > 0) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Purchase Expense");
            info.setHeaderText(null);
            info.setContentText("This expense is linked to a purchase. Delete it from Purchases.");
            info.showAndWait();
            return;
        }

        String label = firstNonBlank(expense.getCategory(), expense.getExpenseType(), "expense");
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Expense");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete expense \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new ExpensesService().deleteService(expense.getId());
            Thalam.open((Node) event.getSource(), "/Expenses/Expenses.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Expense");
            error.setHeaderText(null);
            error.setContentText("Could not delete expense.\n" + ex.getMessage());
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
