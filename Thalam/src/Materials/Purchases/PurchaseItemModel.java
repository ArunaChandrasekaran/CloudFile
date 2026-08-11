package Materials.Purchases;

/**
 *
 * @author aruna
 */
public class PurchaseItemModel {

    private int id;
    private int purchaseId;
    private int materialId;
    private String materialName;
    private double qty;
    private double unitCost;
    private double amount;

    public PurchaseItemModel() {
    }

    public PurchaseItemModel(int materialId, double qty, double unitCost, double amount) {
        this.materialId = materialId;
        this.qty = qty;
        this.unitCost = unitCost;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
