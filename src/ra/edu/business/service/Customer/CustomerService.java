package ra.edu.business.service.Customer;

import ra.edu.business.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomerList();
    void addCustomer(String name, String email, String phone, String address);
    void updateCustomer(int customerId, String name, String email, String phone, String address);
    void deleteCustomer(int customerId);
}