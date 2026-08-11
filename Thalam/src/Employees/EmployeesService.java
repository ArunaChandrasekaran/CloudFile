package Employees;

import java.sql.SQLException;

public class EmployeesService {
    
    EmployeesDao dao=new EmployeesDao();
    
     public void insertService(EmployeesModel model) throws ClassNotFoundException, SQLException
    {
      
        dao.insert(model);
    
    }

    public void updateService(EmployeesModel model) throws ClassNotFoundException, SQLException {
        dao.update(model);
    }

    public void deleteService(int id) throws ClassNotFoundException, SQLException {
        dao.delete(id);
    }
    
}
