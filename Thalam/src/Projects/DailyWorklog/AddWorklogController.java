package Projects.DailyWorklog;

import Employees.EmployeesModel;
import Expenses.ExpensesDao;
import Materials.MaterialsDao;
import Materials.MaterialsModel;
import Projects.ProjectDao;
import Projects.ProjectsModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import thalam.Thalam;

public class AddWorklogController implements Initializable {

    private static final String ADD_CATEGORY = "+ Add Category";
    private static final int INITIAL_EXPENSE_ROWS = 4;
    private static final int INITIAL_MATERIAL_ROWS = 4;

    private static DailyWorklogModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private ComboBox<ProjectsModel> projectCombo;
    @FXML
    private DatePicker workDatePicker;
    @FXML
    private ComboBox<EmployeesModel> employeeCombo;
    @FXML
    private TextArea todayWorkField;
    @FXML
    private VBox expenseRowsBox;
    @FXML
    private Label expenseTotalLabel;
    @FXML
    private VBox materialRowsBox;

    private final ProjectDao projectDao = new ProjectDao();
    private final List<ExpenseRow> expenseRows = new ArrayList<>();
    private final List<MaterialRow> materialRows = new ArrayList<>();
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<MaterialsModel> materials = FXCollections.observableArrayList();
    private DailyWorklogModel editingWorklog;

    public static void prepareEdit(DailyWorklogModel worklog) {
        pendingEdit = worklog;
    }

    private static class ExpenseRow {
        final GridPane pane;
        final ComboBox<String> categoryCombo;
        final TextField descriptionField;
        final TextField amountField;

        ExpenseRow(
                GridPane pane,
                ComboBox<String> categoryCombo,
                TextField descriptionField,
                TextField amountField) {
            this.pane = pane;
            this.categoryCombo = categoryCombo;
            this.descriptionField = descriptionField;
            this.amountField = amountField;
        }
    }

    private static class MaterialRow {
        final GridPane pane;
        final ComboBox<MaterialsModel> materialCombo;
        final TextField unitField;
        final TextField qtyField;
        final TextField remarksField;

        MaterialRow(
                GridPane pane,
                ComboBox<MaterialsModel> materialCombo,
                TextField unitField,
                TextField qtyField,
                TextField remarksField) {
            this.pane = pane;
            this.materialCombo = materialCombo;
            this.unitField = unitField;
            this.qtyField = qtyField;
            this.remarksField = remarksField;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingWorklog = pendingEdit;
        pendingEdit = null;

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

        employeeCombo.setConverter(new StringConverter<EmployeesModel>() {
            @Override
            public String toString(EmployeesModel employee) {
                return employee == null ? "" : employee.getName();
            }

            @Override
            public EmployeesModel fromString(String string) {
                return null;
            }
        });

        try {
            projectCombo.setItems(FXCollections.observableArrayList(projectDao.view()));
            materials = FXCollections.observableArrayList(new MaterialsDao().view());
            categories = FXCollections.observableArrayList(
                    new ExpensesDao().viewCategoriesByType("Project Expense"));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddWorklogController.class.getName()).log(Level.SEVERE, null, ex);
        }

        projectCombo.valueProperty().addListener((obs, oldVal, selected) -> {
            Integer keepEmployeeId = null;
            if (editingWorklog != null
                    && selected != null
                    && selected.getId() == editingWorklog.getProjectId()) {
                keepEmployeeId = editingWorklog.getEmployeeId();
            } else {
                employeeCombo.getSelectionModel().clearSelection();
            }
            if (selected == null) {
                employeeCombo.setItems(FXCollections.observableArrayList());
                return;
            }
            try {
                ObservableList<EmployeesModel> employees = FXCollections.observableArrayList(
                        projectDao.viewAssociatedEmployees(selected.getId()));
                employeeCombo.setItems(employees);
                if (keepEmployeeId != null) {
                    for (EmployeesModel emp : employees) {
                        if (emp.getId() != null && emp.getId().equals(keepEmployeeId)) {
                            employeeCombo.setValue(emp);
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(AddWorklogController.class.getName()).log(Level.SEVERE, null, ex);
                employeeCombo.setItems(FXCollections.observableArrayList());
            }
        });

        if (editingWorklog != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Worklog");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the daily worklog details.");
            }
            applyEditValues();
        } else {
            for (int i = 0; i < INITIAL_EXPENSE_ROWS; i++) {
                addExpenseRow();
            }
            for (int i = 0; i < INITIAL_MATERIAL_ROWS; i++) {
                addMaterialRow();
            }
        }
        updateExpenseTotal();
    }

