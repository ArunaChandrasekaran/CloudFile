package Invoices;

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
public class InvoicesDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(InvoicesModel model) throws SQLException, ClassNotFoundException {
        model.applyResolvedStatus();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Invoices(project_id, invoice_purpose, invoice_date, due_date, "
                        + "invoice_amount, payment_date, status, is_paid, payment_mode, notes) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)");

        ps.setInt(1, model.getProjectId());
        ps.setString(2, model.getInvoicePurpose());
        ps.setDate(3, Date.valueOf(model.getInvoiceDate()));

        if (model.getDueDate() != null) {
            ps.setDate(4, Date.valueOf(model.getDueDate()));
        } else {
            ps.setNull(4, Types.DATE);
        }

        ps.setDouble(5, model.getInvoiceAmount());

        if (model.getPaymentDate() != null) {
            ps.setDate(6, Date.valueOf(model.getPaymentDate()));
        } else {
            ps.setNull(6, Types.DATE);
        }

        ps.setString(7, model.getStatus());
        ps.setBoolean(8, model.isPaid());

        if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
            ps.setString(9, model.getPaymentMode());
        } else {
            ps.setNull(9, Types.VARCHAR);
        }

        ps.setString(10, model.getNotes() == null ? "" : model.getNotes());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void update(InvoicesModel model) throws SQLException, ClassNotFoundException {
        model.applyResolvedStatus();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Invoices SET project_id = ?, invoice_purpose = ?, invoice_date = ?, due_date = ?, "
                        + "invoice_amount = ?, payment_date = ?, status = ?, is_paid = ?, payment_mode = ?, "
                        + "notes = ? WHERE id = ?");

        ps.setInt(1, model.getProjectId());
        ps.setString(2, model.getInvoicePurpose());
        ps.setDate(3, Date.valueOf(model.getInvoiceDate()));

        if (model.getDueDate() != null) {
            ps.setDate(4, Date.valueOf(model.getDueDate()));
        } else {
            ps.setNull(4, Types.DATE);
        }

        ps.setDouble(5, model.getInvoiceAmount());

        if (model.getPaymentDate() != null) {
            ps.setDate(6, Date.valueOf(model.getPaymentDate()));
        } else {
            ps.setNull(6, Types.DATE);
        }

        ps.setString(7, model.getStatus());
        ps.setBoolean(8, model.isPaid());

        if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
            ps.setString(9, model.getPaymentMode());
        } else {
            ps.setNull(9, Types.VARCHAR);
        }

        ps.setString(10, model.getNotes() == null ? "" : model.getNotes());
        ps.setInt(11, model.getId());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Invoices WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public ArrayList<InvoicesModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<InvoicesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        refreshStatuses(con);
        PreparedStatement ps = con.prepareStatement(
                "SELECT i.id, i.project_id, i.invoice_purpose, i.invoice_date, i.due_date, "
                        + "i.invoice_amount, i.payment_date, i.status, i.is_paid, i.payment_mode, "
                        + "i.notes, p.name AS project_name "
                        + "FROM Invoices i "
                        + "INNER JOIN Projects p ON i.project_id = p.id "
                        + "ORDER BY i.id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            InvoicesModel model = new InvoicesModel();
            model.setId(rs.getInt("id"));
            model.setProjectId(rs.getInt("project_id"));
            model.setProjectName(rs.getString("project_name"));
            model.setInvoicePurpose(rs.getString("invoice_purpose"));

            Date invoiceDate = rs.getDate("invoice_date");
            if (invoiceDate != null) {
                model.setInvoiceDate(invoiceDate.toLocalDate());
            }

            Date dueDate = rs.getDate("due_date");
            if (dueDate != null) {
                model.setDueDate(dueDate.toLocalDate());
            }

            model.setInvoiceAmount(rs.getDouble("invoice_amount"));

            Date paymentDate = rs.getDate("payment_date");
            if (paymentDate != null) {
                model.setPaymentDate(paymentDate.toLocalDate());
            }

            model.setPaid(rs.getBoolean("is_paid"));
            model.setPaymentMode(rs.getString("payment_mode"));
            model.setNotes(rs.getString("notes"));
            model.applyResolvedStatus();
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    /** Keep status column aligned: Paid / Overdue / Pending. */
    private void refreshStatuses(Connection con) throws SQLException {
        try (PreparedStatement paid = con.prepareStatement(
                "UPDATE Invoices SET status = 'Paid' WHERE is_paid = TRUE AND status IS DISTINCT FROM 'Paid'")) {
            paid.executeUpdate();
        }
        try (PreparedStatement overdue = con.prepareStatement(
                "UPDATE Invoices SET status = 'Overdue' "
                        + "WHERE is_paid = FALSE "
                        + "AND due_date IS NOT NULL AND due_date < CURRENT_DATE "
                        + "AND status IS DISTINCT FROM 'Overdue'")) {
            overdue.executeUpdate();
        }
        try (PreparedStatement pending = con.prepareStatement(
                "UPDATE Invoices SET status = 'Pending' "
                        + "WHERE is_paid = FALSE "
                        + "AND (due_date IS NULL OR due_date >= CURRENT_DATE) "
                        + "AND status IS DISTINCT FROM 'Pending'")) {
            pending.executeUpdate();
        }
    }

    public ArrayList<InvoicesModel> viewRecentByProject(int projectId, int limit)
            throws ClassNotFoundException, SQLException {
        return viewRecent("WHERE i.project_id = ?", projectId, limit);
    }

    public ArrayList<InvoicesModel> viewRecentByClient(int clientId, int limit)
            throws ClassNotFoundException, SQLException {
        return viewRecent("WHERE p.client_id = ?", clientId, limit);
    }

    private ArrayList<InvoicesModel> viewRecent(String whereClause, int id, int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<InvoicesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT i.id, i.invoice_purpose, i.invoice_amount, i.status, p.name AS project_name "
                        + "FROM Invoices i "
                        + "INNER JOIN Projects p ON i.project_id = p.id "
                        + whereClause + " "
                        + "ORDER BY i.id DESC LIMIT ?");
        ps.setInt(1, id);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            InvoicesModel model = new InvoicesModel();
            model.setId(rs.getInt("id"));
            model.setInvoicePurpose(rs.getString("invoice_purpose"));
            model.setInvoiceAmount(rs.getDouble("invoice_amount"));
            model.setStatus(rs.getString("status"));
            model.setProjectName(rs.getString("project_name"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<InvoicesModel> viewOverdue(int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<InvoicesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        refreshStatuses(con);
        PreparedStatement ps = con.prepareStatement(
                "SELECT i.id, i.project_id, i.invoice_purpose, i.invoice_date, i.due_date, "
                        + "i.invoice_amount, i.payment_date, i.status, i.is_paid, i.payment_mode, "
                        + "i.notes, p.name AS project_name, c.name AS client_name "
                        + "FROM Invoices i "
                        + "INNER JOIN Projects p ON i.project_id = p.id "
                        + "INNER JOIN Clients c ON p.client_id = c.id "
                        + "WHERE i.is_paid = FALSE "
                        + "AND i.due_date IS NOT NULL "
                        + "AND i.due_date < CURRENT_DATE "
                        + "ORDER BY i.due_date ASC, i.id ASC "
                        + "LIMIT ?");
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(mapInvoice(rs));
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public double getOverdueTotalAmount() throws ClassNotFoundException, SQLException {
        double amount = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(invoice_amount), 0) FROM Invoices "
                        + "WHERE is_paid = FALSE "
                        + "AND due_date IS NOT NULL "
                        + "AND due_date < CURRENT_DATE");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            amount = rs.getDouble(1);
        }

        rs.close();
        ps.close();
        con.close();
        return amount;
    }

    private InvoicesModel mapInvoice(ResultSet rs) throws SQLException {
        InvoicesModel model = new InvoicesModel();
        model.setId(rs.getInt("id"));
        model.setProjectId(rs.getInt("project_id"));
        model.setProjectName(rs.getString("project_name"));
        model.setClientName(rs.getString("client_name"));
        model.setInvoicePurpose(rs.getString("invoice_purpose"));

        Date invoiceDate = rs.getDate("invoice_date");
        if (invoiceDate != null) {
            model.setInvoiceDate(invoiceDate.toLocalDate());
        }

        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            model.setDueDate(dueDate.toLocalDate());
        }

        model.setInvoiceAmount(rs.getDouble("invoice_amount"));

        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            model.setPaymentDate(paymentDate.toLocalDate());
        }

        model.setStatus(rs.getString("status"));
        model.setPaid(rs.getBoolean("is_paid"));
        model.setPaymentMode(rs.getString("payment_mode"));
        model.setNotes(rs.getString("notes"));
        return model;
    }
}
