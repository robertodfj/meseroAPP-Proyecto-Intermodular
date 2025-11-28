import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Products {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String productName;

    public int price;

    public int stock;

    public Products() {}

    public Products(int stock, String productName, int id, int price) {
        this.stock = stock;
        this.productName = productName;
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
