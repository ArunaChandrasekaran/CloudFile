package Projects;

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
import javafx.geometry.Pos;
import thalam.TableActionButtons;
import thalam.TableRowDetails;
import thalam.TableSearch;
import thalam.Thalam;

public class ProjectsListController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<ProjectsModel> recordsTable;
    @FXML
    private TableColumn<ProjectsModel, Integer> serialColumn;
    @FXML
    private TableColumn<ProjectsModel, String> projectIdColumn;
    @FXML
    private TableColumn<ProjectsModel, String> projectNameColumn;
    @FXML
    private TableColumn<ProjectsModel, String> clientColumn;
    @FXML
    private TableColumn<ProjectsModel, LocalDate> startDateColumn;
    @FXML
    private TableColumn<ProjectsModel, LocalDate> endDateColumn;
    @FXML
    private TableColumn<ProjectsModel, String> statusColumn;
    @FXML
    private TableColumn<ProjectsModel, Void> actionsColumn;
    @FXML
    private Label recordsSummaryLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button pageNumberButton;
    @FXML
    private Button nextPageButton;

    private final ObservableList<ProjectsModel> master = FXCollections.observableArrayList();
    private final ProjectDao projectDao = new ProjectDao();
    private final ProjectsService projectsService = new ProjectsService();
    private TableSearch.PageState pageState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        projectIdColumn.setCellValueFactory(cell -> {
            ProjectsModel row = cell.getValue();
            return new SimpleStringProperty(row == null ? "" : "PRO-" + row.getId());
        });
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("starDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedEndDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(col -> new TableCell<ProjectsModel, String>() {
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
                ProjectsModel row = getTableRow() == null ? null : getTableRow().getItem();
                String key = row == null ? "ongoing" : row.getStatusKey();
                badge.getStyleClass().setAll("project-status-badge", "project-status-" + key);
                setGraphic(badge);
                setText(null);
            }
        });

        pageState = TableSearch.bind(searchField, recordsTable, master, p ->
                "PRO-" + p.getId() + " " + TableSearch.safe(p.getProjectName())
                        + " " + TableSearch.safe(p.getClient()) + " " + TableSearch.safe(p.getStatus()),
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

    private void onOpenDetails(ProjectsModel project) {
        try {
            ProjectDetailsController.prepare(project);
            Thalam.open(recordsTable, "/Projects/ProjectDetails.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectsListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadTable() {
        try {
            master.setAll(projectDao.view());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListController.class.getName()).log(Level.SEVERE, null, ex);
            master.clear();
        }
    }

    private void onEdit(ProjectsModel project) {
        try {
            AddProjectController.prepareEdit(project);
            Thalam.open(recordsTable, "/Projects/AddProject.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectsListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onDelete(ProjectsModel project) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Project");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete project \"" + project.getProjectName() + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            projectsService.deleteService(project.getId());
            reloadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListController.class.getName()).log(Level.SEVERE, null, ex);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Delete Project");
            error.setHeaderText(null);
            error.setContentText("Could not delete project.\n" + ex.getMessage());
            error.showAndWait();
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        AddProjectController.prepareEdit(null);
        Thalam.open((Node) event.getSource(), "/Projects/AddProject.fxml");
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
