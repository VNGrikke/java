package ra.edu.business.service.Admin;

public interface AdminService {
    boolean login(String username, String password);
    void logout();

}
