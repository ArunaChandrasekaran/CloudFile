package Reports;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Report queries filtered by optional project and date range.
 */
public class ReportsDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public static class NamedAmount {
        public final String name;
        public final double amount;

        public NamedAmount(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    public List<NamedAmount> expensesByCategory(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(NULLIF(TRIM(category), ''), 'Uncategorized') AS label, "
                        + "COALESCE(SUM(amount), 0) AS total "
                        + "FROM Expenses "
                        + "WHERE expense_date >= ? AND expense_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        sql.append("GROUP BY label ORDER BY total DESC");

        return queryNamedAmounts(sql.toString(), projectId, from, to);
    }

    public List<NamedAmount> invoicesByMonth(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT TO_CHAR(invoice_date, 'Mon YYYY') AS label, "
                        + "COALESCE(SUM(invoice_amount), 0) AS total "
                        + "FROM Invoices "
                        + "WHERE invoice_date >= ? AND invoice_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        sql.append("GROUP BY date_trunc('month', invoice_date), label "
                + "ORDER BY date_trunc('month', invoice_date)");

        return queryNamedAmounts(sql.toString(), projectId, from, to);
    }

    public double totalInvoiceAmount(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(SUM(invoice_amount), 0) FROM Invoices "
                        + "WHERE invoice_date >= ? AND invoice_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        return querySingleAmount(sql.toString(), projectId, from, to);
    }

    public double totalExpenseAmount(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(SUM(amount), 0) FROM Expenses "
                        + "WHERE expense_date >= ? AND expense_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        return querySingleAmount(sql.toString(), projectId, from, to);
    }

    public List<NamedAmount> purchasesByMonth(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT TO_CHAR(purchase_date, 'Mon YYYY') AS label, "
                        + "COALESCE(SUM(grand_total), 0) AS total "
                        + "FROM Purchases "
                        + "WHERE purchase_date >= ? AND purchase_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        sql.append("GROUP BY date_trunc('month', purchase_date), label "
                + "ORDER BY date_trunc('month', purchase_date)");

