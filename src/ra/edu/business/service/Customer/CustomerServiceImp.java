package ra.edu.business.service.Customer;

import ra.edu.business.dao.Customer.CustomerDAO;
import ra.edu.business.dao.Customer.CustomerDaoImp;
import ra.edu.business.model.Customer;
import ra.edu.exception.ValidationException;
import ra.edu.validate.CustomerValidator;
import ra.edu.validate.Validator;

import java.util.List;

public class CustomerServiceImp implements CustomerService {
    private CustomerDAO customerDAO;

    public CustomerServiceImp() {
        this.customerDAO = new CustomerDaoImp();
    }

    @Override
    public List<Customer> getCustomerList() {
        return customerDAO.getCustomerList();
    }

    @Override
    public void addCustomer(String name, String email, String phone, String address) {
        try {
            CustomerValidator.validateCustomer(name, email, phone, address);
            customerDAO.addCustomer(name, email, phone, address);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
        }
    }

    @Override
    public void updateCustomer(int customerId, String name, String email, String phone, String address) {
        try {
            Validator.validatePositiveNumber(customerId, "ID khách hàng");
            CustomerValidator.validateCustomer(name, email, phone, address);
            customerDAO.updateCustomer(customerId, name, email, phone, address);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        try {
            Validator.validatePositiveNumber(customerId, "ID khách hàng");
            customerDAO.deleteCustomer(customerId);
        } catch (ValidationException e) {
            System.out.println("\u001B[31m" + "Lỗi validation: " + e.getMessage() + "\u001B[0m");
        }
    }
}