    private void applyEditValues() {
        workDatePicker.setValue(editingWorklog.getWorkDate());
        todayWorkField.setText(editingWorklog.getWorkDescription());

        for (ProjectsModel project : projectCombo.getItems()) {
            if (project.getId() == editingWorklog.getProjectId()) {
                projectCombo.setValue(project);
                break;
            }
        }

        List<DailyWorklogExpenseLine> expenseLines = new ArrayList<>();
        List<DailyWorklogMaterialLine> materialLines = new ArrayList<>();
        try {
            DailyWorklogDao dao = new DailyWorklogDao();
            expenseLines = dao.viewExpenses(editingWorklog.getId());
            materialLines = dao.viewMaterials(editingWorklog.getId());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddWorklogController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (DailyWorklogExpenseLine line : expenseLines) {
            if (line.getCategory() != null
                    && !line.getCategory().isBlank()
                    && !categories.contains(line.getCategory())) {
                categories.add(line.getCategory());
            }
        }

        int expenseRowsNeeded = Math.max(INITIAL_EXPENSE_ROWS, expenseLines.size());
        for (int i = 0; i < expenseRowsNeeded; i++) {
            addExpenseRow();
        }
        for (int i = 0; i < expenseLines.size() && i < expenseRows.size(); i++) {
            DailyWorklogExpenseLine line = expenseLines.get(i);
            ExpenseRow row = expenseRows.get(i);
            row.categoryCombo.setValue(line.getCategory());
            row.descriptionField.setText(line.getDescription());
            if (line.getAmount() != 0) {
                row.amountField.setText(String.format("%.2f", line.getAmount()));
            }
        }

        int materialRowsNeeded = Math.max(INITIAL_MATERIAL_ROWS, materialLines.size());
        for (int i = 0; i < materialRowsNeeded; i++) {
            addMaterialRow();
        }
        for (int i = 0; i < materialLines.size() && i < materialRows.size(); i++) {
            DailyWorklogMaterialLine line = materialLines.get(i);
            MaterialRow row = materialRows.get(i);
            for (MaterialsModel material : materials) {
                if (material.getId() == line.getMaterialId()) {
                    row.materialCombo.setValue(material);
                    break;
                }
            }
            row.unitField.setText(line.getUnit() == null ? "" : line.getUnit());
            if (line.getQty() != 0) {
                if (line.getQty() == (long) line.getQty()) {
                    row.qtyField.setText(String.valueOf((long) line.getQty()));
                } else {
                    row.qtyField.setText(String.valueOf(line.getQty()));
                }
            }
            row.remarksField.setText(line.getRemarks() == null ? "" : line.getRemarks());
        }
    }

    @FXML
    private void onAddExpenseRow(ActionEvent event) {
        addExpenseRow();
    }

    private void addExpenseRow() {
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.setPromptText("Select category");
        categoryCombo.getStyleClass().add("module-form-bulk-combo");
        categoryCombo.setMaxWidth(Double.MAX_VALUE);
        categoryCombo.setEditable(false);
        applyCategoryItems(categoryCombo);
        styleActionCell(categoryCombo, ADD_CATEGORY);

        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (ADD_CATEGORY.equals(newVal)) {
                Platform.runLater(() -> {
                    categoryCombo.getSelectionModel().clearSelection();
                    if (oldVal != null && !ADD_CATEGORY.equals(oldVal)) {
                        categoryCombo.setValue(oldVal);
                    }
                    promptAddCategory(categoryCombo);
                });
            }
        });

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        descriptionField.getStyleClass().add("module-form-bulk-input");

        TextField amountField = new TextField();
        amountField.setPromptText("0.00");
        amountField.getStyleClass().add("module-form-bulk-input");
        amountField.textProperty().addListener((obs, o, n) -> updateExpenseTotal());

        Button removeButton = new Button("✕");
        removeButton.getStyleClass().add("module-form-bulk-remove");
        removeButton.setFocusTraversable(false);

