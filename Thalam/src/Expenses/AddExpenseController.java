package Expenses;

import Projects.ProjectDao;
import Projects.ProjectsModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import thalam.Thalam;

public class AddExpenseController implements Initializable {

    private static final String ADD_CATEGORY = "+ Add Category";
    private static final String PROJECT_EXPENSE = "Project Expense";
    private static final String COMPANY_EXPENSE = "Company Expense";

    private static ExpensesModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> expenseTypeCombo;
    @FXML
    private VBox conditionalLinkHost;
    @FXML
    private VBox projectSection;
    @FXML
    private ComboBox<ProjectsModel> projectCombo;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private TextField amountField;
    @FXML
    private CheckBox markAsPaidCheck;
    @FXML
    private VBox paymentModeSection;
    @FXML
    private ComboBox<String> paymentModeCombo;
    @FXML
    private TextArea notesField;

    private final ObservableList<String> categories = FXCollections.observableArrayList();
    private ExpensesModel editingExpense;

    public static void prepareEdit(ExpensesModel expense) {
        pendingEdit = expense;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingExpense = pendingEdit;
        pendingEdit = null;

        expenseTypeCombo.setItems(FXCollections.observableArrayList(
                PROJECT_EXPENSE, COMPANY_EXPENSE));

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
            ProjectDao projectDao = new ProjectDao();
            projectCombo.setItems(FXCollections.observableArrayList(projectDao.view()));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddExpenseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        expenseTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateConditionalSections(newVal);
            loadCategoriesForType(newVal);
        });

        refreshCategoryItems();
        styleActionCell(categoryCombo, ADD_CATEGORY);

        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (ADD_CATEGORY.equals(newVal)) {
                Platform.runLater(() -> {
                    categoryCombo.getSelectionModel().clearSelection();
                    if (oldVal != null && !ADD_CATEGORY.equals(oldVal)) {
                        categoryCombo.setValue(oldVal);
                    }
                    promptAddCategory();
                });
            }
        });

        paymentModeCombo.setItems(FXCollections.observableArrayList(
                "Cash", "UPI", "Bank Transfer", "Cheque", "Credit"));

        if (markAsPaidCheck != null) {
            markAsPaidCheck.selectedProperty().addListener((obs, was, isPaid) -> {
                paymentModeSection.setVisible(isPaid);
                paymentModeSection.setManaged(isPaid);
                if (!isPaid) {
                    paymentModeCombo.getSelectionModel().clearSelection();
                }
            });
        }

        if (editingExpense != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Expense");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the expense details.");
            }
            prefillForm(editingExpense);
        }
    }

    private void prefillForm(ExpensesModel expense) {
        datePicker.setValue(expense.getExpenseDate());
        expenseTypeCombo.setValue(expense.getExpenseType());
        updateConditionalSections(expense.getExpenseType());

        if (expense.getProjectId() != null && expense.getProjectId() > 0
                && projectCombo.getItems() != null) {
            for (ProjectsModel project : projectCombo.getItems()) {
                if (project != null && project.getId() == expense.getProjectId()) {
                    projectCombo.setValue(project);
                    break;
                }
            }
        }

        if (expense.getCategory() != null && !expense.getCategory().isBlank()) {
            if (!categories.contains(expense.getCategory())) {
                categories.add(expense.getCategory());
                refreshCategoryItems();
            }
            categoryCombo.setValue(expense.getCategory());
        }

        amountField.setText(String.valueOf(expense.getAmount()));
        markAsPaidCheck.setSelected(expense.isPaid());
        paymentModeSection.setVisible(expense.isPaid());
        paymentModeSection.setManaged(expense.isPaid());
        if (expense.isPaid() && expense.getPaymentMode() != null) {
            paymentModeCombo.setValue(expense.getPaymentMode());
        }
        notesField.setText(expense.getNotes() == null ? "" : expense.getNotes());
    }

    private void loadCategoriesForType(String expenseType) {
        String previous = categoryCombo.getValue();
        if (ADD_CATEGORY.equals(previous)) {
            previous = null;
        }
        categories.clear();
        if (expenseType != null && !expenseType.isBlank()) {
            try {
                categories.addAll(new ExpensesDao().viewCategoriesByType(expenseType));
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(AddExpenseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        refreshCategoryItems();
        if (previous != null && categories.contains(previous)) {
            categoryCombo.setValue(previous);
        } else {
            categoryCombo.getSelectionModel().clearSelection();
        }
    }

    private void refreshCategoryItems() {
        ObservableList<String> items = FXCollections.observableArrayList(categories);
        items.add(ADD_CATEGORY);
        categoryCombo.setItems(items);
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

    private void updateConditionalSections(String expenseType) {
        boolean showProject = PROJECT_EXPENSE.equals(expenseType);

        conditionalLinkHost.setVisible(showProject);
        conditionalLinkHost.setManaged(showProject);

        projectSection.setVisible(showProject);
        projectSection.setManaged(showProject);

        if (!showProject) {
            projectCombo.getSelectionModel().clearSelection();
        }
    }

    private void promptAddCategory() {
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
            return;
        }
        categories.add(name);
        refreshCategoryItems();
        categoryCombo.setValue(name);
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Expenses/Expenses.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        LocalDate expenseDate = datePicker.getValue();
        String expenseType = expenseTypeCombo.getValue();
        ProjectsModel selectedProject = projectCombo.getValue();
        String category = categoryCombo.getValue();
        if (ADD_CATEGORY.equals(category)) {
            category = null;
        }
        boolean paid = markAsPaidCheck.isSelected();
        String paymentMode = paymentModeCombo.getValue();
        String notes = notesField.getText() == null ? "" : notesField.getText().trim();

        if (expenseDate == null || expenseType == null || expenseType.isBlank()) {
            showError("Please fill date and expense type.");
            return;
        }

        if (PROJECT_EXPENSE.equals(expenseType) && selectedProject == null) {
            showError("Select a project.");
            return;
        }

        double amount;
        try {
            String amountText = amountField.getText() == null ? "" : amountField.getText().replace(",", "").trim();
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Enter a valid amount.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Enter a valid amount.");
            return;
        }

        if (paid && (paymentMode == null || paymentMode.isBlank())) {
            showError("Select payment mode.");
            return;
        }

        ExpensesModel model = new ExpensesModel();
        model.setExpenseDate(expenseDate);
        model.setExpenseType(expenseType);
        if (PROJECT_EXPENSE.equals(expenseType) && selectedProject != null) {
            model.setProjectId(selectedProject.getId());
        } else {
            model.setProjectId(null);
        }
        model.setCategory(category);
        model.setAmount(amount);
        model.setPaid(paid);
        model.setPaymentMode(paid ? paymentMode : null);
        model.setNotes(notes);
        if (editingExpense != null) {
            model.setId(editingExpense.getId());
            model.setWorklogId(editingExpense.getWorklogId());
            model.setPurchaseId(editingExpense.getPurchaseId());
        }

        try {
            ExpensesService service = new ExpensesService();
            if (editingExpense != null) {
                service.updateService(model);
            } else {
                service.insertService(model);
            }
            Thalam.open((Node) event.getSource(), "/Expenses/Expenses.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddExpenseController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Could not save expense.\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Expense");
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
