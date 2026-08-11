package Materials.Vendors;

import Materials.Purchases.PurchaseDao;
import Materials.Purchases.PurchaseModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import thalam.DetailsRefs;
import thalam.TableSearch;
import thalam.Thalam;

public class VendorDetailsController implements Initializable {

    private static final int REF_LIMIT = 5;
    private static VendorsModel pendingVendor;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label nameValue;
    @FXML
    private Label phoneValue;
    @FXML
    private Hyperlink emailLink;
    @FXML
    private Label altPhoneValue;
    @FXML
    private Label addressValue;
    @FXML
    private VBox purchasesRefBox;

    private VendorsModel vendor;

    public static void prepare(VendorsModel model) {
        pendingVendor = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vendor = pendingVendor;
        pendingVendor = null;

        if (vendor == null) {
            return;
        }

        String name = TableSearch.safe(vendor.getName());
        displayNameLabel.setText("VEN-" + vendor.getId());
        nameValue.setText(name);
        phoneValue.setText(TableSearch.safe(vendor.getPhone()));
        altPhoneValue.setText(TableSearch.safe(vendor.getAltPhone()));
        addressValue.setText(TableSearch.safe(vendor.getAddress()));

        String email = TableSearch.safe(vendor.getEmail());
        emailLink.setText(email);
        emailLink.setDisable(email.isEmpty());
        emailLink.setVisited(false);

        loadRelated();
    }

    private void loadRelated() {
        List<String> purchaseLines = new ArrayList<>();
        try {
            for (PurchaseModel p : new PurchaseDao().viewRecentByVendor(vendor.getId(), REF_LIMIT)) {
                String label = TableSearch.safe(p.getProjectName());
                if (label.isEmpty() && p.getPurchaseDate() != null) {
                    label = p.getPurchaseDate().toString();
                }
                if (label.isEmpty()) {
                    label = String.format("₹ %.2f", p.getGrandTotal());
                }
                purchaseLines.add("PUR-" + p.getId() + " · " + label);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VendorDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DetailsRefs.fill(purchasesRefBox, purchaseLines);
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Vendors/Vendors.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddVendorController.prepareEdit(vendor);
        Thalam.open((Node) event.getSource(), "/Materials/Vendors/AddVendor.fxml");
    }

    @FXML
    private void onDeleteVendor(ActionEvent event) throws IOException {
        if (vendor == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Vendor");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete vendor \"" + TableSearch.safe(vendor.getName()) + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new VendorsService().deleteService(vendor.getId());
            Thalam.open((Node) event.getSource(), "/Materials/Vendors/Vendors.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Vendor");
            error.setHeaderText(null);
            error.setContentText("Could not delete vendor.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onViewAllPurchases(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Purchases/Purchases.fxml");
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
