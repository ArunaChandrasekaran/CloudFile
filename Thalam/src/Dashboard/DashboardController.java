package Dashboard;

import Invoices.InvoicesDao;
import Invoices.InvoicesModel;
import Materials.MaterialsDao;
import Materials.Purchases.PurchaseDao;
import Materials.Purchases.PurchaseModel;
import Projects.DailyWorklog.DailyWorklogDao;
import Projects.DailyWorklog.DailyWorklogModel;
import Projects.ProjectDao;
import Projects.ProjectsModel;
import com.ibm.icu.text.NumberFormat;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import thalam.Thalam;

public class DashboardController implements Initializable {

    private static final int DASHBOARD_LIST_LIMIT = 3;
    private static final DateTimeFormatter ACTIVITY_DATE =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter LIST_DATE =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final NumberFormat AMOUNT_FORMAT =
            NumberFormat.getNumberInstance(Locale.forLanguageTag("en-IN"));

    @FXML
    private Label totalProjectsValue;
    @FXML
    private Label totalClientsValue;
    @FXML
    private Label employeesValue;
    @FXML
    private Label pendingExpensesValue;
    @FXML
    private Label pendingInvoicesValue;
    @FXML
    private ComboBox<ProjectsModel> materialProjectCombo;
    @FXML
    private Label outOfStockValue;
    @FXML
    private Label lowStockValue;
    @FXML
    private Label nearRestockValue;
    @FXML
    private Label inStockValue;
    @FXML
    private Label totalOverdueAmountValue;
    @FXML
    private Label totalUnpaidPurchasesValue;
    @FXML
    private VBox recentActivitiesBox;
    @FXML
    private VBox overdueInvoicesBox;
    @FXML
    private VBox unpaidPurchasesBox;

