package Clients;

import java.sql.SQLException;

/**
 *
 * @author aruna
 */
public class ClientsService {

    ClientsDao cd = new ClientsDao();

    public void insertService(ClientsModel c) throws SQLException, ClassNotFoundException {
        cd.insert(c);
    }

    public void updateService(ClientsModel c) throws SQLException, ClassNotFoundException {
        cd.update(c);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        cd.delete(id);
    }
}
