package Invoices;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class InvoicesService {

    InvoicesDao dao = new InvoicesDao();

    public void insertService(InvoicesModel model) throws SQLException, ClassNotFoundException {
        if (model.getProjectId() <= 0) {
            throw new SQLException("Select a project");
        }
        if (model.getInvoicePurpose() == null || model.getInvoicePurpose().isBlank()) {
            throw new SQLException("Invoice purpose is required");
        }
        if (model.getInvoiceDate() == null) {
            throw new SQLException("Invoice date is required");
        }
        if (model.getInvoiceAmount() <= 0) {
            throw new SQLException("Enter a valid invoice amount");
        }
        if (model.isPaid()) {
            if (model.getPaymentMode() == null || model.getPaymentMode().isBlank()) {
                throw new SQLException("Select payment mode");
            }
            if (model.getPaymentDate() == null) {
                throw new SQLException("Payment date is required when marked as paid");
            }
        }
        dao.insert(model);
    }

    public void updateService(InvoicesModel model) throws SQLException, ClassNotFoundException {
        if (model.getId() <= 0) {
            throw new SQLException("Invoice id is required");
        }
        if (model.getProjectId() <= 0) {
            throw new SQLException("Select a project");
        }
        if (model.getInvoicePurpose() == null || model.getInvoicePurpose().isBlank()) {
            throw new SQLException("Invoice purpose is required");
        }
        if (model.getInvoiceDate() == null) {
            throw new SQLException("Invoice date is required");
        }
        if (model.getInvoiceAmount() <= 0) {
            throw new SQLException("Enter a valid invoice amount");
        }
        if (model.isPaid()) {
            if (model.getPaymentMode() == null || model.getPaymentMode().isBlank()) {
                throw new SQLException("Select payment mode");
            }
            if (model.getPaymentDate() == null) {
                throw new SQLException("Payment date is required when marked as paid");
            }
        }
        dao.update(model);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        dao.delete(id);
    }

    public ArrayList<InvoicesModel> viewService() throws ClassNotFoundException, SQLException {
        return dao.view();
    }
}
