package Invoices;

import Projects.ProjectDao;
import Projects.ProjectsModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import thalam.Thalam;

public class AddInvoiceController implements Initializable {

    private static InvoicesModel pendingEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private Label formTaglineLabel;
    @FXML
    private ComboBox<ProjectsModel> projectCombo;
    @FXML
    private TextField purposeField;
    @FXML
    private DatePicker invoiceDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TextField amountField;
    @FXML
    private Label statusValue;
    @FXML
    private CheckBox markAsPaidCheck;
    @FXML
    private GridPane paymentSection;
    @FXML
    private DatePicker paymentDatePicker;
    @FXML
    private ComboBox<String> paymentModeCombo;
    @FXML
    private TextArea notesField;

    private InvoicesModel editingInvoice;

    public static void prepareEdit(InvoicesModel invoice) {
        pendingEdit = invoice;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editingInvoice = pendingEdit;
        pendingEdit = null;

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
            Logger.getLogger(AddInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        paymentModeCombo.setItems(FXCollections.observableArrayList(
                "Cash", "UPI", "Bank Transfer", "Cheque", "Credit"));

        markAsPaidCheck.selectedProperty().addListener((obs, was, isPaid) -> {
            paymentSection.setVisible(isPaid);
            paymentSection.setManaged(isPaid);
            if (isPaid) {
                if (paymentDatePicker.getValue() == null) {
                    paymentDatePicker.setValue(LocalDate.now());
                }
            } else {
                paymentDatePicker.setValue(null);
                paymentModeCombo.getSelectionModel().clearSelection();
            }
            refreshStatusLabel();
        });
        dueDatePicker.valueProperty().addListener((obs, o, n) -> refreshStatusLabel());
        refreshStatusLabel();

        if (editingInvoice != null) {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Edit Invoice");
            }
            if (formTaglineLabel != null) {
                formTaglineLabel.setText("Update the invoice details.");
            }
            prefillForm(editingInvoice);
        }
    }

    private void prefillForm(InvoicesModel invoice) {
        if (projectCombo.getItems() != null) {
            for (ProjectsModel project : projectCombo.getItems()) {
                if (project != null && project.getId() == invoice.getProjectId()) {
                    projectCombo.setValue(project);
                    break;
                }
            }
        }

        purposeField.setText(invoice.getInvoicePurpose() == null ? "" : invoice.getInvoicePurpose());
        invoiceDatePicker.setValue(invoice.getInvoiceDate());
        dueDatePicker.setValue(invoice.getDueDate());
        amountField.setText(String.valueOf(invoice.getInvoiceAmount()));

        markAsPaidCheck.setSelected(invoice.isPaid());
        paymentSection.setVisible(invoice.isPaid());
        paymentSection.setManaged(invoice.isPaid());
        if (invoice.isPaid()) {
            paymentDatePicker.setValue(invoice.getPaymentDate());
            if (invoice.getPaymentMode() != null) {
                paymentModeCombo.setValue(invoice.getPaymentMode());
            }
        }

        notesField.setText(invoice.getNotes() == null ? "" : invoice.getNotes());
        refreshStatusLabel();
    }

    private void refreshStatusLabel() {
        if (statusValue == null) {
            return;
        }
        statusValue.setText(InvoicesModel.resolveStatus(
                markAsPaidCheck.isSelected(), dueDatePicker.getValue()));
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
    }

    @FXML
    private void onSave(ActionEvent event) throws IOException {
        ProjectsModel selectedProject = projectCombo.getValue();
        String purpose = purposeField.getText() == null ? "" : purposeField.getText().trim();
        LocalDate invoiceDate = invoiceDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();
        LocalDate paymentDate = paymentDatePicker.getValue();
        boolean paid = markAsPaidCheck.isSelected();
        String paymentMode = paymentModeCombo.getValue();
        String notes = notesField.getText() == null ? "" : notesField.getText().trim();

        if (selectedProject == null || purpose.isEmpty() || invoiceDate == null) {
            showError("Please fill project, invoice purpose and invoice date.");
            return;
        }

        double amount;
        try {
            String amountText = amountField.getText() == null
                    ? "" : amountField.getText().replace(",", "").trim();
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Enter a valid invoice amount.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Enter a valid invoice amount.");
            return;
        }

        if (paid) {
            if (paymentDate == null) {
                showError("Select payment date.");
                return;
            }
            if (paymentMode == null || paymentMode.isBlank()) {
                showError("Select payment mode.");
                return;
            }
        }

        InvoicesModel model = new InvoicesModel();
        model.setProjectId(selectedProject.getId());
        model.setInvoicePurpose(purpose);
        model.setInvoiceDate(invoiceDate);
        model.setDueDate(dueDate);
        model.setInvoiceAmount(amount);
        model.setPaid(paid);
        model.setPaymentDate(paid ? paymentDate : null);
        model.setPaymentMode(paid ? paymentMode : null);
        model.applyResolvedStatus();
        model.setNotes(notes);
        if (editingInvoice != null) {
            model.setId(editingInvoice.getId());
        }

        try {
            InvoicesService service = new InvoicesService();
            if (editingInvoice != null) {
                service.updateService(model);
            } else {
                service.insertService(model);
            }
            Thalam.open((Node) event.getSource(), "/Invoices/Invoices.fxml");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Could not save invoice.\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invoice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
