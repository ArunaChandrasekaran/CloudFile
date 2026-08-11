package Projects.DailyWorklog;

import Materials.MaterialsDao;
import Materials.MaterialsModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import thalam.DetailsRefs;
import thalam.TableSearch;
import thalam.Thalam;

public class WorklogDetailsController implements Initializable {

    private static DailyWorklogModel pendingWorklog;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label projectValue;
    @FXML
    private Label dateValue;
    @FXML
    private Label employeeValue;
    @FXML
    private Label workDescriptionValue;
    @FXML
    private Label notesValue;
    @FXML
    private TableView<DailyWorklogExpenseLine> expensesTable;
    @FXML
    private TableColumn<DailyWorklogExpenseLine, String> expenseCategoryColumn;
    @FXML
    private TableColumn<DailyWorklogExpenseLine, String> expenseDescriptionColumn;
    @FXML
    private TableColumn<DailyWorklogExpenseLine, String> expenseAmountColumn;
    @FXML
    private TableView<DailyWorklogMaterialLine> materialsTable;
    @FXML
    private TableColumn<DailyWorklogMaterialLine, String> materialNameColumn;
    @FXML
    private TableColumn<DailyWorklogMaterialLine, String> materialQtyColumn;
    @FXML
    private TableColumn<DailyWorklogMaterialLine, String> materialUnitColumn;
    @FXML
    private TableColumn<DailyWorklogMaterialLine, String> materialRemarksColumn;
    @FXML
    private VBox projectRefBox;

    private DailyWorklogModel worklog;

    public static void prepare(DailyWorklogModel model) {
        pendingWorklog = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        worklog = pendingWorklog;
        pendingWorklog = null;

        setupTables();

        if (worklog == null) {
            return;
        }

        displayNameLabel.setText("WOR-" + worklog.getId());
        projectValue.setText(TableSearch.safe(worklog.getProjectName()));
        dateValue.setText(formatDate(worklog.getWorkDate()));
        employeeValue.setText(TableSearch.safe(worklog.getEmployeeName()));
        workDescriptionValue.setText(TableSearch.safe(worklog.getWorkDescription()));
        notesValue.setText(TableSearch.safe(worklog.getNotes()));

        expensesTable.setItems(FXCollections.observableArrayList(loadExpenses(worklog)));
        materialsTable.setItems(FXCollections.observableArrayList(loadMaterials(worklog)));
        fitTableHeight(expensesTable);
        fitTableHeight(materialsTable);

        loadRelated();
    }

    private void setupTables() {
        expensesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        materialsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        expenseCategoryColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(TableSearch.safe(cd.getValue().getCategory())));
        expenseDescriptionColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(TableSearch.safe(cd.getValue().getDescription())));
        expenseAmountColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(formatAmount(cd.getValue().getAmount())));

        materialNameColumn.setCellValueFactory(cd -> {
            DailyWorklogMaterialLine item = cd.getValue();
            String name = TableSearch.safe(item.getMaterialName());
            if (name.isEmpty()) {
                name = "Material #" + item.getMaterialId();
            }
            return new SimpleStringProperty(name);
        });
        materialQtyColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(formatQty(cd.getValue().getQty())));
        materialUnitColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(TableSearch.safe(cd.getValue().getUnit())));
        materialRemarksColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(TableSearch.safe(cd.getValue().getRemarks())));
    }

    private void loadRelated() {
        List<String> projectLines = new ArrayList<>();
        if (worklog.getProjectId() > 0) {
            projectLines.add("PRO-" + worklog.getProjectId() + " · "
                    + TableSearch.safe(worklog.getProjectName()));
        }
        DetailsRefs.fill(projectRefBox,
                projectLines.isEmpty() ? Collections.emptyList() : projectLines);
    }

    private List<DailyWorklogExpenseLine> loadExpenses(DailyWorklogModel model) {
        List<DailyWorklogExpenseLine> lines = model.getExpenses();
        if (lines == null || lines.isEmpty()) {
            try {
                lines = new DailyWorklogDao().viewExpenses(model.getId());
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(WorklogDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                return Collections.emptyList();
            }
        }
        return lines == null ? Collections.emptyList() : lines;
    }

    private List<DailyWorklogMaterialLine> loadMaterials(DailyWorklogModel model) {
        List<DailyWorklogMaterialLine> lines = model.getMaterials();
        if (lines == null || lines.isEmpty()) {
            try {
                lines = new DailyWorklogDao().viewMaterials(model.getId());
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(WorklogDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                return Collections.emptyList();
            }
        }
        if (lines == null || lines.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, String> namesById = loadMaterialNames();
        for (DailyWorklogMaterialLine line : lines) {
            if (TableSearch.safe(line.getMaterialName()).isEmpty()) {
                line.setMaterialName(namesById.getOrDefault(
                        line.getMaterialId(), "Material #" + line.getMaterialId()));
            }
        }
        return lines;
    }

    private static Map<Integer, String> loadMaterialNames() {
        Map<Integer, String> map = new HashMap<>();
        try {
            for (MaterialsModel material : new MaterialsDao().view()) {
                map.put(material.getId(), material.getName());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(WorklogDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    private static void fitTableHeight(TableView<?> table) {
        int rows = Math.max(table.getItems().size(), 1);
        double header = 36;
        double row = 34;
        table.setPrefHeight(header + (rows * row) + 2);
        table.setFixedCellSize(row);
    }

    private static String formatDate(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    private static String formatAmount(double amount) {
        return String.format("₹ %.2f", amount);
    }

    private static String formatQty(double qty) {
        if (qty == (long) qty) {
            return String.valueOf((long) qty);
        }
        return String.valueOf(qty);
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
        Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddWorklogController.prepareEdit(worklog);
        Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/AddWorklog.fxml");
    }

    @FXML
    private void onDeleteWorklog(ActionEvent event) throws IOException {
        if (worklog == null) {
            return;
        }

        String label = firstNonBlank(
                worklog.getProjectName(),
                formatDate(worklog.getWorkDate()),
                "#" + worklog.getId());
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Worklog");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete worklog \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new DailyWorklogService().deleteService(worklog.getId());
            Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Worklog");
            error.setHeaderText(null);
            error.setContentText("Could not delete worklog.\n" + ex.getMessage());
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
