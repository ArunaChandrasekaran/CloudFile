package Materials.Purchases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class PurchaseDao {

    private static final String PURCHASE_EXPENSE_CATEGORY = "Materials Purchase";

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(PurchaseModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Purchases(purchase_date, project_id, vendor_id, grand_total, "
                            + "is_paid, payment_mode, notes) VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setDate(1, Date.valueOf(model.getPurchaseDate()));
            ps.setInt(2, model.getProjectId());
            ps.setInt(3, model.getVendorId());
            ps.setDouble(4, model.getGrandTotal());
            ps.setBoolean(5, model.isPaid());

            if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
                ps.setString(6, model.getPaymentMode());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setString(7, model.getNotes() == null ? "" : model.getNotes());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) {
                throw new SQLException("Failed to get generated purchase id");
            }
            int purchaseId = keys.getInt(1);
            keys.close();
            ps.close();
            model.setId(purchaseId);

            PreparedStatement pi = con.prepareStatement(
                    "INSERT INTO PurchaseItems(purchase_id, material_id, qty, unit_cost, amount) "
                            + "VALUES (?,?,?,?,?)");

            for (PurchaseItemModel item : model.getItems()) {
                pi.setInt(1, purchaseId);
                pi.setInt(2, item.getMaterialId());
                pi.setDouble(3, item.getQty());
                pi.setDouble(4, item.getUnitCost());
                pi.setDouble(5, item.getAmount());
                pi.addBatch();
            }
            pi.executeBatch();
            pi.close();

            upsertLinkedExpense(con, model);

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public void update(PurchaseModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE Purchases SET purchase_date = ?, project_id = ?, vendor_id = ?, "
                            + "grand_total = ?, is_paid = ?, payment_mode = ?, notes = ? "
                            + "WHERE id = ?");

            ps.setDate(1, Date.valueOf(model.getPurchaseDate()));
            ps.setInt(2, model.getProjectId());
            ps.setInt(3, model.getVendorId());
            ps.setDouble(4, model.getGrandTotal());
            ps.setBoolean(5, model.isPaid());

            if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
                ps.setString(6, model.getPaymentMode());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setString(7, model.getNotes() == null ? "" : model.getNotes());
            ps.setInt(8, model.getId());
            ps.executeUpdate();
            ps.close();

            PreparedStatement del = con.prepareStatement(
                    "DELETE FROM PurchaseItems WHERE purchase_id = ?");
            del.setInt(1, model.getId());
            del.executeUpdate();
            del.close();

            PreparedStatement pi = con.prepareStatement(
                    "INSERT INTO PurchaseItems(purchase_id, material_id, qty, unit_cost, amount) "
                            + "VALUES (?,?,?,?,?)");

            for (PurchaseItemModel item : model.getItems()) {
                pi.setInt(1, model.getId());
                pi.setInt(2, item.getMaterialId());
                pi.setDouble(3, item.getQty());
                pi.setDouble(4, item.getUnitCost());
                pi.setDouble(5, item.getAmount());
                pi.addBatch();
            }
            pi.executeBatch();
            pi.close();

            upsertLinkedExpense(con, model);

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
            PreparedStatement delExpense = con.prepareStatement(
                    "DELETE FROM Expenses WHERE purchase_id = ?");
            delExpense.setInt(1, id);
            delExpense.executeUpdate();
            delExpense.close();

            PreparedStatement delItems = con.prepareStatement(
                    "DELETE FROM PurchaseItems WHERE purchase_id = ?");
            delItems.setInt(1, id);
            delItems.executeUpdate();
            delItems.close();

            PreparedStatement del = con.prepareStatement("DELETE FROM Purchases WHERE id = ?");
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

    /**
     * Keeps a single Expenses row linked via purchase_id in sync with the purchase.
     */
    private void upsertLinkedExpense(Connection con, PurchaseModel model) throws SQLException {
        PreparedStatement find = con.prepareStatement(
                "SELECT id FROM Expenses WHERE purchase_id = ?");
        find.setInt(1, model.getId());
        ResultSet rs = find.executeQuery();
        boolean exists = rs.next();
        int expenseId = exists ? rs.getInt(1) : 0;
        rs.close();
        find.close();

        String notes = model.getNotes() == null ? "" : model.getNotes().trim();
        String vendorPart = model.getVendorName() == null || model.getVendorName().isBlank()
                ? ""
                : "Vendor: " + model.getVendorName().trim();
        String expenseNotes;
        if (notes.isEmpty()) {
            expenseNotes = vendorPart.isEmpty() ? "Purchase #" + model.getId() : vendorPart;
        } else if (vendorPart.isEmpty()) {
            expenseNotes = notes;
        } else {
            expenseNotes = vendorPart + " · " + notes;
        }

        if (exists) {
            PreparedStatement upd = con.prepareStatement(
                    "UPDATE Expenses SET expense_date = ?, expense_type = ?, project_id = ?, "
                            + "category = ?, amount = ?, is_paid = ?, payment_mode = ?, notes = ? "
                            + "WHERE id = ?");
            upd.setDate(1, Date.valueOf(model.getPurchaseDate()));
            upd.setString(2, "Project Expense");
            upd.setInt(3, model.getProjectId());
            upd.setString(4, PURCHASE_EXPENSE_CATEGORY);
            upd.setDouble(5, model.getGrandTotal());
            upd.setBoolean(6, model.isPaid());
            if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
                upd.setString(7, model.getPaymentMode());
            } else {
                upd.setNull(7, Types.VARCHAR);
            }
            upd.setString(8, expenseNotes);
            upd.setInt(9, expenseId);
            upd.executeUpdate();
            upd.close();
        } else {
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO Expenses(expense_date, expense_type, project_id, category, "
                            + "amount, is_paid, payment_mode, notes, worklog_id, purchase_id) "
                            + "VALUES (?,?,?,?,?,?,?,?,NULL,?)");
            ins.setDate(1, Date.valueOf(model.getPurchaseDate()));
            ins.setString(2, "Project Expense");
            ins.setInt(3, model.getProjectId());
            ins.setString(4, PURCHASE_EXPENSE_CATEGORY);
            ins.setDouble(5, model.getGrandTotal());
            ins.setBoolean(6, model.isPaid());
            if (model.getPaymentMode() != null && !model.getPaymentMode().isBlank()) {
                ins.setString(7, model.getPaymentMode());
            } else {
                ins.setNull(7, Types.VARCHAR);
            }
            ins.setString(8, expenseNotes);
            ins.setInt(9, model.getId());
            ins.executeUpdate();
            ins.close();
        }
    }

    /**
     * One-time backfill: create linked expenses for purchases that do not have one yet.
     */
    public int backfillMissingPurchaseExpenses() throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        con.setAutoCommit(false);
        int created = 0;
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT p.id, p.purchase_date, p.project_id, p.grand_total, p.is_paid, "
                            + "p.payment_mode, p.notes, v.name AS vendor_name "
                            + "FROM Purchases p "
                            + "INNER JOIN Vendors v ON v.id = p.vendor_id "
                            + "WHERE NOT EXISTS ("
                            + "  SELECT 1 FROM Expenses e WHERE e.purchase_id = p.id"
                            + ")");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PurchaseModel model = new PurchaseModel();
                model.setId(rs.getInt("id"));
                Date date = rs.getDate("purchase_date");
                if (date != null) {
                    model.setPurchaseDate(date.toLocalDate());
                }
                model.setProjectId(rs.getInt("project_id"));
                model.setGrandTotal(rs.getDouble("grand_total"));
                model.setPaid(rs.getBoolean("is_paid"));
                model.setPaymentMode(rs.getString("payment_mode"));
                model.setNotes(rs.getString("notes"));
                model.setVendorName(rs.getString("vendor_name"));
                upsertLinkedExpense(con, model);
                created++;
            }
            rs.close();
            ps.close();
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
        return created;
    }

    public ArrayList<PurchaseItemModel> viewItems(int purchaseId)
            throws ClassNotFoundException, SQLException {
        ArrayList<PurchaseItemModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT pi.id, pi.purchase_id, pi.material_id, pi.qty, pi.unit_cost, pi.amount, "
                        + "m.name AS material_name "
                        + "FROM PurchaseItems pi "
                        + "LEFT JOIN Materials m ON m.id = pi.material_id "
                        + "WHERE pi.purchase_id = ? ORDER BY pi.id");
        ps.setInt(1, purchaseId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PurchaseItemModel item = new PurchaseItemModel();
            item.setId(rs.getInt("id"));
            item.setPurchaseId(rs.getInt("purchase_id"));
            item.setMaterialId(rs.getInt("material_id"));
            item.setMaterialName(rs.getString("material_name"));
            item.setQty(rs.getDouble("qty"));
            item.setUnitCost(rs.getDouble("unit_cost"));
            item.setAmount(rs.getDouble("amount"));
            list.add(item);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<PurchaseModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<PurchaseModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.purchase_date, p.project_id, p.vendor_id, p.grand_total, "
                        + "p.is_paid, p.payment_mode, p.notes, "
                        + "pr.name AS project_name, v.name AS vendor_name "
                        + "FROM Purchases p "
                        + "INNER JOIN Projects pr ON p.project_id = pr.id "
                        + "INNER JOIN Vendors v ON p.vendor_id = v.id "
                        + "ORDER BY p.id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PurchaseModel model = new PurchaseModel();
            model.setId(rs.getInt("id"));

            Date date = rs.getDate("purchase_date");
            if (date != null) {
                model.setPurchaseDate(date.toLocalDate());
            }

            model.setProjectId(rs.getInt("project_id"));
            model.setProjectName(rs.getString("project_name"));
            model.setVendorId(rs.getInt("vendor_id"));
            model.setVendorName(rs.getString("vendor_name"));
            model.setGrandTotal(rs.getDouble("grand_total"));
            model.setPaid(rs.getBoolean("is_paid"));
            model.setPaymentMode(rs.getString("payment_mode"));
            model.setPaymentStatus(model.isPaid() ? "Paid" : "Unpaid");
            model.setNotes(rs.getString("notes"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<PurchaseModel> viewUnpaid(int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<PurchaseModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.purchase_date, p.project_id, p.vendor_id, p.grand_total, "
                        + "p.is_paid, p.payment_mode, p.notes, "
                        + "pr.name AS project_name, v.name AS vendor_name "
                        + "FROM Purchases p "
                        + "INNER JOIN Projects pr ON p.project_id = pr.id "
                        + "INNER JOIN Vendors v ON p.vendor_id = v.id "
                        + "WHERE p.is_paid = FALSE "
                        + "ORDER BY p.purchase_date ASC NULLS LAST, p.id ASC "
                        + "LIMIT ?");
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PurchaseModel model = new PurchaseModel();
            model.setId(rs.getInt("id"));
            Date date = rs.getDate("purchase_date");
            if (date != null) {
                model.setPurchaseDate(date.toLocalDate());
            }
            model.setProjectId(rs.getInt("project_id"));
            model.setProjectName(rs.getString("project_name"));
            model.setVendorId(rs.getInt("vendor_id"));
            model.setVendorName(rs.getString("vendor_name"));
            model.setGrandTotal(rs.getDouble("grand_total"));
            model.setPaid(rs.getBoolean("is_paid"));
            model.setPaymentMode(rs.getString("payment_mode"));
            model.setPaymentStatus("Unpaid");
            model.setNotes(rs.getString("notes"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public double getUnpaidTotalAmount() throws ClassNotFoundException, SQLException {
        double amount = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(grand_total), 0) FROM Purchases WHERE is_paid = FALSE");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            amount = rs.getDouble(1);
        }

        rs.close();
        ps.close();
        con.close();
        return amount;
    }

    public ArrayList<PurchaseModel> viewRecentByVendor(int vendorId, int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<PurchaseModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.purchase_date, p.grand_total, pr.name AS project_name "
                        + "FROM Purchases p "
                        + "INNER JOIN Projects pr ON p.project_id = pr.id "
                        + "WHERE p.vendor_id = ? "
                        + "ORDER BY p.id DESC LIMIT ?");
        ps.setInt(1, vendorId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            PurchaseModel model = new PurchaseModel();
            model.setId(rs.getInt("id"));
            Date date = rs.getDate("purchase_date");
            if (date != null) {
                model.setPurchaseDate(date.toLocalDate());
            }
            model.setGrandTotal(rs.getDouble("grand_total"));
            model.setProjectName(rs.getString("project_name"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<PurchaseModel> viewRecentByProject(int projectId, int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<PurchaseModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.purchase_date, p.grand_total, v.name AS vendor_name "
                        + "FROM Purchases p "
                        + "INNER JOIN Vendors v ON p.vendor_id = v.id "
                        + "WHERE p.project_id = ? "
                        + "ORDER BY p.id DESC LIMIT ?");
        ps.setInt(1, projectId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            PurchaseModel model = new PurchaseModel();
            model.setId(rs.getInt("id"));
            Date date = rs.getDate("purchase_date");
            if (date != null) {
                model.setPurchaseDate(date.toLocalDate());
            }
            model.setGrandTotal(rs.getDouble("grand_total"));
            model.setVendorName(rs.getString("vendor_name"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }
}
