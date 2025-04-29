package ra.edu.business.service.Admin;

import ra.edu.business.dao.Admin.AdminDAO;
import ra.edu.business.model.Admin;

public class AdminServiceImp implements AdminService {
    private final AdminDAO adminDAO;
    private boolean loggedIn = false;

    public AdminServiceImp(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override
    public boolean login(String username, String password) {
        Admin admin = adminDAO.authenticate(username, password);
        if (admin != null) {
            loggedIn = true;
            System.out.println("Đăng nhập thành công!");
            return true;
        }
        System.out.println("Tên đăng nhập hoặc mật khẩu không đúng!");
        return false;
    }

    @Override
    public void logout() {
        loggedIn = false;
        System.out.println("Đăng xuất thành công!");
    }
}