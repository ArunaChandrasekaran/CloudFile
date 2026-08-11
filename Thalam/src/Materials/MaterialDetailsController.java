package Materials;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import thalam.TableSearch;
import thalam.Thalam;

public class MaterialDetailsController implements Initializable {

    private static MaterialsModel pendingMaterial;

    @FXML
    private Label displayNameLabel;
    @FXML
    private Label nameValue;
    @FXML
    private Label unitValue;
    @FXML
    private Label notesValue;
    @FXML
    private Label purchasedStockValue;
    @FXML
    private Label currentStockValue;

    private MaterialsModel material;

    public static void prepare(MaterialsModel model) {
        pendingMaterial = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        material = pendingMaterial;
        pendingMaterial = null;

        if (material == null) {
            return;
        }

        enrichNotes(material);

        String name = TableSearch.safe(material.getName());
        displayNameLabel.setText("MAT-" + material.getId());
        nameValue.setText(name);
        unitValue.setText(TableSearch.safe(material.getUnit()));
        notesValue.setText(TableSearch.safe(material.getNotes()));
        purchasedStockValue.setText(formatQty(material.getPurchasedStock()));
        currentStockValue.setText(formatQty(material.getCurrentStock()));
    }

    private static void enrichNotes(MaterialsModel model) {
        if (model.getNotes() != null && !model.getNotes().trim().isEmpty()) {
            return;
        }
        try {
            for (MaterialsModel row : new MaterialsDao().view()) {
                if (row.getId() == model.getId()) {
                    model.setNotes(row.getNotes());
                    if (TableSearch.safe(model.getUnit()).isEmpty()) {
                        model.setUnit(row.getUnit());
                    }
                    if (TableSearch.safe(model.getName()).isEmpty()) {
                        model.setName(row.getName());
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MaterialDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String formatQty(double qty) {
        if (qty == (long) qty) {
            return String.valueOf((long) qty);
        }
        return String.valueOf(qty);
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Materials/Materials.fxml");
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
