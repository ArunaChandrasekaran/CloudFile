package Materials.Purchases;

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

public class PurchasesController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<PurchaseModel> recordsTable;
    @FXML
    private TableColumn<PurchaseModel, Integer> serialColumn;
    @FXML
    private TableColumn<PurchaseModel, String> idColumn;
    @FXML
    private TableColumn<PurchaseModel, LocalDate> dateColumn;
    @FXML
    private TableColumn<PurchaseModel, String> projectColumn;
    @FXML
    private TableColumn<PurchaseModel, String> vendorColumn;
    @FXML
    private TableColumn<PurchaseModel, Double> amountColumn;
    @FXML
    private TableColumn<PurchaseModel, String> paymentStatusColumn;
    @FXML
    private TableColumn<PurchaseModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<PurchaseModel> master = FXCollections.observableArrayList();
    private final PurchaseDao purchaseDao = new PurchaseDao();
    private final PurchaseService purchaseService = new PurchaseService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cell -> {
            PurchaseModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "PUR-" + row.getId());
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendorName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        pageState = TableSearch.bind(searchField, recordsTable, master, p ->
                "PUR-" + p.getId() + " " + TableSearch.safe(p.getProjectName())
                        + " " + TableSearch.safe(p.getVendorName())
                        + " " + TableSearch.safe(p.getPaymentStatus()),
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
            purchaseService.backfillLinkedExpenses();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PurchasesController.class.getName()).log(Level.WARNING, null, ex);
        }
        reloadTable();
    }

    private void onOpenDetails(PurchaseModel purchase) {
        try {
            PurchaseDetailsController.prepare(purchase);
            Thalam.open(recordsTable, "/Materials/Purchases/PurchaseDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(PurchasesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(purchaseDao.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PurchasesController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(PurchaseModel purchase) {
        try {
            AddPurchaseController.prepareEdit(purchase);
            Thalam.open(recordsTable, "/Materials/Purchases/AddPurchase.fxml");
        } catch (IOException ex) {
            Logger.getLogger(PurchasesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(PurchaseModel purchase) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Purchase");
        confirm.setHeaderText(null);
        String label = purchase.getVendorName() == null
                ? ("#" + purchase.getId())
                : purchase.getVendorName();
        confirm.setContentText("Delete purchase \"" + label + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            purchaseService.deleteService(purchase.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PurchasesController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Purchase");
            error.setHeaderText(null);
            error.setContentText("Could not delete purchase.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddPurchaseController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Materials/Purchases/AddPurchase.fxml");
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
