package Employees.Roles;

import java.sql.SQLException;

public class RoleService {
    
    RoleDao daoObject=new RoleDao();
    
     public void insertService(RoleModel modelObject) throws SQLException, ClassNotFoundException
    {
      
        daoObject.insert(modelObject);
    
    }

    public void updateService(RoleModel modelObject) throws SQLException, ClassNotFoundException {
        daoObject.update(modelObject);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        daoObject.delete(id);
    }
    
    
}
