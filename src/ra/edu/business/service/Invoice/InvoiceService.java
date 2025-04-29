package ra.edu.business.service.Invoice;

import ra.edu.business.model.Invoice;
import ra.edu.business.model.InvoiceItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface InvoiceService {
    List<Invoice> getInvoices();
    void addInvoice(int customerId, double totalAmount);
    void addInvoiceItem(int invoiceId, int productId, int quantity, double unitPrice);
    List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId);
    List<Invoice> searchInvoicesByCustomerName(String customerName);
    List<Invoice> searchInvoicesByDate(LocalDate date);
    Map<LocalDate, Double> getRevenueByDay();
    Map<String, Double> getRevenueByMonth();
    Map<Integer, Double> getRevenueByYear();
    boolean customerExists(int customerId);
    boolean productExists(int productId);
    double getProductPriceById(int productId);
    int getProductStockById(int productId);
}