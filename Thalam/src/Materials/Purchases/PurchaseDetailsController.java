package Materials.Purchases;

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

public class PurchaseDetailsController implements Initializable {

    private static PurchaseModel pendingPurchase;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label dateValue;
    @FXML
    private Label projectValue;
    @FXML
    private Label vendorValue;
    @FXML
    private Label amountValue;
    @FXML
    private Label paidValue;
    @FXML
    private Label paymentModeValue;
    @FXML
    private Label notesValue;
    @FXML
    private TableView<PurchaseItemModel> materialsTable;
    @FXML
    private TableColumn<PurchaseItemModel, String> materialNameColumn;
    @FXML
    private TableColumn<PurchaseItemModel, String> materialQtyColumn;
    @FXML
    private TableColumn<PurchaseItemModel, String> materialUnitCostColumn;
    @FXML
    private TableColumn<PurchaseItemModel, String> materialAmountColumn;
    @FXML
    private VBox projectRefBox;
    @FXML
    private VBox vendorRefBox;

    private PurchaseModel purchase;

    public static void prepare(PurchaseModel model) {
        pendingPurchase = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        purchase = pendingPurchase;
        pendingPurchase = null;

        setupMaterialsTable();

        if (purchase == null) {
            return;
        }

        displayNameLabel.setText("PUR-" + purchase.getId());
        dateValue.setText(formatDate(purchase.getPurchaseDate()));
        projectValue.setText(TableSearch.safe(purchase.getProjectName()));
        vendorValue.setText(TableSearch.safe(purchase.getVendorName()));
        amountValue.setText(formatAmount(purchase.getGrandTotal()));
        paidValue.setText(purchase.isPaid() ? "Yes" : "No");
        paymentModeValue.setText(TableSearch.safe(purchase.getPaymentMode()));
        notesValue.setText(TableSearch.safe(purchase.getNotes()));
        materialsTable.setItems(FXCollections.observableArrayList(loadMaterials(purchase)));
        fitTableHeight(materialsTable);

        loadRelated();
    }

    private void setupMaterialsTable() {
        materialsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        materialNameColumn.setCellValueFactory(cd -> {
            PurchaseItemModel item = cd.getValue();
            String name = TableSearch.safe(item.getMaterialName());
            if (name.isEmpty()) {
                name = "Material #" + item.getMaterialId();
            }
            return new SimpleStringProperty(name);
        });
        materialQtyColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(formatQty(cd.getValue().getQty())));
        materialUnitCostColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(formatAmount(cd.getValue().getUnitCost())));
        materialAmountColumn.setCellValueFactory(cd ->
                new SimpleStringProperty(formatAmount(cd.getValue().getAmount())));
    }

    private void loadRelated() {
        List<String> projectLines = new ArrayList<>();
        List<String> vendorLines = new ArrayList<>();
        if (purchase.getProjectId() > 0) {
            projectLines.add("PRO-" + purchase.getProjectId() + " · "
                    + TableSearch.safe(purchase.getProjectName()));
        }
        if (purchase.getVendorId() > 0) {
            vendorLines.add("VEN-" + purchase.getVendorId() + " · "
                    + TableSearch.safe(purchase.getVendorName()));
        }
        DetailsRefs.fill(projectRefBox, projectLines.isEmpty() ? Collections.emptyList() : projectLines);
        DetailsRefs.fill(vendorRefBox, vendorLines.isEmpty() ? Collections.emptyList() : vendorLines);
    }

    private List<PurchaseItemModel> loadMaterials(PurchaseModel model) {
        List<PurchaseItemModel> items = model.getItems();
        if (items == null || items.isEmpty()) {
            try {
                items = new PurchaseDao().viewItems(model.getId());
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(PurchaseDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                return Collections.emptyList();
            }
        }
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, String> namesById = loadMaterialNames();
        for (PurchaseItemModel item : items) {
            if (TableSearch.safe(item.getMaterialName()).isEmpty()) {
                item.setMaterialName(namesById.getOrDefault(
                        item.getMaterialId(), "Material #" + item.getMaterialId()));
            }
        }
        return items;
    }

    private static Map<Integer, String> loadMaterialNames() {
        Map<Integer, String> map = new HashMap<>();
        try {
            for (MaterialsModel material : new MaterialsDao().view()) {
                map.put(material.getId(), material.getName());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PurchaseDetailsController.class.getName()).log(Level.SEVERE, null, ex);
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
        Thalam.open((Node) event.getSource(), "/Materials/Purchases/Purchases.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddPurchaseController.prepareEdit(purchase);
        Thalam.open((Node) event.getSource(), "/Materials/Purchases/AddPurchase.fxml");
    }

    @FXML
    private void onDeletePurchase(ActionEvent event) throws IOException {
        if (purchase == null) {
            return;
        }

        String label = firstNonBlank(purchase.getVendorName(), "#" + purchase.getId());
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Purchase");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete purchase \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new PurchaseService().deleteService(purchase.getId());
            Thalam.open((Node) event.getSource(), "/Materials/Purchases/Purchases.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Purchase");
            error.setHeaderText(null);
            error.setContentText("Could not delete purchase.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onViewAllProjects(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
    }

    @FXML
    private void onViewAllVendors(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Vendors/Vendors.fxml");
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
