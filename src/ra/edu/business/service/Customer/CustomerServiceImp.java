package ra.edu.business.service.Customer;

import ra.edu.business.dao.Customer.CustomerDAO;
import ra.edu.business.model.Customer;

import java.util.List;

public class CustomerServiceImp implements CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerServiceImp(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public List<Customer> getCustomerList() {
        return customerDAO.getCustomerList();
    }

    @Override
    public void addCustomer(String name, String email, String phone, String address) {
        customerDAO.addCustomer(name, email, phone, address);
    }

    @Override
    public void updateCustomer(int customerId, String name, String email, String phone, String address) {
        customerDAO.updateCustomer(customerId, name, email, phone, address);
    }

    @Override
    public void deleteCustomer(int customerId) {
        customerDAO.deleteCustomer(customerId);
    }
}