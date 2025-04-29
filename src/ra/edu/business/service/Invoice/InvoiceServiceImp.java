package ra.edu.business.service.Invoice;

import ra.edu.business.dao.Customer.CustomerDAO;
import ra.edu.business.dao.Invoice.InvoiceDAO;
import ra.edu.business.dao.InvoiceItem.InvoiceItemDAO;
import ra.edu.business.dao.Product.ProductDAO;
import ra.edu.business.model.Invoice;
import ra.edu.business.model.InvoiceItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class InvoiceServiceImp implements InvoiceService {
    private final InvoiceDAO invoiceDAO;
    private final InvoiceItemDAO invoiceItemDAO;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public InvoiceServiceImp(InvoiceDAO invoiceDAO, InvoiceItemDAO invoiceItemDAO, CustomerDAO customerDAO, ProductDAO productDAO) {
        this.invoiceDAO = invoiceDAO;
        this.invoiceItemDAO = invoiceItemDAO;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
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
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        return invoiceItemDAO.getInvoiceItemsByInvoiceId(invoiceId);
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

    @Override
    public boolean customerExists(int customerId) {
        return customerDAO.existsById(customerId);
    }

    @Override
    public boolean productExists(int productId) {
        return productDAO.existsById(productId);
    }

    @Override
    public double getProductPriceById(int productId) {
        return productDAO.getPriceById(productId);
    }

    @Override
    public int getProductStockById(int productId) {
        return productDAO.getStockById(productId);
    }
}