        GridPane row = new GridPane();
        row.getStyleClass().add("module-form-bulk-row");
        row.setHgap(8);
        row.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c0 = percentCol(30);
        ColumnConstraints c1 = percentCol(38);
        ColumnConstraints c2 = percentCol(24);
        ColumnConstraints c3 = percentCol(8);
        c3.setHgrow(Priority.NEVER);
        c3.setHalignment(HPos.CENTER);
        row.getColumnConstraints().addAll(c0, c1, c2, c3);
        GridPane.setHalignment(removeButton, HPos.CENTER);
        row.add(categoryCombo, 0, 0);
        row.add(descriptionField, 1, 0);
        row.add(amountField, 2, 0);
        row.add(removeButton, 3, 0);

        ExpenseRow expenseRow = new ExpenseRow(row, categoryCombo, descriptionField, amountField);
        removeButton.setOnAction(e -> removeExpenseRow(expenseRow));

        expenseRowsBox.getChildren().add(row);
        expenseRows.add(expenseRow);
    }

    private void removeExpenseRow(ExpenseRow row) {
        if (expenseRows.size() <= 1) {
            row.categoryCombo.setValue(null);
            row.descriptionField.clear();
            row.amountField.clear();
            updateExpenseTotal();
            return;
        }
        expenseRowsBox.getChildren().remove(row.pane);
        expenseRows.remove(row);
        updateExpenseTotal();
    }

    private void applyCategoryItems(ComboBox<String> combo) {
        ObservableList<String> items = FXCollections.observableArrayList(categories);
        items.add(ADD_CATEGORY);
        String selected = combo.getValue();
        combo.setItems(items);
        if (selected != null && !ADD_CATEGORY.equals(selected) && categories.contains(selected)) {
            combo.setValue(selected);
        }
    }

    private void refreshAllCategoryCombos() {
        for (ExpenseRow row : expenseRows) {
            applyCategoryItems(row.categoryCombo);
        }
    }

