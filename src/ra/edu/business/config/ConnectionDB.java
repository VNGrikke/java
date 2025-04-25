package ra.edu.business.config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/manager_phone_store";
    private static final String USER = "root1";
    private static final String PASSWORD = "a@1234";

    public static void testConnection() {
        try (Connection conn = openConnection()) {
            if (conn != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối thất bại!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }

    public static Connection openConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "Lỗi kết nối CSDL do: " + e.getMessage() + "\u001B[0m");
            throw e; // Ném lại ngoại lệ để tầng gọi xử lý
        }
    }

    public static void closeConnection(Connection conn, CallableStatement callSt) {
        if (callSt != null) {
            try {
                callSt.close();
            } catch (SQLException e) {
                System.out.println("\u001B[31m" + "Lỗi đóng CallableStatement: " + e.getMessage() + "\u001B[0m");
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("\u001B[31m" + "Lỗi đóng kết nối: " + e.getMessage() + "\u001B[0m");
            }
        }
    }
}