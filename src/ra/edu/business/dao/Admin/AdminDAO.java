package ra.edu.business.dao.Admin;

import ra.edu.business.model.Admin;

public interface AdminDAO {
    Admin authenticate(String username, String password);
}