package Clients;

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

public class ClientsController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<ClientsModel> recordsTable;
    @FXML
    private TableColumn<ClientsModel, Integer> serialColumn;
    @FXML
    private TableColumn<ClientsModel, String> id;
    @FXML
    private TableColumn<ClientsModel, String> name;
    @FXML
    private TableColumn<ClientsModel, String> phone;
    @FXML
    private TableColumn<ClientsModel, String> email;
    @FXML
    private TableColumn<ClientsModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<ClientsModel> master = FXCollections.observableArrayList();
    private final ClientsDao cd = new ClientsDao();
    private final ClientsService clientsService = new ClientsService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(cell -> {
            ClientsModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "CLI-" + row.getId());
        });
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        pageState = TableSearch.bind(searchField, recordsTable, master, c ->
                "CLI-" + c.getId() + " " + TableSearch.safe(c.getName())
                        + " " + TableSearch.safe(c.getPhone()) + " " + TableSearch.safe(c.getEmail()),
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

    private void onOpenDetails(ClientsModel client) {
        try {
            ClientDetailsController.prepare(client);
            Thalam.open(recordsTable, "/Clients/ClientDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(cd.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(ClientsModel client) {
        try {
            AddClientController.prepareEdit(client);
            Thalam.open(recordsTable, "/Clients/AddClient.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(ClientsModel client) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Client");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete client \"" + client.getName() + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            clientsService.deleteService(client.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Client");
            error.setHeaderText(null);
            error.setContentText("Could not delete client.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddClientController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Clients/AddClient.fxml");
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
