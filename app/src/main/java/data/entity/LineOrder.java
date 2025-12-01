package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LineOrder {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public int productId;
    public int units;
    public double linePrice; // price * units

    public LineOrder() {}

    public LineOrder(int id, int orderId, int productId, int units, double linePrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.units = units;
        this.linePrice = linePrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public double getLinePrice() { return linePrice; }
    public void setLinePrice(double linePrice) { this.linePrice = linePrice; }
}