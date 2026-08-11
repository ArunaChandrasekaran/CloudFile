package Materials;

public class MaterialsModel {

    private int id;
    private String name;
    private String unit;
    private String notes;
    private double purchasedStock;
    private double currentStock;
    /** How many usage-days current stock will last at avg qty per worklog usage. */
    private double daysOfCover;

    public MaterialsModel() {
    }

    public MaterialsModel(String name, String unit, String notes) {
        this.name = name;
        this.unit = unit;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getPurchasedStock() {
        return purchasedStock;
    }

    public void setPurchasedStock(double purchasedStock) {
        this.purchasedStock = purchasedStock;
    }

    public double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(double currentStock) {
        this.currentStock = currentStock;
    }

    public double getDaysOfCover() {
        return daysOfCover;
    }

    public void setDaysOfCover(double daysOfCover) {
        this.daysOfCover = daysOfCover;
    }

    /**
     * Usage-based status (days of cover = current ÷ avg qty per worklog that used it):
     * Out ≤ 0, Low &lt; 5, Near Low 5–10, In Stock &gt; 10.
     * No usage with positive stock is treated as In Stock.
     */
    public String getStockStatus() {
        if (currentStock <= 0 || daysOfCover <= 0) {
            return "Out of Stock";
        }
        if (daysOfCover < 5) {
            return "Low Stock";
        }
        if (daysOfCover <= 10) {
            return "Near Low Stock";
        }
        return "In Stock";
    }

    /** CSS key: out | low | near | in */
    public String getStockStatusKey() {
        String status = getStockStatus();
        if ("Out of Stock".equals(status)) {
            return "out";
        }
        if ("Low Stock".equals(status)) {
            return "low";
        }
        if ("Near Low Stock".equals(status)) {
            return "near";
        }
        return "in";
    }
}