    private final DashboardDao dashboardDao = new DashboardDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setStatValue(totalProjectsValue, String.valueOf(dashboardDao.getTotalProjects()));
            setStatValue(totalClientsValue, String.valueOf(dashboardDao.getTotalClients()));
            setStatValue(employeesValue, String.valueOf(dashboardDao.getTotalEmployees()));
            setStatValue(pendingExpensesValue, AMOUNT_FORMAT.format(dashboardDao.getPendingExpenseAmount()));
            setStatValue(pendingInvoicesValue, AMOUNT_FORMAT.format(dashboardDao.getPendingInvoiceAmount()));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        setupMaterialProjectCombo();
        loadRecentActivities();
        loadOverdueInvoices();
        loadUnpaidPurchases();
    }

    private void setupMaterialProjectCombo() {
        materialProjectCombo.setConverter(new StringConverter<ProjectsModel>() {
            @Override
            public String toString(ProjectsModel project) {
                return project == null ? "" : project.getProjectName();
            }

            @Override
            public ProjectsModel fromString(String string) {
                return null;
            }
        });

        try {
            List<ProjectsModel> projects = new ProjectDao().view();
            materialProjectCombo.setItems(FXCollections.observableArrayList(projects));
            if (!projects.isEmpty()) {
                materialProjectCombo.setValue(projects.get(0));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        materialProjectCombo.valueProperty().addListener((obs, oldVal, selected) ->
                loadMaterialOverview(selected));
        loadMaterialOverview(materialProjectCombo.getValue());
    }

    private void loadMaterialOverview(ProjectsModel selected) {
        if (selected == null || selected.getId() <= 0) {
            setStatValue(outOfStockValue, "0");
            setStatValue(lowStockValue, "0");
            setStatValue(nearRestockValue, "0");
            setStatValue(inStockValue, "0");
            return;
        }

        try {
            MaterialsDao.MaterialStockCounts stock =
                    new MaterialsDao().countStockByStatus(selected.getId());
            setStatValue(outOfStockValue, String.valueOf(stock.outOfStock));
            setStatValue(lowStockValue, String.valueOf(stock.lowStock));
            setStatValue(nearRestockValue, String.valueOf(stock.nearLowStock));
            setStatValue(inStockValue, String.valueOf(stock.inStock));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            setStatValue(outOfStockValue, "—");
            setStatValue(lowStockValue, "—");
            setStatValue(nearRestockValue, "—");
            setStatValue(inStockValue, "—");
        }
    }

    @FXML
    private void onMaterialProjectComboClick(MouseEvent e) {
        e.consume();
    }

    private static void setStatValue(Label label, String text) {
        label.setText(text);
    }

    private void loadRecentActivities() {
        recentActivitiesBox.getChildren().clear();

        try {
            List<DailyWorklogModel> recent = new DailyWorklogDao().viewRecent(DASHBOARD_LIST_LIMIT);
            if (recent.isEmpty()) {
                Label empty = new Label("No recent worklogs yet.");
                empty.getStyleClass().add("recent-activity-empty");
                recentActivitiesBox.getChildren().add(empty);
                return;
            }

            for (DailyWorklogModel worklog : recent) {
                recentActivitiesBox.getChildren().add(buildActivityRow(worklog));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Label error = new Label("Unable to load recent activities.");
            error.getStyleClass().add("recent-activity-empty");
            recentActivitiesBox.getChildren().add(error);
        }
    }

    private HBox buildActivityRow(DailyWorklogModel worklog) {
        Label projectLabel = new Label(
                worklog.getProjectName() == null ? "—" : worklog.getProjectName());
        projectLabel.getStyleClass().add("recent-activity-project");
        projectLabel.setMaxWidth(Double.MAX_VALUE);

        Label employeeLabel = new Label(worklog.getEmployeeName());
        employeeLabel.getStyleClass().add("recent-activity-members");

        VBox left = new VBox(4, projectLabel, employeeLabel);
        left.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(left, Priority.ALWAYS);
        left.setMaxWidth(Double.MAX_VALUE);

        String description = worklog.getWorkDescription();
        if (description == null || description.isBlank()) {
            description = "—";
        }
        Label workLabel = new Label(description);
        workLabel.getStyleClass().add("recent-activity-work");
        workLabel.setWrapText(true);
        workLabel.setMaxWidth(Double.MAX_VALUE);

        Label updatedLabel = new Label(formatLastUpdated(worklog.getWorkDate()));
        updatedLabel.getStyleClass().add("recent-activity-updated");

        VBox right = new VBox(4, workLabel, updatedLabel);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(right, Priority.ALWAYS);

        HBox row = new HBox(16, left, right);
        row.getStyleClass().add("recent-activity-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    private String formatLastUpdated(LocalDate workDate) {
        if (workDate == null) {
            return "Last updated: —";
        }

        long days = ChronoUnit.DAYS.between(workDate, LocalDate.now());
        if (days == 0) {
            return "Last updated: Today";
        }
        if (days == 1) {
            return "Last updated: Yesterday";
        }
        if (days > 1 && days < 7) {
            return "Last updated: " + days + " days ago";
        }
        return "Last updated: " + ACTIVITY_DATE.format(workDate);
    }

    private void loadOverdueInvoices() {
        overdueInvoicesBox.getChildren().clear();

        try {
            InvoicesDao invoicesDao = new InvoicesDao();
            List<InvoicesModel> overdue = invoicesDao.viewOverdue(DASHBOARD_LIST_LIMIT);
            double total = invoicesDao.getOverdueTotalAmount();

            if (overdue.isEmpty()) {
                Label empty = new Label("No overdue invoices.");
                empty.getStyleClass().add("overdue-empty");
                overdueInvoicesBox.getChildren().add(empty);
            } else {
                for (InvoicesModel invoice : overdue) {
                    overdueInvoicesBox.getChildren().add(buildOverdueRow(invoice));
                }
            }

            totalOverdueAmountValue.setText(AMOUNT_FORMAT.format(total));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Label error = new Label("Unable to load overdue invoices.");
            error.getStyleClass().add("overdue-empty");
            overdueInvoicesBox.getChildren().add(error);
            totalOverdueAmountValue.setText("—");
        }
    }

    private HBox buildOverdueRow(InvoicesModel invoice) {
        Label clientLabel = new Label(
                invoice.getClientName() == null || invoice.getClientName().isBlank()
                        ? "—" : invoice.getClientName());
        clientLabel.getStyleClass().add("overdue-col-client");
        clientLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(clientLabel, Priority.ALWAYS);

        Label invoiceNoLabel = new Label(String.format("INV-%02d", invoice.getId()));
        invoiceNoLabel.getStyleClass().add("overdue-col-value");
        invoiceNoLabel.setPrefWidth(110);
        invoiceNoLabel.setMinWidth(90);

        Label dueDateLabel = new Label(
                invoice.getDueDate() == null
                        ? "—"
                        : LIST_DATE.format(invoice.getDueDate()).toUpperCase());
        dueDateLabel.getStyleClass().add("overdue-col-value");
        dueDateLabel.setPrefWidth(100);
        dueDateLabel.setMinWidth(80);

        Label amountLabel = new Label(AMOUNT_FORMAT.format(invoice.getInvoiceAmount()));
        amountLabel.getStyleClass().add("overdue-col-amount");
        amountLabel.setPrefWidth(100);
        amountLabel.setMinWidth(80);
        amountLabel.setMaxWidth(100);
        amountLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(8, clientLabel, invoiceNoLabel, dueDateLabel, amountLabel);
        row.getStyleClass().add("overdue-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    private void loadUnpaidPurchases() {
        unpaidPurchasesBox.getChildren().clear();

        try {
            PurchaseDao purchaseDao = new PurchaseDao();
            List<PurchaseModel> unpaid = purchaseDao.viewUnpaid(DASHBOARD_LIST_LIMIT);
            double total = purchaseDao.getUnpaidTotalAmount();

            if (unpaid.isEmpty()) {
                Label empty = new Label("No unpaid purchases.");
                empty.getStyleClass().add("overdue-empty");
                unpaidPurchasesBox.getChildren().add(empty);
            } else {
                for (PurchaseModel purchase : unpaid) {
                    unpaidPurchasesBox.getChildren().add(buildUnpaidPurchaseRow(purchase));
                }
            }

            totalUnpaidPurchasesValue.setText(AMOUNT_FORMAT.format(total));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Label error = new Label("Unable to load unpaid purchases.");
            error.getStyleClass().add("overdue-empty");
            unpaidPurchasesBox.getChildren().add(error);
            totalUnpaidPurchasesValue.setText("—");
        }
    }

    private HBox buildUnpaidPurchaseRow(PurchaseModel purchase) {
        Label vendorLabel = new Label(
                purchase.getVendorName() == null || purchase.getVendorName().isBlank()
                        ? "—" : purchase.getVendorName());
        vendorLabel.getStyleClass().add("overdue-col-client");
        vendorLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(vendorLabel, Priority.ALWAYS);

        Label purchaseNoLabel = new Label(String.format("PUR-%02d", purchase.getId()));
        purchaseNoLabel.getStyleClass().add("overdue-col-value");
        purchaseNoLabel.setPrefWidth(110);
        purchaseNoLabel.setMinWidth(90);

        Label dateLabel = new Label(
                purchase.getPurchaseDate() == null
                        ? "—"
                        : LIST_DATE.format(purchase.getPurchaseDate()).toUpperCase());
        dateLabel.getStyleClass().add("overdue-col-value");
        dateLabel.setPrefWidth(100);
        dateLabel.setMinWidth(80);

        Label amountLabel = new Label(AMOUNT_FORMAT.format(purchase.getGrandTotal()));
        amountLabel.getStyleClass().add("overdue-col-amount");
        amountLabel.setPrefWidth(100);
        amountLabel.setMinWidth(80);
        amountLabel.setMaxWidth(100);
        amountLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(8, vendorLabel, purchaseNoLabel, dateLabel, amountLabel);
        row.getStyleClass().add("overdue-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        return row;
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

    @FXML
    private void onTotalProjectsClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Projects/ProjectsList.fxml");
    }

    @FXML
    private void onTotalClientsClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Clients/Clients.fxml");
    }

    @FXML
    private void onEmployeesStatClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Employees/Employees.fxml");
    }

    @FXML
    private void onPendingExpensesClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Expenses/Expenses.fxml");
    }

    @FXML
    private void onPendingInvoicesClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onMaterialOverviewClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Materials/Materials.fxml");
    }

    @FXML
    private void onMaterialTileClick(MouseEvent e) throws IOException {
        e.consume();
        Thalam.open((Node) e.getSource(), "/Materials/Materials.fxml");
    }

    @FXML
    private void onViewAllMaterials(ActionEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Materials/Materials.fxml");
    }

    @FXML
    private void onRecentActivitiesClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
    }

    @FXML
    private void onViewAllActivities(ActionEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
    }

    @FXML
    private void onOverdueInvoicesClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onViewAllInvoices(ActionEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onUnpaidPurchasesClick(MouseEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Materials/Purchases/Purchases.fxml");
    }

    @FXML
    private void onViewAllPurchases(ActionEvent e) throws IOException {
        Thalam.open((Node) e.getSource(), "/Materials/Purchases/Purchases.fxml");
    }
}
