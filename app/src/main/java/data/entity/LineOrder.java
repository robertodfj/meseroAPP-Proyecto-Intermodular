package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LineOrder {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int productId;
    public int units;

    public double lineTotalPrice;

    public LineOrder() {}

    public LineOrder(int productId, int units, double pricePerUnit) {
        this.productId = productId;
        this.units = units;
        this.lineTotalPrice = pricePerUnit * units;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUnits() { return units; }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getLineTotalPrice() { return lineTotalPrice; }

    public void updateLineTotalPrice(double pricePerUnit) {
        this.lineTotalPrice = pricePerUnit * this.units;
    }
}