        return queryNamedAmounts(sql.toString(), projectId, from, to);
    }

    public double totalPurchaseAmount(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(SUM(grand_total), 0) FROM Purchases "
                        + "WHERE purchase_date >= ? AND purchase_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        return querySingleAmount(sql.toString(), projectId, from, to);
    }

    /**
     * Company spend by month from Expenses only (includes purchase-linked rows via purchase_id).
     */
    public Map<String, Double> spendByMonth(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        Map<String, Double> map = new LinkedHashMap<>();
        addMonthAmounts(map, expensesByMonth(projectId, from, to));
        return map;
    }

    public List<NamedAmount> expensesByMonth(Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT TO_CHAR(expense_date, 'Mon YYYY') AS label, "
                        + "COALESCE(SUM(amount), 0) AS total "
                        + "FROM Expenses "
                        + "WHERE expense_date >= ? AND expense_date <= ? ");
        if (projectId != null) {
            sql.append("AND project_id = ? ");
        }
        sql.append("GROUP BY date_trunc('month', expense_date), label "
                + "ORDER BY date_trunc('month', expense_date)");
        return queryNamedAmounts(sql.toString(), projectId, from, to);
    }

    private static void addMonthAmounts(Map<String, Double> map, List<NamedAmount> rows) {
        for (NamedAmount row : rows) {
            map.merge(row.name, row.amount, Double::sum);
        }
    }

    /**
     * Progress = worklog count / planned days (start→end, inclusive), capped at 100%.
     * When {@code projectId} is null, returns the average across projects that have both dates.
     */
    public ProjectProgress projectProgress(Integer projectId)
            throws ClassNotFoundException, SQLException {
        String sql = projectId != null
                ? "SELECT p.id, p.startdate, p.enddate, "
                        + "COALESCE((SELECT COUNT(*) FROM DailyWorklogs w WHERE w.project_id = p.id), 0) AS worklog_count "
                        + "FROM Projects p WHERE p.id = ?"
                : "SELECT p.id, p.startdate, p.enddate, "
                        + "COALESCE((SELECT COUNT(*) FROM DailyWorklogs w WHERE w.project_id = p.id), 0) AS worklog_count "
                        + "FROM Projects p "
                        + "WHERE p.startdate IS NOT NULL AND p.enddate IS NOT NULL";

        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(sql);
        if (projectId != null) {
            ps.setInt(1, projectId);
        }
        ResultSet rs = ps.executeQuery();

        long totalPlannedDays = 0;
        long totalWorklogs = 0;
        int projectRows = 0;
        double percentSum = 0;

        while (rs.next()) {
            java.sql.Date startSql = rs.getDate("startdate");
            java.sql.Date endSql = rs.getDate("enddate");
            if (startSql == null || endSql == null) {
                continue;
            }
            LocalDate start = startSql.toLocalDate();
            LocalDate end = endSql.toLocalDate();
            if (end.isBefore(start)) {
                continue;
            }
            long plannedDays = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
            if (plannedDays < 1) {
                plannedDays = 1;
            }
            long worklogs = rs.getLong("worklog_count");
            double pct = Math.min(100.0, (worklogs * 100.0) / plannedDays);

            totalPlannedDays += plannedDays;
            totalWorklogs += worklogs;
            percentSum += pct;
            projectRows++;
        }

        rs.close();
        ps.close();
        con.close();

        if (projectRows == 0) {
            return new ProjectProgress(0, 0, 0);
        }

        if (projectId != null) {
            double pct = Math.min(100.0, (totalWorklogs * 100.0) / Math.max(totalPlannedDays, 1));
            return new ProjectProgress(totalPlannedDays, totalWorklogs, pct);
        }

        double avgPct = percentSum / projectRows;
        return new ProjectProgress(totalPlannedDays, totalWorklogs, avgPct);
    }

    public static final class ProjectProgress {
        public final long plannedDays;
        public final long worklogCount;
        public final double percent;

        public ProjectProgress(long plannedDays, long worklogCount, double percent) {
            this.plannedDays = plannedDays;
            this.worklogCount = worklogCount;
            this.percent = percent;
        }
    }

    /** Budget for reports = 70% of project contract amount(s). */
    public double contractBudget(Integer projectId) throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps;
        if (projectId != null) {
            ps = con.prepareStatement(
                    "SELECT COALESCE(SUM(contractamount), 0) FROM Projects WHERE id = ?");
            ps.setInt(1, projectId);
        } else {
            ps = con.prepareStatement("SELECT COALESCE(SUM(contractamount), 0) FROM Projects");
        }
        ResultSet rs = ps.executeQuery();
        double amount = 0;
        if (rs.next()) {
            amount = rs.getDouble(1);
        }
        rs.close();
        ps.close();
        con.close();
        return amount * 0.70;
    }

    private List<NamedAmount> queryNamedAmounts(
            String sql, Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        List<NamedAmount> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(sql);
        bindProjectAndDates(ps, projectId, from, to);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new NamedAmount(rs.getString("label"), rs.getDouble("total")));
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    private double querySingleAmount(String sql, Integer projectId, LocalDate from, LocalDate to)
            throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(sql);
        bindProjectAndDates(ps, projectId, from, to);
        ResultSet rs = ps.executeQuery();
        double amount = 0;
        if (rs.next()) {
            amount = rs.getDouble(1);
        }
        rs.close();
        ps.close();
        con.close();
        return amount;
    }

    private void bindProjectAndDates(
            PreparedStatement ps, Integer projectId, LocalDate from, LocalDate to)
            throws SQLException {
        ps.setDate(1, Date.valueOf(from));
        ps.setDate(2, Date.valueOf(to));
        if (projectId != null) {
            ps.setInt(3, projectId);
        }
    }
}
