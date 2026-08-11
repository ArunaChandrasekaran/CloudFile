package Materials.Purchases;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class PurchaseService {

    PurchaseDao dao = new PurchaseDao();

    public void insertService(PurchaseModel model) throws SQLException, ClassNotFoundException {
        validate(model);
        dao.insert(model);
    }

    public void updateService(PurchaseModel model) throws SQLException, ClassNotFoundException {
        if (model.getId() <= 0) {
            throw new SQLException("Purchase id is required for update");
        }
        validate(model);
        dao.update(model);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        dao.delete(id);
    }

    public ArrayList<PurchaseModel> viewService() throws ClassNotFoundException, SQLException {
        return dao.view();
    }

    /** Create missing Expenses rows for existing purchases (idempotent). */
    public int backfillLinkedExpenses() throws ClassNotFoundException, SQLException {
        return dao.backfillMissingPurchaseExpenses();
    }

    private void validate(PurchaseModel model) throws SQLException {
        if (model.getPurchaseDate() == null) {
            throw new SQLException("Purchase date is required");
        }
        if (model.getProjectId() <= 0) {
            throw new SQLException("Select a project");
        }
        if (model.getVendorId() <= 0) {
            throw new SQLException("Select a vendor");
        }
        if (model.getItems() == null || model.getItems().isEmpty()) {
            throw new SQLException("Add at least one material row");
        }
        if (model.isPaid()
                && (model.getPaymentMode() == null || model.getPaymentMode().isBlank())) {
            throw new SQLException("Select payment mode");
        }
    }
}
