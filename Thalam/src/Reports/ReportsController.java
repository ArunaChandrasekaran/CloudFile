package Reports;

import Projects.ProjectDao;
import Projects.ProjectsModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import thalam.Thalam;

public class ReportsController implements Initializable {

    private static final double FIXED_CHART_HEIGHT = 250;
    /** Extra empty categories keep single-month bars from filling the plot width. */
    private static final int BAR_SLOT_COUNT = 5;

    @FXML
    private ComboBox<ProjectsModel> projectCombo;
    @FXML
    private ComboBox<String> dateRangeCombo;
    @FXML
    private StackPane projectProgressHost;
    @FXML
    private StackPane expensesChartHost;
    @FXML
    private StackPane invoicesChartHost;
    @FXML
    private StackPane profitLossChartHost;
    @FXML
    private StackPane purchasesChartHost;
    @FXML
    private Label budgetSpendEmptyLabel;
    @FXML
    private VBox budgetSpendContent;
    @FXML
    private Label budgetValueLabel;
    @FXML
    private Label spendValueLabel;
    @FXML
    private Label remainingValueLabel;
    @FXML
    private ProgressBar budgetSpendProgress;
    @FXML
    private Label budgetSpendPercentLabel;

    private final ReportsDao reportsDao = new ReportsDao();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupProjectCombo();
        if (dateRangeCombo.getValue() == null) {
            dateRangeCombo.setValue("This Month");
        }
        refreshCharts();
    }

    private void setupProjectCombo() {
        ProjectsModel all = new ProjectsModel();
        all.setId(0);
        all.setProjectName("All Projects");

        projectCombo.setConverter(new StringConverter<ProjectsModel>() {
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
            projectCombo.setItems(FXCollections.observableArrayList());
            projectCombo.getItems().add(all);
            projectCombo.getItems().addAll(projects);
            projectCombo.setValue(all);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            projectCombo.setItems(FXCollections.observableArrayList(all));
            projectCombo.setValue(all);
        }
    }

    @FXML
    private void onApplyReport(ActionEvent event) {
        refreshCharts();
    }

    private void refreshCharts() {
        Integer projectId = selectedProjectId();
        LocalDate[] range = resolveDateRange(dateRangeCombo.getValue());
        LocalDate from = range[0];
        LocalDate to = range[1];

        try {
            loadProjectProgressChart(projectId, from, to);
            loadExpensesChart(projectId, from, to);
            loadInvoicesChart(projectId, from, to);
            loadProfitLossChart(projectId, from, to);
            loadPurchasesChart(projectId, from, to);
            loadBudgetSpendChart(projectId, from, to);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadProjectProgressChart(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        ReportsDao.ProjectProgress progress = reportsDao.projectProgress(projectId);
        if (progress.plannedDays <= 0) {
            showPlaceholder(projectProgressHost,
                    projectId == null
                            ? "No projects with start/end dates"
                            : "Set project start and end dates");
            return;
        }

        double pct = Math.max(0, Math.min(100, progress.percent));
        Label percentLabel = new Label(String.format("%.0f%%", pct));
        percentLabel.getStyleClass().add("reports-progress-percent");

        ProgressBar bar = new ProgressBar(pct / 100.0);
        bar.getStyleClass().add("reports-progress-bar");
        bar.setMaxWidth(Double.MAX_VALUE);

        String scope = projectId == null ? "All projects (avg)" : "Selected project";
        Label detail = new Label(
                scope + " · " + progress.worklogCount + " worklogs / "
                        + progress.plannedDays + " days");
        detail.getStyleClass().add("reports-progress-detail");
        detail.setWrapText(true);

        VBox box = new VBox(12, percentLabel, bar, detail);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(24, 28, 24, 28));
        box.getStyleClass().add("reports-progress-box");
        VBox.setVgrow(bar, Priority.NEVER);
        projectProgressHost.getChildren().setAll(box);
    }

    private Integer selectedProjectId() {
        ProjectsModel selected = projectCombo.getValue();
        if (selected == null || selected.getId() <= 0) {
            return null;
        }
        return selected.getId();
    }

    private LocalDate[] resolveDateRange(String label) {
        LocalDate today = LocalDate.now();
        LocalDate from;
        if ("Last 3 Months".equals(label)) {
            from = today.minusMonths(3).withDayOfMonth(1);
        } else if ("Last 6 Months".equals(label)) {
            from = today.minusMonths(6).withDayOfMonth(1);
        } else if ("Last 1 Year".equals(label)) {
            from = today.minusYears(1).withDayOfMonth(1);
        } else {
            from = today.withDayOfMonth(1);
        }
        return new LocalDate[]{from, today};
    }

    private void loadExpensesChart(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        List<ReportsDao.NamedAmount> rows = reportsDao.expensesByCategory(projectId, from, to);
        if (rows.isEmpty()) {
            showPlaceholder(expensesChartHost, "No expense data");
            return;
        }
        PieChart chart = createPieChart();
        for (ReportsDao.NamedAmount row : rows) {
            chart.getData().add(new PieChart.Data(row.name + " (" + formatAmount(row.amount) + ")", row.amount));
        }
        setChart(expensesChartHost, chart);
    }

    private void loadInvoicesChart(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        List<ReportsDao.NamedAmount> rows = reportsDao.invoicesByMonth(projectId, from, to);
        if (rows.isEmpty()) {
            showPlaceholder(invoicesChartHost, "No invoice data");
            return;
        }
        BarChart<String, Number> chart = createBarChart("Amount");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Invoices");
        fillMonthSeries(chart, series, rows);
        chart.getData().add(series);
        attachXyTooltips(chart);
        setChart(invoicesChartHost, chart);
    }

    private void loadProfitLossChart(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        double income = reportsDao.totalInvoiceAmount(projectId, from, to);
        double expense = reportsDao.totalExpenseAmount(projectId, from, to);
        double profit = income - expense;

        if (income <= 0 && expense <= 0) {
            showPlaceholder(profitLossChartHost, "No profit & loss data");
            return;
        }

        PieChart chart = createPieChart();
        if (income > 0) {
            chart.getData().add(new PieChart.Data("Income (" + formatAmount(income) + ")", income));
        }
        if (expense > 0) {
            chart.getData().add(new PieChart.Data("Expense (" + formatAmount(expense) + ")", expense));
        }

        Label netLabel = new Label(profit >= 0
                ? "Net Profit: " + formatAmount(profit)
                : "Net Loss: " + formatAmount(Math.abs(profit)));
        netLabel.getStyleClass().add("reports-pl-net-label");
        netLabel.setMouseTransparent(true);

        chart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane layout = new StackPane(chart, netLabel);
        StackPane.setAlignment(netLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(netLabel, new Insets(6, 10, 0, 0));
        setChart(profitLossChartHost, layout);
    }

    private void loadPurchasesChart(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        List<ReportsDao.NamedAmount> rows = reportsDao.purchasesByMonth(projectId, from, to);
        if (rows.isEmpty()) {
            showPlaceholder(purchasesChartHost, "No purchase data");
            return;
        }
        BarChart<String, Number> chart = createBarChart("Amount");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Purchases");
        fillMonthSeries(chart, series, rows);
        chart.getData().add(series);
        attachXyTooltips(chart);
        setChart(purchasesChartHost, chart);
    }

    private void loadBudgetSpendChart(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        double budget = reportsDao.contractBudget(projectId);
        double spend = reportsDao.totalExpenseAmount(projectId, from, to);

        if (budget <= 0 && spend <= 0) {
            budgetSpendContent.setVisible(false);
            budgetSpendContent.setManaged(false);
            budgetSpendEmptyLabel.setVisible(true);
            budgetSpendEmptyLabel.setManaged(true);
            return;
        }

        double remaining = Math.max(0, budget - spend);
        double pct = budget > 0 ? Math.min(100.0, (spend * 100.0) / budget) : 0;

        budgetValueLabel.setText(formatAmount(budget));
        spendValueLabel.setText(formatAmount(spend));
        remainingValueLabel.setText(formatAmount(remaining));
        budgetSpendProgress.setProgress(pct / 100.0);
        budgetSpendPercentLabel.setText(String.format("%.2f%% of budget used", pct));

        budgetSpendEmptyLabel.setVisible(false);
        budgetSpendEmptyLabel.setManaged(false);
        budgetSpendContent.setVisible(true);
        budgetSpendContent.setManaged(true);
    }

    /**
     * Puts real months first, then empty spacer categories so few months stay narrow
     * without pushing the first bar away from the Y-axis.
     */
    private void fillMonthSeries(
            BarChart<String, Number> chart,
            XYChart.Series<String, Number> series,
            List<ReportsDao.NamedAmount> rows) {
        List<String> categories = new ArrayList<>();
        for (ReportsDao.NamedAmount row : rows) {
            categories.add(row.name);
        }
        int pad = Math.max(0, BAR_SLOT_COUNT - rows.size());
        for (int i = 0; i < pad; i++) {
            categories.add(spacerCategory(i));
        }

        CategoryAxis xAxis = (CategoryAxis) chart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList(categories));

        for (ReportsDao.NamedAmount row : rows) {
            series.getData().add(new XYChart.Data<>(row.name, row.amount));
        }
        for (int i = 0; i < pad; i++) {
            series.getData().add(new XYChart.Data<>(spacerCategory(i), 0));
        }
    }

    private static String spacerCategory(int index) {
        StringBuilder sb = new StringBuilder(index + 1);
        for (int i = 0; i < index + 1; i++) {
            sb.append('\u200B');
        }
        return sb.toString();
    }

    private static boolean isSpacerCategory(String category) {
        if (category == null || category.isEmpty()) {
            return false;
        }
        for (int i = 0; i < category.length(); i++) {
            if (category.charAt(i) != '\u200B') {
                return false;
            }
        }
        return true;
    }

    private PieChart createPieChart() {
        PieChart chart = new PieChart();
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.RIGHT);
        chart.setLabelsVisible(false);
        chart.setAnimated(false);
        return chart;
    }

    private BarChart<String, Number> createBarChart(String yLabel) {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        if (yLabel != null) {
            y.setLabel(yLabel);
        }
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        return chart;
    }

    private void attachXyTooltips(XYChart<?, ?> chart) {
        for (XYChart.Series<?, ?> series : chart.getData()) {
            String seriesName = series.getName();
            for (XYChart.Data<?, ?> data : series.getData()) {
                if (data.getXValue() instanceof String
                        && isSpacerCategory((String) data.getXValue())) {
                    continue;
                }
                installXyTooltip(data, seriesName);
            }
        }
    }

    private void installXyTooltip(XYChart.Data<?, ?> data, String seriesName) {
        Runnable install = () -> {
            Node node = data.getNode();
            if (node == null || !node.isVisible()) {
                return;
            }
            String category = resolveCategoryLabel(data);
            double value = resolveNumericValue(data);
            String seriesPart = (seriesName == null || seriesName.isBlank()) ? "" : seriesName + " · ";
            Tooltip tip = new Tooltip(seriesPart + category + ": " + formatAmount(value));
            tip.setShowDelay(Duration.millis(120));
            tip.setHideDelay(Duration.millis(80));
            tip.getStyleClass().add("reports-chart-tooltip");
            Tooltip.install(node, tip);
        };

        if (data.getNode() != null) {
            install.run();
        } else {
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    install.run();
                }
            });
        }
    }

    private static String resolveCategoryLabel(XYChart.Data<?, ?> data) {
        Object x = data.getXValue();
        Object y = data.getYValue();
        if (x instanceof String) {
            return (String) x;
        }
        if (y instanceof String) {
            return (String) y;
        }
        return "";
    }

    private static double resolveNumericValue(XYChart.Data<?, ?> data) {
        Object x = data.getXValue();
        Object y = data.getYValue();
        if (y instanceof Number) {
            return ((Number) y).doubleValue();
        }
        if (x instanceof Number) {
            return ((Number) x).doubleValue();
        }
        return 0;
    }

    private void setChart(StackPane host, Node chart) {
        chart.getStyleClass().add("reports-live-chart");
        if (chart instanceof Chart) {
            sizeChartNode((Chart) chart);
        } else if (chart instanceof StackPane) {
            StackPane wrap = (StackPane) chart;
            wrap.setMinHeight(FIXED_CHART_HEIGHT);
            wrap.setPrefHeight(FIXED_CHART_HEIGHT);
            wrap.setMaxHeight(FIXED_CHART_HEIGHT);
            wrap.setMaxWidth(Double.MAX_VALUE);
            for (Node child : wrap.getChildren()) {
                if (child instanceof Chart) {
                    sizeChartNode((Chart) child);
                }
            }
        }
        host.getChildren().setAll(chart);
    }

    private void sizeChartNode(Chart chart) {
        chart.setMinHeight(FIXED_CHART_HEIGHT);
        chart.setPrefHeight(FIXED_CHART_HEIGHT);
        chart.setMaxHeight(FIXED_CHART_HEIGHT);
        chart.setMaxWidth(Double.MAX_VALUE);
    }

    private void showPlaceholder(StackPane host, String message) {
        Label label = new Label(message);
        label.getStyleClass().add("reports-chart-placeholder-label");
        StackPane.setAlignment(label, Pos.CENTER);
        host.getChildren().setAll(label);
    }

    private static String formatAmount(double amount) {
        if (Math.rint(amount) == amount) {
            return String.format("%,d", (long) amount);
        }
        return String.format("%,.0f", amount);
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
