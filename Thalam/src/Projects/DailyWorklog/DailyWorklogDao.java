package Projects.DailyWorklog;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class DailyWorklogDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(DailyWorklogModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO DailyWorklogs(project_id, work_date, employee_id, "
                            + "work_description, notes) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, model.getProjectId());
            ps.setDate(2, Date.valueOf(model.getWorkDate()));

            if (model.getEmployeeId() != null && model.getEmployeeId() > 0) {
                ps.setInt(3, model.getEmployeeId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, model.getWorkDescription());
            ps.setString(5, model.getNotes() == null ? "" : model.getNotes());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) {
                throw new SQLException("Failed to get generated worklog id");
            }
            int worklogId = keys.getInt(1);
            keys.close();
            ps.close();

            PreparedStatement pe = con.prepareStatement(
                    "INSERT INTO Expenses(expense_date, expense_type, project_id, category, "
                            + "amount, is_paid, payment_mode, notes, worklog_id) "
                            + "VALUES (?,?,?,?,?,?,?,?,?)");

            for (DailyWorklogExpenseLine line : model.getExpenses()) {
                pe.setDate(1, Date.valueOf(model.getWorkDate()));
                pe.setString(2, "Project Expense");
                pe.setInt(3, model.getProjectId());
                pe.setString(4, line.getCategory());
                pe.setDouble(5, line.getAmount());
                pe.setBoolean(6, true);
                pe.setString(7, "Cash");
                pe.setString(8, line.getDescription() == null ? "" : line.getDescription());
                pe.setInt(9, worklogId);
                pe.addBatch();
            }
            if (!model.getExpenses().isEmpty()) {
                pe.executeBatch();
            }
            pe.close();

            PreparedStatement pm = con.prepareStatement(
                    "INSERT INTO DailyWorklogMaterials(worklog_id, material_id, unit, qty, remarks) "
                            + "VALUES (?,?,?,?,?)");

            for (DailyWorklogMaterialLine line : model.getMaterials()) {
                pm.setInt(1, worklogId);
                pm.setInt(2, line.getMaterialId());
                pm.setString(3, line.getUnit() == null ? "" : line.getUnit());
                pm.setDouble(4, line.getQty());
                pm.setString(5, line.getRemarks() == null ? "" : line.getRemarks());
                pm.addBatch();
            }
            if (!model.getMaterials().isEmpty()) {
                pm.executeBatch();
            }
            pm.close();

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public void update(DailyWorklogModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE DailyWorklogs SET project_id = ?, work_date = ?, employee_id = ?, "
                            + "work_description = ?, notes = ? WHERE id = ?");

            ps.setInt(1, model.getProjectId());
            ps.setDate(2, Date.valueOf(model.getWorkDate()));

            if (model.getEmployeeId() != null && model.getEmployeeId() > 0) {
                ps.setInt(3, model.getEmployeeId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, model.getWorkDescription());
            ps.setString(5, model.getNotes() == null ? "" : model.getNotes());
            ps.setInt(6, model.getId());
            ps.executeUpdate();
            ps.close();

            PreparedStatement delExpenses = con.prepareStatement(
                    "DELETE FROM Expenses WHERE worklog_id = ?");
            delExpenses.setInt(1, model.getId());
            delExpenses.executeUpdate();
            delExpenses.close();

            PreparedStatement delMaterials = con.prepareStatement(
                    "DELETE FROM DailyWorklogMaterials WHERE worklog_id = ?");
            delMaterials.setInt(1, model.getId());
            delMaterials.executeUpdate();
            delMaterials.close();

            PreparedStatement pe = con.prepareStatement(
                    "INSERT INTO Expenses(expense_date, expense_type, project_id, category, "
                            + "amount, is_paid, payment_mode, notes, worklog_id) "
                            + "VALUES (?,?,?,?,?,?,?,?,?)");

            for (DailyWorklogExpenseLine line : model.getExpenses()) {
                pe.setDate(1, Date.valueOf(model.getWorkDate()));
                pe.setString(2, "Project Expense");
                pe.setInt(3, model.getProjectId());
                pe.setString(4, line.getCategory());
                pe.setDouble(5, line.getAmount());
                pe.setBoolean(6, true);
                pe.setString(7, "Cash");
                pe.setString(8, line.getDescription() == null ? "" : line.getDescription());
                pe.setInt(9, model.getId());
                pe.addBatch();
            }
            if (!model.getExpenses().isEmpty()) {
                pe.executeBatch();
            }
            pe.close();

            PreparedStatement pm = con.prepareStatement(
                    "INSERT INTO DailyWorklogMaterials(worklog_id, material_id, unit, qty, remarks) "
                            + "VALUES (?,?,?,?,?)");

            for (DailyWorklogMaterialLine line : model.getMaterials()) {
                pm.setInt(1, model.getId());
                pm.setInt(2, line.getMaterialId());
                pm.setString(3, line.getUnit() == null ? "" : line.getUnit());
                pm.setDouble(4, line.getQty());
                pm.setString(5, line.getRemarks() == null ? "" : line.getRemarks());
                pm.addBatch();
            }
            if (!model.getMaterials().isEmpty()) {
                pm.executeBatch();
            }
            pm.close();

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement delExpenses = con.prepareStatement(
                    "DELETE FROM Expenses WHERE worklog_id = ?");
            delExpenses.setInt(1, id);
            delExpenses.executeUpdate();
            delExpenses.close();

            PreparedStatement delMaterials = con.prepareStatement(
                    "DELETE FROM DailyWorklogMaterials WHERE worklog_id = ?");
            delMaterials.setInt(1, id);
            delMaterials.executeUpdate();
            delMaterials.close();

            PreparedStatement del = con.prepareStatement(
                    "DELETE FROM DailyWorklogs WHERE id = ?");
            del.setInt(1, id);
            del.executeUpdate();
            del.close();

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public ArrayList<DailyWorklogExpenseLine> viewExpenses(int worklogId)
            throws ClassNotFoundException, SQLException {
        ArrayList<DailyWorklogExpenseLine> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT category, notes, amount FROM Expenses "
                        + "WHERE worklog_id = ? ORDER BY id");
        ps.setInt(1, worklogId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            DailyWorklogExpenseLine line = new DailyWorklogExpenseLine();
            line.setCategory(rs.getString("category"));
            line.setDescription(rs.getString("notes"));
            line.setAmount(rs.getDouble("amount"));
            list.add(line);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<DailyWorklogMaterialLine> viewMaterials(int worklogId)
            throws ClassNotFoundException, SQLException {
        ArrayList<DailyWorklogMaterialLine> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT material_id, unit, qty, remarks FROM DailyWorklogMaterials "
                        + "WHERE worklog_id = ? ORDER BY id");
        ps.setInt(1, worklogId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            DailyWorklogMaterialLine line = new DailyWorklogMaterialLine();
            line.setMaterialId(rs.getInt("material_id"));
            line.setUnit(rs.getString("unit"));
            line.setQty(rs.getDouble("qty"));
            line.setRemarks(rs.getString("remarks"));
            list.add(line);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<DailyWorklogModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<DailyWorklogModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT w.id, w.project_id, w.work_date, w.employee_id, w.work_description, "
                        + "w.notes, p.name AS project_name, e.name AS employee_name "
                        + "FROM DailyWorklogs w "
                        + "INNER JOIN Projects p ON w.project_id = p.id "
                        + "LEFT JOIN Employees e ON w.employee_id = e.id "
                        + "ORDER BY w.id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            DailyWorklogModel model = new DailyWorklogModel();
            model.setId(rs.getInt("id"));
            model.setProjectId(rs.getInt("project_id"));
            model.setProjectName(rs.getString("project_name"));

            Date workDate = rs.getDate("work_date");
            if (workDate != null) {
                model.setWorkDate(workDate.toLocalDate());
            }

            int employeeId = rs.getInt("employee_id");
            if (!rs.wasNull()) {
                model.setEmployeeId(employeeId);
            }
            model.setEmployeeName(rs.getString("employee_name"));
            model.setWorkDescription(rs.getString("work_description"));
            model.setNotes(rs.getString("notes"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<DailyWorklogModel> viewRecentByProject(int projectId, int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<DailyWorklogModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT w.id, w.work_date, w.work_description, e.name AS employee_name "
                        + "FROM DailyWorklogs w "
                        + "LEFT JOIN Employees e ON w.employee_id = e.id "
                        + "WHERE w.project_id = ? "
                        + "ORDER BY w.work_date DESC, w.id DESC LIMIT ?");
        ps.setInt(1, projectId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            DailyWorklogModel model = new DailyWorklogModel();
            model.setId(rs.getInt("id"));
            Date workDate = rs.getDate("work_date");
            if (workDate != null) {
                model.setWorkDate(workDate.toLocalDate());
            }
            model.setWorkDescription(rs.getString("work_description"));
            model.setEmployeeName(rs.getString("employee_name"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<DailyWorklogModel> viewRecent(int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<DailyWorklogModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT w.id, w.project_id, w.work_date, w.employee_id, w.work_description, "
                        + "w.notes, p.name AS project_name, e.name AS employee_name "
                        + "FROM DailyWorklogs w "
                        + "INNER JOIN Projects p ON w.project_id = p.id "
                        + "LEFT JOIN Employees e ON w.employee_id = e.id "
                        + "ORDER BY w.work_date DESC, w.id DESC "
                        + "LIMIT ?");
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            DailyWorklogModel model = new DailyWorklogModel();
            model.setId(rs.getInt("id"));
            model.setProjectId(rs.getInt("project_id"));
            model.setProjectName(rs.getString("project_name"));

            Date workDate = rs.getDate("work_date");
            if (workDate != null) {
                model.setWorkDate(workDate.toLocalDate());
            }

            int employeeId = rs.getInt("employee_id");
            if (!rs.wasNull()) {
                model.setEmployeeId(employeeId);
            }
            model.setEmployeeName(rs.getString("employee_name"));
            model.setWorkDescription(rs.getString("work_description"));
            model.setNotes(rs.getString("notes"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }
}
