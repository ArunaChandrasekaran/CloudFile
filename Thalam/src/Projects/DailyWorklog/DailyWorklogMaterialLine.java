package Projects.DailyWorklog;

/**
 *
 * @author aruna
 */
public class DailyWorklogMaterialLine {

    private int materialId;
    private String materialName;
    private String unit;
    private double qty;
    private String remarks;

    public DailyWorklogMaterialLine() {
    }

    public DailyWorklogMaterialLine(int materialId, String unit, double qty, String remarks) {
        this.materialId = materialId;
        this.unit = unit;
        this.qty = qty;
        this.remarks = remarks;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
