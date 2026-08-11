package Expenses;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class ExpensesDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(ExpensesModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Expenses(expense_date, expense_type, project_id, category, "
                        + "amount, is_paid, payment_mode, notes, worklog_id, purchase_id) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)");
        bindExpenseFields(ps, model);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public void update(ExpensesModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Expenses SET expense_date = ?, expense_type = ?, project_id = ?, category = ?, "
                        + "amount = ?, is_paid = ?, payment_mode = ?, notes = ?, worklog_id = ?, "
                        + "purchase_id = ? WHERE id = ?");
        bindExpenseFields(ps, model);
        ps.setInt(11, model.getId());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    private void bindExpenseFields(PreparedStatement ps, ExpensesModel model) throws SQLException {
        ps.setDate(1, Date.valueOf(model.getExpenseDate()));
        ps.setString(2, model.getExpenseType());

        if (model.getProjectId() != null && model.getProjectId() > 0) {
            ps.setInt(3, model.getProjectId());
        } else {
            ps.setNull(3, Types.INTEGER);
        }

        if (model.getCategory() != null && !model.getCategory().isBlank()) {
            ps.setString(4, model.getCategory());
        } else {
            ps.setNull(4, Types.VARCHAR);
        }

        ps.setDouble(5, model.getAmount());
        ps.setBoolean(6, model.isPaid());

        if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
            ps.setString(7, model.getPaymentMode());
        } else {
            ps.setNull(7, Types.VARCHAR);
        }

        ps.setString(8, model.getNotes() == null ? "" : model.getNotes());

        if (model.getWorklogId() != null && model.getWorklogId() > 0) {
            ps.setInt(9, model.getWorklogId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        if (model.getPurchaseId() != null && model.getPurchaseId() > 0) {
            ps.setInt(10, model.getPurchaseId());
        } else {
            ps.setNull(10, Types.INTEGER);
        }
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Expenses WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public ArrayList<ExpensesModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<ExpensesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT e.id, e.expense_date, e.expense_type, e.project_id, e.category, "
                        + "e.amount, e.is_paid, e.payment_mode, e.notes, e.worklog_id, e.purchase_id, "
                        + "p.name AS project_name "
                        + "FROM Expenses e "
                        + "LEFT JOIN Projects p ON e.project_id = p.id "
                        + "ORDER BY e.id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(mapExpense(rs));
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    private ExpensesModel mapExpense(ResultSet rs) throws SQLException {
        ExpensesModel model = new ExpensesModel();
        model.setId(rs.getInt("id"));

        Date date = rs.getDate("expense_date");
        if (date != null) {
            model.setExpenseDate(date.toLocalDate());
        }

        model.setExpenseType(rs.getString("expense_type"));

        int projectId = rs.getInt("project_id");
        if (!rs.wasNull()) {
            model.setProjectId(projectId);
        }
        model.setProjectName(rs.getString("project_name"));
        model.setCategory(rs.getString("category"));
        model.setAmount(rs.getDouble("amount"));
        model.setPaid(rs.getBoolean("is_paid"));
        model.setPaymentMode(rs.getString("payment_mode"));
        model.setPaymentStatus(model.isPaid() ? "Paid" : "Unpaid");
        model.setNotes(rs.getString("notes"));

        int worklogId = rs.getInt("worklog_id");
        if (!rs.wasNull()) {
            model.setWorklogId(worklogId);
        }

        int purchaseId = rs.getInt("purchase_id");
        if (!rs.wasNull()) {
            model.setPurchaseId(purchaseId);
        }
        return model;
    }

    public ArrayList<ExpensesModel> viewRecentByProject(int projectId, int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<ExpensesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT e.id, e.category, e.amount, e.expense_date "
                        + "FROM Expenses e "
                        + "WHERE e.project_id = ? "
                        + "ORDER BY e.id DESC LIMIT ?");
        ps.setInt(1, projectId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ExpensesModel model = new ExpensesModel();
            model.setId(rs.getInt("id"));
            model.setCategory(rs.getString("category"));
            model.setAmount(rs.getDouble("amount"));
            Date date = rs.getDate("expense_date");
            if (date != null) {
                model.setExpenseDate(date.toLocalDate());
            }
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<String> viewCategoriesByType(String expenseType)
            throws ClassNotFoundException, SQLException {
        ArrayList<String> list = new ArrayList<>();
        if (expenseType == null || expenseType.isBlank()) {
            return list;
        }

        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT DISTINCT category "
                        + "FROM Expenses "
                        + "WHERE expense_type = ? "
                        + "AND category IS NOT NULL "
                        + "AND TRIM(category) <> '' "
                        + "ORDER BY category");
        ps.setString(1, expenseType);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(rs.getString("category"));
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }
}
