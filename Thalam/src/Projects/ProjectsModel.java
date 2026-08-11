package Projects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aruna
 */
public class ProjectsModel {

    private int id;
    private String projectName;
    private String phone;
    private int clientId;
    private String client;
    private List<Integer> employeeIds = new ArrayList<>();
    private LocalDate starDate;
    private LocalDate expectedEndDate;
    private String address;
    private double contractAmount;
    private String notes;
    private String status;

    public ProjectsModel() {
    }

    public ProjectsModel(
            String projectName,
            int clientId,
            List<Integer> employeeIds,
            LocalDate starDate,
            LocalDate expectedEndDate,
            String address,
            double contractAmount,
            String notes,
            String status) {
        this.projectName = projectName;
        this.clientId = clientId;
        this.employeeIds = employeeIds != null ? employeeIds : new ArrayList<>();
        this.starDate = starDate;
        this.expectedEndDate = expectedEndDate;
        this.address = address;
        this.contractAmount = contractAmount;
        this.notes = notes;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds != null ? employeeIds : new ArrayList<>();
    }

    public LocalDate getStarDate() {
        return starDate;
    }

    public void setStarDate(LocalDate starDate) {
        this.starDate = starDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final String STATUS_NOT_STARTED = "Not yet started";
    public static final String STATUS_ONGOING = "Ongoing";
    public static final String STATUS_LATE = "Late";
    public static final String STATUS_COMPLETED = "Completed";

    /**
     * Completed stays until marked otherwise.
     * Not yet started: today before start date.
     * Late: end date passed and not completed.
     * Ongoing: started and not past end.
     */
    public static String resolveStatus(String currentStatus, LocalDate startDate, LocalDate endDate) {
        if (STATUS_COMPLETED.equalsIgnoreCase(currentStatus)) {
            return STATUS_COMPLETED;
        }
        LocalDate today = LocalDate.now();
        if (startDate != null && today.isBefore(startDate)) {
            return STATUS_NOT_STARTED;
        }
        if (endDate != null && today.isAfter(endDate)) {
            return STATUS_LATE;
        }
        return STATUS_ONGOING;
    }

    public void applyResolvedStatus() {
        setStatus(resolveStatus(status, starDate, expectedEndDate));
    }

    /** CSS key: late | ongoing | not-started | completed */
    public String getStatusKey() {
        if (STATUS_LATE.equalsIgnoreCase(status)) {
            return "late";
        }
        if (STATUS_ONGOING.equalsIgnoreCase(status)) {
            return "ongoing";
        }
        if (STATUS_NOT_STARTED.equalsIgnoreCase(status)) {
            return "not-started";
        }
        if (STATUS_COMPLETED.equalsIgnoreCase(status)) {
            return "completed";
        }
        return "ongoing";
    }
}
