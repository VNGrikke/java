package ra.edu.business.model;

public class InvoiceItem {
    int invoice_item_id;
    int product_id;
    int quantity;
    double unit_price;

    public InvoiceItem() {}

    public InvoiceItem(int product_id, int quantity, double unit_price) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public int getInvoice_item_id() {
        return invoice_item_id;
    }

    public void setInvoice_item_id(int invoice_item_id) {
        this.invoice_item_id = invoice_item_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }
}