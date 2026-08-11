package Clients;

import Invoices.InvoicesDao;
import Invoices.InvoicesModel;
import Projects.ProjectDao;
import Projects.ProjectsModel;
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

public class ClientDetailsController implements Initializable {

    private static final int REF_LIMIT = 5;
    private static ClientsModel pendingClient;

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
    private VBox projectsRefBox;
    @FXML
    private VBox invoicesRefBox;

    private ClientsModel client;

    public static void prepare(ClientsModel model) {
        pendingClient = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = pendingClient;
        pendingClient = null;

        if (client == null) {
            return;
        }

        String name = TableSearch.safe(client.getName());
        displayNameLabel.setText("CLI-" + client.getId());
        nameValue.setText(name);
        phoneValue.setText(TableSearch.safe(client.getPhone()));
        altPhoneValue.setText(TableSearch.safe(client.getAltPhone()));
        addressValue.setText(TableSearch.safe(client.getAddress()));

        String email = TableSearch.safe(client.getEmail());
        emailLink.setText(email);
        emailLink.setDisable(email.isEmpty());
        emailLink.setVisited(false);

        loadRelated();
    }

    private void loadRelated() {
        List<String> projectLines = new ArrayList<>();
        List<String> invoiceLines = new ArrayList<>();
        try {
            for (ProjectsModel p : new ProjectDao().viewRecentByClient(client.getId(), REF_LIMIT)) {
                projectLines.add("PRO-" + p.getId() + " · "
                        + TableSearch.safe(p.getProjectName()));
            }
            for (InvoicesModel i : new InvoicesDao().viewRecentByClient(client.getId(), REF_LIMIT)) {
                invoiceLines.add("INV-" + i.getId() + " · "
                        + TableSearch.safe(i.getInvoicePurpose()));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DetailsRefs.fill(projectsRefBox, projectLines);
        DetailsRefs.fill(invoicesRefBox, invoiceLines);
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Clients/Clients.fxml");
    }

    @FXML
    private void onEdit(ActionEvent event) throws IOException {
        AddClientController.prepareEdit(client);
        Thalam.open((Node) event.getSource(), "/Clients/AddClient.fxml");
    }

    @FXML
    private void onDeleteClient(ActionEvent event) throws IOException {
        if (client == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Client");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete client \"" + TableSearch.safe(client.getName()) + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            new ClientsService().deleteService(client.getId());
            Thalam.open((Node) event.getSource(), "/Clients/Clients.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Client");
            error.setHeaderText(null);
            error.setContentText("Could not delete client.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onViewAllProjects(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Projects/ProjectsList.fxml");
    }

    @FXML
    private void onViewAllInvoices(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
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
