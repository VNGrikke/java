package ra.edu.business.model;

import java.time.LocalDate;
import java.util.Date;

public class Invoice {
    int invoiceId;
    int customerId;
    LocalDate created_at;
    double total_amount;

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public Invoice(){}

    public Invoice(int invoiceId, int customerId, LocalDate created_at, double total_amount) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.created_at = created_at;
        this.total_amount = total_amount;
    }
}
