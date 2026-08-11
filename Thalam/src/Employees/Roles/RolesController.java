package Employees.Roles;

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

public class RolesController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<RoleModel> recordsTable;
    @FXML
    private TableColumn<RoleModel, Integer> serialColumn;
    @FXML
    private TableColumn<RoleModel, String> id;
    @FXML
    private TableColumn<RoleModel, String> name;
    @FXML
    private TableColumn<RoleModel, String> description;
    @FXML
    private TableColumn<RoleModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<RoleModel> master = FXCollections.observableArrayList();
    private final RoleDao daoObject = new RoleDao();
    private final RoleService roleService = new RoleService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(cell -> {
            RoleModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "ROL-" + row.getId());
        });
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        pageState = TableSearch.bind(searchField, recordsTable, master, r ->
                "ROL-" + r.getId() + " " + TableSearch.safe(r.getName())
                        + " " + TableSearch.safe(r.getDescription()),
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

    private void onOpenDetails(RoleModel role) {
        try {
            RoleDetailsController.prepare(role);
            Thalam.open(recordsTable, "/Employees/Roles/RoleDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(daoObject.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(RoleModel role) {
        try {
            AddRoleController.prepareEdit(role);
            Thalam.open(recordsTable, "/Employees/Roles/AddRole.fxml");
        } catch (IOException ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(RoleModel role) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Role");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete role \"" + role.getName() + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            roleService.deleteService(role.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Role");
            error.setHeaderText(null);
            error.setContentText("Could not delete role.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddRoleController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Employees/Roles/AddRole.fxml");
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
