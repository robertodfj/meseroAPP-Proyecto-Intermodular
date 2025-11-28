import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Products {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String productName;

    public int stock;

    public Products() {}

    public Products(int stock, String productName, int id) {
        this.stock = stock;
        this.productName = productName;
        this.id = id;
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
}
