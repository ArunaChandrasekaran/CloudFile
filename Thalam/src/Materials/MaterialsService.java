package Materials;

import java.sql.SQLException;

/**
 *
 * @author aruna
 */
public class MaterialsService {

    private final MaterialsDao dao = new MaterialsDao();

    public void insertService(MaterialsModel model) throws SQLException, ClassNotFoundException {
        dao.insert(model);
    }
}
