package Projects;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class ProjectsService {

    ProjectDao dao = new ProjectDao();

    public void insertService(ProjectsModel model) throws SQLException, ClassNotFoundException {
        if (model.getEmployeeIds() == null || model.getEmployeeIds().isEmpty()) {
            throw new SQLException("Select at least one associated employee");
        }
        if (model.getExpectedEndDate() == null) {
            throw new SQLException("End date is required");
        }
        model.applyResolvedStatus();
        dao.insert(model);
    }

    public void updateService(ProjectsModel model) throws SQLException, ClassNotFoundException {
        if (model.getId() <= 0) {
            throw new SQLException("Project id is required for update");
        }
        if (model.getEmployeeIds() == null || model.getEmployeeIds().isEmpty()) {
            throw new SQLException("Select at least one associated employee");
        }
        if (model.getExpectedEndDate() == null) {
            throw new SQLException("End date is required");
        }
        model.applyResolvedStatus();
        dao.update(model);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        dao.delete(id);
    }

    public void markCompletedService(int id) throws SQLException, ClassNotFoundException {
        if (id <= 0) {
            throw new SQLException("Project id is required");
        }
        dao.markCompleted(id);
    }

    public ArrayList<ProjectsModel> viewService() throws ClassNotFoundException, SQLException {
        return dao.view();
    }
}
