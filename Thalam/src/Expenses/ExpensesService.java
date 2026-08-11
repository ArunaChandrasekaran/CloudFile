package Expenses;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class ExpensesService {

    ExpensesDao dao = new ExpensesDao();

    public void insertService(ExpensesModel model) throws SQLException, ClassNotFoundException {
        validate(model);
        dao.insert(model);
    }

    public void updateService(ExpensesModel model) throws SQLException, ClassNotFoundException {
        if (model.getId() <= 0) {
            throw new SQLException("Expense id is required");
        }
        validate(model);
        dao.update(model);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        dao.delete(id);
    }

    public ArrayList<ExpensesModel> viewService() throws ClassNotFoundException, SQLException {
        return dao.view();
    }

    private void validate(ExpensesModel model) throws SQLException {
        if (model.getExpenseDate() == null) {
            throw new SQLException("Expense date is required");
        }
        if (model.getExpenseType() == null || model.getExpenseType().isBlank()) {
            throw new SQLException("Select expense type");
        }
        if ("Project Expense".equals(model.getExpenseType())
                && (model.getProjectId() == null || model.getProjectId() <= 0)) {
            throw new SQLException("Select a project");
        }
        if (model.getAmount() <= 0) {
            throw new SQLException("Enter a valid amount");
        }
        if (model.isPaid()
                && (model.getPaymentMode() == null || model.getPaymentMode().isBlank())) {
            throw new SQLException("Select payment mode");
        }
    }
}
