package Materials.Vendors;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

public class VendorsController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<VendorsModel, Integer> serialColumn;
    @FXML
    private TableColumn<VendorsModel, String> id;
    @FXML
    private TableColumn<VendorsModel, String> name;
    @FXML
    private TableColumn<VendorsModel, String> phone;
    @FXML
    private TableColumn<VendorsModel, String> email;
    @FXML
    private TableColumn<VendorsModel, Void> actionsColumn;
    @FXML
    private TableView<VendorsModel> recordsTable;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<VendorsModel> master = FXCollections.observableArrayList();
    private final VendorsDao vd = new VendorsDao();
    private final VendorsService vendorsService = new VendorsService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(cell -> {
            VendorsModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "VEN-" + row.getId());
        });
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        pageState = TableSearch.bind(searchField, recordsTable, master, v ->
                "VEN-" + v.getId() + " " + TableSearch.safe(v.getName())
                        + " " + TableSearch.safe(v.getPhone()) + " " + TableSearch.safe(v.getEmail()),
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
        reloadTable();
    }

    private void onOpenDetails(VendorsModel vendor) {
        try {
            VendorDetailsController.prepare(vendor);
            Thalam.open(recordsTable, "/Materials/Vendors/VendorDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(vd.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(VendorsModel vendor) {
        try {
            AddVendorController.prepareEdit(vendor);
            Thalam.open(recordsTable, "/Materials/Vendors/AddVendor.fxml");
        } catch (IOException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(VendorsModel vendor) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Vendor");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete vendor \"" + vendor.getName() + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            vendorsService.deleteService(vendor.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Vendor");
            error.setHeaderText(null);
            error.setContentText("Could not delete vendor.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddVendorController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Materials/Vendors/AddVendor.fxml");
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
