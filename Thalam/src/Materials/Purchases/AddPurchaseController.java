package Materials.Purchases;

import Materials.MaterialsDao;
import Materials.MaterialsModel;
import Materials.Vendors.VendorsDao;
import Materials.Vendors.VendorsModel;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import thalam.Thalam;

public class AddPurchaseController implements Initializable {

    private static final int INITIAL_ROWS = 5;

    private static PurchaseModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private DatePicker purchaseDatePicker;
    @FXML
    private ComboBox<ProjectsModel> projectCombo;
    @FXML
    private ComboBox<VendorsModel> vendorCombo;
    @FXML
    private VBox materialRowsBox;
    @FXML
    private Label grandTotalLabel;
    @FXML
    private CheckBox markAsPaidCheck;
    @FXML
    private VBox paymentModeSection;
    @FXML
    private ComboBox<String> paymentModeCombo;
    @FXML
    private TextArea notesField;

    private final List<MaterialRow> materialRows = new ArrayList<>();
    private ObservableList<MaterialsModel> materials = FXCollections.observableArrayList();
    private PurchaseModel editingPurchase;

    public static void prepareEdit(PurchaseModel purchase) {
        pendingEdit = purchase;
    }

    private static class MaterialRow {
        final GridPane pane;
        final ComboBox<MaterialsModel> materialCombo;
        final TextField qtyField;
        final TextField unitCostField;
        final TextField amountField;

        MaterialRow(
                GridPane pane,
                ComboBox<MaterialsModel> materialCombo,
                TextField qtyField,
                TextField unitCostField,
                TextField amountField) {
            this.pane = pane;
            this.materialCombo = materialCombo;
            this.qtyField = qtyField;
            this.unitCostField = unitCostField;
            this.amountField = amountField;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingPurchase = pendingEdit;
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

        vendorCombo.setConverter(new StringConverter<VendorsModel>() {
            @Override
            public String toString(VendorsModel vendor) {
                return vendor == null ? "" : vendor.getName();
            }

            @Override
            public VendorsModel fromString(String string) {
                return null;
            }
        });

        paymentModeCombo.setItems(FXCollections.observableArrayList(
                "Cash", "UPI", "Bank Transfer", "Cheque", "Credit"));

        markAsPaidCheck.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            paymentModeSection.setVisible(isSelected);
            paymentModeSection.setManaged(isSelected);
            if (!isSelected) {
                paymentModeCombo.getSelectionModel().clearSelection();
            }
        });

