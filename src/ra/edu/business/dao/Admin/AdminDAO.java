package ra.edu.business.dao.Admin;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.Admin;

import java.sql.*;

public interface AdminDAO extends AppDAO {
    public default Admin authenticate(String username, String password){
        return null;
    };
}