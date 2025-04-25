package ra.edu.business.dao.Admin;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Admin;

import java.sql.*;

public class AdminDaoImp implements AdminDAO {
    @Override
    public Admin authenticate(String username, String password) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call admin_login(?, ?, ?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);
            callSt.registerOutParameter(3, Types.INTEGER);

            callSt.execute();
            int accountId = callSt.getInt(3);

            if (accountId > 0) {
                Admin account = new Admin();
                account.setAccountId(accountId);
                account.setAccountName(username);
                account.setPassword(password);
                return account;
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi truy vấn CSDL: " + e.getMessage() + "\u001B[0m");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return null;
    }
}