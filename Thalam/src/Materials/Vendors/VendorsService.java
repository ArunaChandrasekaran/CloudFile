package Materials.Vendors;

import java.sql.SQLException;

public class VendorsService {
    
    
    VendorsDao vd = new VendorsDao();
    public void insertService(VendorsModel v) throws SQLException, ClassNotFoundException
    {
       vd.insert(v);
    }

    public void updateService(VendorsModel v) throws SQLException, ClassNotFoundException {
        vd.update(v);
    }

    public void deleteService(int id) throws SQLException, ClassNotFoundException {
        vd.delete(id);
    }
    
}
