package Invoices;

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

public class InvoiceDetailsController implements Initializable {

    private static InvoicesModel pendingInvoice;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label projectValue;
    @FXML
    private Label purposeValue;
    @FXML
    private Label invoiceDateValue;
    @FXML
    private Label dueDateValue;
    @FXML
    private Label amountValue;
    @FXML
    private Label statusValue;
    @FXML
    private Label paidValue;
    @FXML
    private Label paymentModeValue;
    @FXML
    private Label paymentDateValue;
    @FXML
    private Label notesValue;
    @FXML
    private VBox projectRefBox;

    private InvoicesModel invoice;

    public static void prepare(InvoicesModel model) {
        pendingInvoice = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        invoice = pendingInvoice;
        pendingInvoice = null;

        if (invoice == null) {
            return;
        }

        displayNameLabel.setText("INV-" + invoice.getId());
        projectValue.setText(TableSearch.safe(invoice.getProjectName()));
        purposeValue.setText(TableSearch.safe(invoice.getInvoicePurpose()));
        invoiceDateValue.setText(formatDate(invoice.getInvoiceDate()));
        dueDateValue.setText(formatDate(invoice.getDueDate()));
        amountValue.setText(formatAmount(invoice.getInvoiceAmount()));
        statusValue.setText(TableSearch.safe(invoice.getStatus()));
        paidValue.setText(invoice.isPaid() ? "Paid" : "Unpaid");
        paymentModeValue.setText(TableSearch.safe(invoice.getPaymentMode()));
        paymentDateValue.setText(formatDate(invoice.getPaymentDate()));
        notesValue.setText(TableSearch.safe(invoice.getNotes()));

        loadRelated();
    }

    private void loadRelated() {
        List<String> projectLines = new ArrayList<>();
        if (invoice.getProjectId() > 0) {
            projectLines.add("PRO-" + invoice.getProjectId() + " · "
                    + TableSearch.safe(invoice.getProjectName()));
        }
        DetailsRefs.fill(projectRefBox, projectLines.isEmpty() ? Collections.emptyList() : projectLines);
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
        Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddInvoiceController.prepareEdit(invoice);
        Thalam.open((Node) event.getSource(), "/Invoices/AddInvoice.fxml");
    }

    @FXML
    private void onDeleteInvoice(ActionEvent event) throws IOException {
        if (invoice == null) {
            return;
        }

        String label = firstNonBlank(invoice.getInvoicePurpose(), invoice.getProjectName(), "invoice");
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Invoice");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete invoice \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new InvoicesService().deleteService(invoice.getId());
            Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Invoice");
            error.setHeaderText(null);
            error.setContentText("Could not delete invoice.\n" + ex.getMessage());
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
