package Projects;

/**
 * One row on the Projects list.
 */
public record ProjectListItem(String projectId, String projectName, String client, String startDate, String expectedEndDate, int associateEmployees, String status)
{

    public ProjectListItem(
        String projectId,
        String projectName,
        String client,
        String startDate,
        String expectedEndDate,
        int associateEmployees,
        String status)
    {
        this.projectId = projectId == null ? "" : projectId;
        this.projectName = projectName == null ? "" : projectName;
        this.client = client == null ? "" : client;
        this.startDate = startDate == null ? "" : startDate;
        this.expectedEndDate = expectedEndDate == null ? "" : expectedEndDate;
        this.associateEmployees = Math.max(0, associateEmployees);
        this.status = status == null ? "" : status;
    }
}
