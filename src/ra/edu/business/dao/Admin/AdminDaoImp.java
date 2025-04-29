package ra.edu.business.dao.Admin;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Admin;

import java.sql.*;

public class AdminDaoImp implements AdminDAO {
    @Override
    public Admin authenticate(String username, String password) {
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall("{CALL admin_login(?,?,?)}")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            int accountId = stmt.getInt(3);
            if (accountId > 0) {
                Admin admin = new Admin();
                admin.setAccountId(accountId);
                admin.setAccountName(username);
                admin.setPassword(password);
                return admin;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng nhập: " + e.getMessage());
        }
        return null;
    }
}