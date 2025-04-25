package ra.edu.business.service.Invoice;

import ra.edu.business.dao.Invoice.InvoiceDAO;
import ra.edu.business.dao.Invoice.InvoiceDaoImp;
import ra.edu.business.dao.InvoiceItem.InvoiceItemDAO;
import ra.edu.business.dao.InvoiceItem.InvoiceItemDaoImp;
import ra.edu.business.dao.Customer.CustomerDAO;
import ra.edu.business.dao.Customer.CustomerDaoImp;
import ra.edu.business.dao.Product.ProductDAO;
import ra.edu.business.dao.Product.ProductDaoImp;
import ra.edu.business.model.Invoice;
import ra.edu.business.model.InvoiceItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class InvoiceServiceImp implements InvoiceService {
    private InvoiceDAO invoiceDAO;
    private InvoiceItemDAO invoiceItemDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;

    public InvoiceServiceImp() {
        this.invoiceDAO = new InvoiceDaoImp();
        this.invoiceItemDAO = new InvoiceItemDaoImp();
        this.customerDAO = new CustomerDaoImp();
        this.productDAO = new ProductDaoImp();
    }

    @Override
    public List<Invoice> getInvoices() {
        return invoiceDAO.getInvoices();
    }

    @Override
    public void addInvoice(int customerId, double totalAmount) {
        invoiceDAO.addInvoice(customerId, totalAmount);
    }

    @Override
    public void addInvoiceItem(int invoiceId, int productId, int quantity, double unitPrice) {
        invoiceItemDAO.addInvoiceItem(invoiceId, productId, quantity, unitPrice);
    }

    @Override
    public List<Invoice> searchInvoicesByCustomerName(String customerName) {
        return invoiceDAO.searchInvoicesByCustomerName(customerName);
    }

    @Override
    public List<Invoice> searchInvoicesByDate(LocalDate date) {
        return invoiceDAO.searchInvoicesByDate(date);
    }

    @Override
    public boolean customerExists(int customerId) {
        return customerDAO.existsById(customerId);
    }

    @Override
    public boolean productExists(int productId) {
        return productDAO.existsById(productId);
    }

    @Override
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        return invoiceItemDAO.getInvoiceItemsByInvoiceId(invoiceId);
    }

    @Override
    public int getProductStockById(int productId) {
        return productDAO.getStockById(productId);
    }

    @Override
    public double getProductPriceById(int productId) {
        return productDAO.getPriceById(productId);
    }

    @Override
    public Map<LocalDate, Double> getRevenueByDay() {
        return invoiceDAO.getRevenueByDay();
    }

    @Override
    public Map<String, Double> getRevenueByMonth() {
        return invoiceDAO.getRevenueByMonth();
    }

    @Override
    public Map<Integer, Double> getRevenueByYear() {
        return invoiceDAO.getRevenueByYear();
    }
}