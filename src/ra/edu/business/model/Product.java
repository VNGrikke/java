package ra.edu.business.model;

public class Product {
    private int productid;
    private String name;
    private double price;
    private int stock;
    private String brand;
    boolean status;

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Product() {}

    public Product(int productid, String name, double price, int stock, String brand, boolean status) {
        this.productid = productid;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.status = status;
    }
}
