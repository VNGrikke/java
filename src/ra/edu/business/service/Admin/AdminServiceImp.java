package ra.edu.business.service.Admin;

import ra.edu.business.dao.Admin.AdminDAO;
import ra.edu.business.dao.Admin.AdminDaoImp;
import ra.edu.business.model.Admin;

public class AdminServiceImp implements AdminService {
    private AdminDAO adminDAO;
    private boolean isLoggedIn;

    public AdminServiceImp() {
        this.adminDAO = new AdminDaoImp();
        this. isLoggedIn= false;
    }

    @Override
    public boolean login(String username, String password) {
        Admin authenticated = adminDAO.authenticate(username, password);
        if (authenticated != null) {
            isLoggedIn = true;
            System.out.println("Đăng nhập thành công!");
            return true;
        } else {
            System.out.println("\u001B[31m" + "Lỗi: Username hoặc password không đúng!" + "\u001B[0m");
            return false;
        }
    }

    @Override
    public void logout() {
        isLoggedIn = false;
        System.out.println("Đăng xuất thành công!");
    }

}