        try {
            ProjectDao projectDao = new ProjectDao();
            projectCombo.setItems(FXCollections.observableArrayList(projectDao.view()));

            VendorsDao vendorsDao = new VendorsDao();
            vendorCombo.setItems(FXCollections.observableArrayList(vendorsDao.view()));

            reloadMaterials();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (editingPurchase != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Purchase");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the purchase details.");
            }
            applyEditValues();
        } else {
            for (int i = 0; i < INITIAL_ROWS; i++) {
                addMaterialRow();
            }
        }
        updateGrandTotal();
    }

    private void applyEditValues() {
        purchaseDatePicker.setValue(editingPurchase.getPurchaseDate());
        notesField.setText(editingPurchase.getNotes());
        markAsPaidCheck.setSelected(editingPurchase.isPaid());
        if (editingPurchase.isPaid() && editingPurchase.getPaymentMode() != null) {
            paymentModeCombo.setValue(editingPurchase.getPaymentMode());
        }

        for (ProjectsModel project : projectCombo.getItems()) {
            if (project.getId() == editingPurchase.getProjectId()) {
                projectCombo.setValue(project);
                break;
            }
        }
        for (VendorsModel vendor : vendorCombo.getItems()) {
            if (vendor.getId() == editingPurchase.getVendorId()) {
                vendorCombo.setValue(vendor);
                break;
            }
        }

        List<PurchaseItemModel> items = new ArrayList<>();
        try {
            items = new PurchaseDao().viewItems(editingPurchase.getId());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        int rowsNeeded = Math.max(INITIAL_ROWS, items.size());
        for (int i = 0; i < rowsNeeded; i++) {
            addMaterialRow();
        }

        for (int i = 0; i < items.size() && i < materialRows.size(); i++) {
            PurchaseItemModel item = items.get(i);
            MaterialRow row = materialRows.get(i);
            for (MaterialsModel material : materials) {
                if (material.getId() == item.getMaterialId()) {
                    row.materialCombo.setValue(material);
                    break;
                }
            }
            row.qtyField.setText(stripTrailingZeros(item.getQty()));
            row.unitCostField.setText(String.format("%.2f", item.getUnitCost()));
        }
    }

    private String stripTrailingZeros(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private void reloadMaterials() throws ClassNotFoundException, SQLException {
        MaterialsDao materialsDao = new MaterialsDao();
        materials = FXCollections.observableArrayList(materialsDao.view());
        for (MaterialRow row : materialRows) {
            MaterialsModel selected = row.materialCombo.getValue();
            row.materialCombo.setItems(materials);
            row.materialCombo.setValue(selected);
        }
    }

    @FXML
    private void onAddMaterialRow(ActionEvent event) {
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

        TextField qtyField = new TextField();
        qtyField.setPromptText("Qty");
        qtyField.getStyleClass().add("module-form-bulk-input");

        TextField unitCostField = new TextField();
        unitCostField.setPromptText("0.00");
        unitCostField.getStyleClass().add("module-form-bulk-input");

        TextField amountField = new TextField();
        amountField.setPromptText("0.00");
        amountField.getStyleClass().add("module-form-bulk-input");
        amountField.setEditable(false);
        amountField.setFocusTraversable(false);

        qtyField.textProperty().addListener((obs, oldVal, newVal) -> recalculateRow(qtyField, unitCostField, amountField));
        unitCostField.textProperty().addListener((obs, oldVal, newVal) -> recalculateRow(qtyField, unitCostField, amountField));

        Button removeButton = new Button("✕");
        removeButton.getStyleClass().add("module-form-bulk-remove");
        removeButton.setFocusTraversable(false);

        GridPane row = new GridPane();
        row.getStyleClass().add("module-form-bulk-row");
        row.setHgap(8);
        row.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPercentWidth(32);
        c0.setHgrow(Priority.ALWAYS);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(16);
        c1.setHgrow(Priority.ALWAYS);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(22);
        c2.setHgrow(Priority.ALWAYS);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(22);
        c3.setHgrow(Priority.ALWAYS);
        ColumnConstraints c4 = new ColumnConstraints();
        c4.setPercentWidth(8);
        c4.setHgrow(Priority.NEVER);
        c4.setHalignment(HPos.CENTER);
        row.getColumnConstraints().addAll(c0, c1, c2, c3, c4);

        GridPane.setHalignment(materialCombo, HPos.LEFT);
        GridPane.setHalignment(removeButton, HPos.CENTER);
        row.add(materialCombo, 0, 0);
        row.add(qtyField, 1, 0);
        row.add(unitCostField, 2, 0);
        row.add(amountField, 3, 0);
        row.add(removeButton, 4, 0);

        MaterialRow materialRow = new MaterialRow(row, materialCombo, qtyField, unitCostField, amountField);
        removeButton.setOnAction(e -> removeMaterialRow(materialRow));

        materialRowsBox.getChildren().add(row);
        materialRows.add(materialRow);
    }

    private void removeMaterialRow(MaterialRow row) {
        if (materialRows.size() <= 1) {
            row.materialCombo.setValue(null);
            row.qtyField.clear();
            row.unitCostField.clear();
            row.amountField.clear();
            updateGrandTotal();
            return;
        }
        materialRowsBox.getChildren().remove(row.pane);
        materialRows.remove(row);
        updateGrandTotal();
    }

    private void recalculateRow(TextField qtyField, TextField unitCostField, TextField amountField) {
        double qty = parseNumber(qtyField.getText());
        double unitCost = parseNumber(unitCostField.getText());
        double amount = qty * unitCost;
        if (qty == 0 && unitCost == 0) {
            amountField.clear();
        } else {
            amountField.setText(String.format("%.2f", amount));
        }
        updateGrandTotal();
    }

    private void updateGrandTotal() {
        double total = 0;
        for (MaterialRow row : materialRows) {
            total += parseNumber(row.amountField.getText());
        }
        if (total == 0) {
            grandTotalLabel.setText("—");
        } else {
            grandTotalLabel.setText(String.format("%.2f", total));
        }
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
    private void onAddMaterial(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Materials/AddMaterialPopup.fxml"));
        Parent root = loader.load();
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(root, 520, 420));
        popupStage.setTitle("Add Material");
        Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        popupStage.initOwner(mainWindow);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        popupStage.showAndWait();

        try {
            reloadMaterials();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Purchases/Purchases.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        LocalDate purchaseDate = purchaseDatePicker.getValue();
        ProjectsModel selectedProject = projectCombo.getValue();
        VendorsModel selectedVendor = vendorCombo.getValue();
        boolean paid = markAsPaidCheck.isSelected();
        String paymentMode = paymentModeCombo.getValue();
        String notes = notesField.getText() == null ? "" : notesField.getText().trim();

        if (purchaseDate == null || selectedProject == null || selectedVendor == null) {
            showError("Please fill purchase date, project and vendor.");
            return;
        }

        List<PurchaseItemModel> items = new ArrayList<>();
        double grandTotal = 0;

        for (MaterialRow row : materialRows) {
            MaterialsModel material = row.materialCombo.getValue();
            double qty = parseNumber(row.qtyField.getText());
            double unitCost = parseNumber(row.unitCostField.getText());

            if (material == null && qty == 0 && unitCost == 0) {
                continue;
            }
            if (material == null || qty <= 0) {
                showError("Each filled material row needs a material and qty greater than 0.");
                return;
            }

            double amount = qty * unitCost;
            items.add(new PurchaseItemModel(material.getId(), qty, unitCost, amount));
            grandTotal += amount;
        }

        if (items.isEmpty()) {
            showError("Add at least one material row.");
            return;
        }

        if (paid && (paymentMode == null || paymentMode.isBlank())) {
            showError("Select payment mode.");
            return;
        }

        PurchaseModel model = new PurchaseModel();
        model.setPurchaseDate(purchaseDate);
        model.setProjectId(selectedProject.getId());
        model.setVendorId(selectedVendor.getId());
        model.setVendorName(selectedVendor.getName());
        model.setGrandTotal(grandTotal);
        model.setPaid(paid);
        model.setPaymentMode(paid ? paymentMode : null);
        model.setNotes(notes);
        model.setItems(items);

        try {
            PurchaseService service = new PurchaseService();
            if (editingPurchase != null) {
                model.setId(editingPurchase.getId());
                service.updateService(model);
            } else {
                service.insertService(model);
            }
            Thalam.open((Node) event.getSource(), "/Materials/Purchases/Purchases.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Could not save purchase.\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Purchase");
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