    private void styleActionCell(ComboBox<String> combo, String actionItem) {
        combo.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                if (actionItem.equals(item)) {
                    setStyle("-fx-text-fill: #F57C1F; -fx-font-weight: 700;");
                } else {
                    setStyle("");
                }
            }
        });
        combo.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || actionItem.equals(item)) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });
    }

    private void promptAddCategory(ComboBox<String> targetCombo) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Category name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        String name = result.get().trim();
        if (name.isEmpty() || ADD_CATEGORY.equals(name) || categories.contains(name)) {
            if (categories.contains(name)) {
                targetCombo.setValue(name);
            }
            return;
        }
        categories.add(name);
        refreshAllCategoryCombos();
        targetCombo.setValue(name);
    }

    @FXML
    private void onAddMaterialUsageRow(ActionEvent event) {
        addMaterialRow();
    }

    private void addMaterialRow() {
        ComboBox<MaterialsModel> materialCombo = new ComboBox<>();
        materialCombo.setPromptText("Select material");
        materialCombo.getStyleClass().add("module-form-bulk-combo");
        materialCombo.setMaxWidth(Double.MAX_VALUE);
        materialCombo.setItems(materials);
        materialCombo.setConverter(new StringConverter<MaterialsModel>() {
            @Override
            public String toString(MaterialsModel material) {
                return material == null ? "" : material.getName();
            }

            @Override
            public MaterialsModel fromString(String string) {
                return null;
            }
        });

        TextField unitField = new TextField();
        unitField.setPromptText("Unit");
        unitField.getStyleClass().add("module-form-bulk-input");

        TextField qtyField = new TextField();
        qtyField.setPromptText("Qty");
        qtyField.getStyleClass().add("module-form-bulk-input");

        TextField remarksField = new TextField();
        remarksField.setPromptText("Remarks");
        remarksField.getStyleClass().add("module-form-bulk-input");

        materialCombo.valueProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                unitField.setText(selected.getUnit() == null ? "" : selected.getUnit());
            }
        });

        Button removeButton = new Button("✕");
        removeButton.getStyleClass().add("module-form-bulk-remove");
        removeButton.setFocusTraversable(false);

        GridPane row = new GridPane();
        row.getStyleClass().add("module-form-bulk-row");
        row.setHgap(8);
        row.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c0 = percentCol(30);
        ColumnConstraints c1 = percentCol(16);
        ColumnConstraints c2 = percentCol(16);
        ColumnConstraints c3 = percentCol(30);
        ColumnConstraints c4 = percentCol(8);
        c4.setHgrow(Priority.NEVER);
        c4.setHalignment(HPos.CENTER);
        row.getColumnConstraints().addAll(c0, c1, c2, c3, c4);
        GridPane.setHalignment(removeButton, HPos.CENTER);
        row.add(materialCombo, 0, 0);
        row.add(unitField, 1, 0);
        row.add(qtyField, 2, 0);
        row.add(remarksField, 3, 0);
        row.add(removeButton, 4, 0);

        MaterialRow materialRow = new MaterialRow(row, materialCombo, unitField, qtyField, remarksField);
        removeButton.setOnAction(e -> removeMaterialRow(materialRow));

        materialRowsBox.getChildren().add(row);
        materialRows.add(materialRow);
    }

    private void removeMaterialRow(MaterialRow row) {
        if (materialRows.size() <= 1) {
            row.materialCombo.setValue(null);
            row.unitField.clear();
            row.qtyField.clear();
            row.remarksField.clear();
            return;
        }
        materialRowsBox.getChildren().remove(row.pane);
        materialRows.remove(row);
    }

    private ColumnConstraints percentCol(double percent) {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(percent);
        c.setHgrow(Priority.ALWAYS);
        return c;
    }

    private void updateExpenseTotal() {
        double total = 0;
        for (ExpenseRow row : expenseRows) {
            total += parseNumber(row.amountField.getText());
        }
        expenseTotalLabel.setText(total == 0 ? "—" : String.format("%.2f", total));
    }

    private double parseNumber(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        try {
            return Double.parseDouble(text.replace(",", "").trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        ProjectsModel project = projectCombo.getValue();
        LocalDate workDate = workDatePicker.getValue();
        EmployeesModel employee = employeeCombo.getValue();
        String description = todayWorkField.getText() == null ? "" : todayWorkField.getText().trim();

        if (project == null || workDate == null || description.isEmpty()) {
            showError("Please fill project, work date and today's work description.");
            return;
        }

        List<DailyWorklogExpenseLine> expenses = new ArrayList<>();
        for (ExpenseRow row : expenseRows) {
            String category = row.categoryCombo.getValue();
            if (category != null) {
                category = category.trim();
            }
            if (ADD_CATEGORY.equals(category)) {
                category = null;
            }
            String desc = row.descriptionField.getText() == null
                    ? "" : row.descriptionField.getText().trim();
            double amount = parseNumber(row.amountField.getText());

            if ((category == null || category.isEmpty()) && desc.isEmpty() && amount == 0) {
                continue;
            }
            if (category == null || category.isEmpty() || amount <= 0) {
                showError("Each expense row needs category and amount greater than 0.");
                return;
            }
            expenses.add(new DailyWorklogExpenseLine(category, desc, amount));
        }

        List<DailyWorklogMaterialLine> materialsList = new ArrayList<>();
        for (MaterialRow row : materialRows) {
            MaterialsModel material = row.materialCombo.getValue();
            double qty = parseNumber(row.qtyField.getText());
            String unit = row.unitField.getText() == null ? "" : row.unitField.getText().trim();
            String remarks = row.remarksField.getText() == null ? "" : row.remarksField.getText().trim();

            if (material == null && qty == 0 && unit.isEmpty() && remarks.isEmpty()) {
                continue;
            }
            if (material == null || qty <= 0) {
                showError("Each material row needs a material and qty greater than 0.");
                return;
            }
            materialsList.add(new DailyWorklogMaterialLine(
                    material.getId(), unit, qty, remarks));
        }

        DailyWorklogModel model = new DailyWorklogModel();
        model.setProjectId(project.getId());
        model.setWorkDate(workDate);
        if (employee != null && employee.getId() != null) {
            model.setEmployeeId(employee.getId());
        }
        model.setWorkDescription(description);
        model.setNotes(editingWorklog != null ? editingWorklog.getNotes() : "");
        model.setExpenses(expenses);
        model.setMaterials(materialsList);

        try {
            DailyWorklogService service = new DailyWorklogService();
            if (editingWorklog != null) {
                model.setId(editingWorklog.getId());
                service.updateService(model);
            } else {
                service.insertService(model);
            }
            Thalam.open((Node) event.getSource(), "/Projects/DailyWorklog/DailyWorklog.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddWorklogController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Could not save worklog.\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Worklog");
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
