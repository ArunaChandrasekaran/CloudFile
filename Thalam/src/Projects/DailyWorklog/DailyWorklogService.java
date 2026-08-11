package Projects.DailyWorklog;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class DailyWorklogService {

    DailyWorklogDao dao = new DailyWorklogDao();

    public void insertService(DailyWorklogModel model) throws SQLException, ClassNotFoundException {
        validate(model);
        dao.insert(model);
    }

    public void updateService(DailyWorklogModel model) throws SQLException, ClassNotFoundException {
        if (model.getId() <= 0) {
            throw new SQLException("Worklog id is required for update");
        }
        validate(model);
        dao.update(model);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        dao.delete(id);
    }

    public ArrayList<DailyWorklogModel> viewService() throws ClassNotFoundException, SQLException {
        return dao.view();
    }

    private void validate(DailyWorklogModel model) throws SQLException {
        if (model.getProjectId() <= 0) {
            throw new SQLException("Select a project");
        }
        if (model.getWorkDate() == null) {
            throw new SQLException("Work date is required");
        }
        if (model.getWorkDescription() == null || model.getWorkDescription().isBlank()) {
            throw new SQLException("Today's work description is required");
        }
    }
}
