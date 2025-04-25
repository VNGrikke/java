package ra.edu.business.dao.Invoice;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface InvoiceDAO extends AppDAO {
    List<Invoice> getInvoices();
    void addInvoice(int customerId, double totalAmount);
    List<Invoice> searchInvoicesByCustomerName(String customerName);
    List<Invoice> searchInvoicesByDate(LocalDate date);
    Map<LocalDate, Double> getRevenueByDay();
    Map<String, Double> getRevenueByMonth();
    Map<Integer, Double> getRevenueByYear();
}