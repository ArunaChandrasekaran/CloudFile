package Materials;

import Projects.ProjectDao;
import Projects.ProjectsModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import thalam.TableRowDetails;
import thalam.TableSearch;
import thalam.Thalam;

public class MaterialsController implements Initializable {

    @FXML
    private ComboBox<ProjectsModel> projectCombo;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<MaterialsModel> recordsTable;
    @FXML
    private TableColumn<MaterialsModel, Integer> serialColumn;
    @FXML
    private TableColumn<MaterialsModel, String> idColumn;
    @FXML
    private TableColumn<MaterialsModel, String> name;
    @FXML
    private TableColumn<MaterialsModel, String> unit;
    @FXML
    private TableColumn<MaterialsModel, Double> purchasedStockColumn;
    @FXML
    private TableColumn<MaterialsModel, Double> currentStockColumn;
    @FXML
    private TableColumn<MaterialsModel, String> statusColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<MaterialsModel> master = FXCollections.observableArrayList();
    private final MaterialsDao materialsDao = new MaterialsDao();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cell -> {
            MaterialsModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "MAT-" + row.getId());
        });
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        purchasedStockColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedStock"));
        currentStockColumn.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));
        statusColumn.setCellFactory(col -> new TableCell<MaterialsModel, String>() {
            private final Label badge = new Label();

            {
                setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null || status.isBlank()) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                badge.setText(status);
                MaterialsModel row = getTableRow() == null ? null : getTableRow().getItem();
                String key = row == null ? "in" : row.getStockStatusKey();
                badge.getStyleClass().setAll("stock-status-badge", "stock-status-" + key);
                setGraphic(badge);
                setText(null);
            }
        });

        pageState = TableSearch.bind(searchField, recordsTable, master, m ->
                "MAT-" + m.getId() + " " + TableSearch.safe(m.getName())
                        + " " + TableSearch.safe(m.getUnit())
                        + " " + TableSearch.safe(m.getStockStatus()),
                recordsSummaryLabel, prevPageButton, pageNumberButton, nextPageButton);

        serialColumn.setSortable(false);
        serialColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(pageState.firstRowNumber() + getIndex()));
            }
        });

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
            Logger.getLogger(MaterialsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        projectCombo.valueProperty().addListener((obs, oldVal, selected) -> loadMaterialsForProject(selected));
        TableRowDetails.attach(recordsTable, this::onOpenDetails);
    }

    private void onOpenDetails(MaterialsModel material) {
        try {
            MaterialDetailsController.prepare(material);
            Thalam.open(recordsTable, "/Materials/MaterialDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(MaterialsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMaterialsForProject(ProjectsModel selected) {
        if (selected == null) {
            master.clear();
            return;
        }

        try {
            master.setAll(materialsDao.viewByProject(selected.getId()));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MaterialsController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
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
