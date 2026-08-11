package Materials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialsDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(MaterialsModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Materials(name, unit, notes) VALUES (?,?,?)");

        ps.setString(1, model.getName());
        ps.setString(2, model.getUnit());
        ps.setString(3, model.getNotes() == null ? "" : model.getNotes());

        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public ArrayList<MaterialsModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<MaterialsModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT id, name, unit, notes FROM Materials ORDER BY id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            MaterialsModel model = new MaterialsModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setUnit(rs.getString("unit"));
            model.setNotes(rs.getString("notes"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    /**
     * Project materials with purchased/current stock. Status is computed in Java
     * from usage qty and how many worklogs used each material.
     */
    public ArrayList<MaterialsModel> viewByProject(int projectId)
            throws ClassNotFoundException, SQLException {
        ArrayList<MaterialsModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT m.id, m.name, m.unit, "
                        + "COALESCE(p_sum.purchased_stock, 0) AS purchased_stock, "
                        + "COALESCE(p_sum.purchased_stock, 0) - COALESCE(u_sum.used_qty, 0) AS current_stock, "
                        + "COALESCE(u_sum.used_qty, 0) AS used_qty, "
                        + "COALESCE(u_sum.usage_worklog_count, 0) AS usage_worklog_count "
                        + "FROM Materials m "
                        + "INNER JOIN ( "
                        + "  SELECT pi.material_id, SUM(pi.qty) AS purchased_stock "
                        + "  FROM Purchases p "
                        + "  INNER JOIN PurchaseItems pi ON pi.purchase_id = p.id "
                        + "  WHERE p.project_id = ? "
                        + "  GROUP BY pi.material_id "
                        + ") p_sum ON p_sum.material_id = m.id "
                        + "LEFT JOIN ( "
                        + "  SELECT dwm.material_id, SUM(dwm.qty) AS used_qty, "
                        + "         COUNT(DISTINCT dwm.worklog_id) AS usage_worklog_count "
                        + "  FROM DailyWorklogMaterials dwm "
                        + "  INNER JOIN DailyWorklogs w ON w.id = dwm.worklog_id "
                        + "  WHERE w.project_id = ? "
                        + "  GROUP BY dwm.material_id "
                        + ") u_sum ON u_sum.material_id = m.id "
                        + "ORDER BY m.id");
        ps.setInt(1, projectId);
        ps.setInt(2, projectId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            MaterialsModel model = new MaterialsModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setUnit(rs.getString("unit"));
            model.setPurchasedStock(rs.getDouble("purchased_stock"));
            double currentStock = rs.getDouble("current_stock");
            model.setCurrentStock(currentStock);
            model.setDaysOfCover(daysOfCover(
                    currentStock,
                    rs.getDouble("used_qty"),
                    rs.getInt("usage_worklog_count")));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    /**
     * Counts materials by status using the same Java rules as the Materials list.
     */
    public MaterialStockCounts countStockByStatus(int projectId)
            throws ClassNotFoundException, SQLException {
        int outOfStock = 0;
        int lowStock = 0;
        int nearLowStock = 0;
        int inStock = 0;

        for (MaterialsModel material : viewByProject(projectId)) {
            switch (material.getStockStatus()) {
                case "Out of Stock":
                    outOfStock++;
                    break;
                case "Low Stock":
                    lowStock++;
                    break;
                case "Near Low Stock":
                    nearLowStock++;
                    break;
                default:
                    inStock++;
                    break;
            }
        }

        return new MaterialStockCounts(outOfStock, lowStock, nearLowStock, inStock);
    }

    /**
     * days of cover = current stock ÷ (total used ÷ worklogs that used this material)
     */
    static double daysOfCover(double currentStock, double usedQty, int usageWorklogCount) {
        if (currentStock <= 0) {
            return 0;
        }
        if (usedQty <= 0 || usageWorklogCount <= 0) {
            // Bought but never used in a worklog → treat as fully in stock
            return Double.POSITIVE_INFINITY;
        }
        double avgPerWorklog = usedQty / usageWorklogCount;
        return currentStock / avgPerWorklog;
    }

    public static final class MaterialStockCounts {
        public final int outOfStock;
        public final int lowStock;
        public final int nearLowStock;
        public final int inStock;

        public MaterialStockCounts(int outOfStock, int lowStock, int nearLowStock, int inStock) {
            this.outOfStock = outOfStock;
            this.lowStock = lowStock;
            this.nearLowStock = nearLowStock;
            this.inStock = inStock;
        }
    }
}
