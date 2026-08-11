package Projects.DailyWorklog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aruna
 */
public class DailyWorklogModel {

    private int id;
    private int projectId;
    private String projectName;
    private LocalDate workDate;
    private Integer employeeId;
    private String employeeName;
    private String workDescription;
    private String notes;
    private List<DailyWorklogExpenseLine> expenses = new ArrayList<>();
    private List<DailyWorklogMaterialLine> materials = new ArrayList<>();

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

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<DailyWorklogExpenseLine> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<DailyWorklogExpenseLine> expenses) {
        this.expenses = expenses != null ? expenses : new ArrayList<>();
    }

    public List<DailyWorklogMaterialLine> getMaterials() {
        return materials;
    }

    public void setMaterials(List<DailyWorklogMaterialLine> materials) {
        this.materials = materials != null ? materials : new ArrayList<>();
    }
}
