package Invoices;

import java.time.LocalDate;

/**
 *
 * @author aruna
 */
public class InvoicesModel {

    private int id;
    private int projectId;
    private String projectName;
    private String clientName;
    private String invoicePurpose;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private double invoiceAmount;
    private LocalDate paymentDate;
    private String status;
    private boolean paid;
    private String paymentMode;
    private String notes;

    public InvoicesModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getInvoicePurpose() {
        return invoicePurpose;
    }

    public void setInvoicePurpose(String invoicePurpose) {
        this.invoicePurpose = invoicePurpose;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /** Paid / Overdue / Pending from payment flag and due date. */
    public static String resolveStatus(boolean paid, LocalDate dueDate) {
        if (paid) {
            return "Paid";
        }
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            return "Overdue";
        }
        return "Pending";
    }

    public void applyResolvedStatus() {
        setStatus(resolveStatus(paid, dueDate));
    }
}
