package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int barId;
    public String productName;
    public double price;
    public int stock;

    public Product() {}

    public Product(int stock, String productName, int id, double price, int barId) {
        this.stock = stock;
        this.productName = productName;
        this.id = id;
        this.price = price;
        this.barId = barId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBarId() { return barId; }

    public void setBarId(int barId) { this.barId = barId; }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
