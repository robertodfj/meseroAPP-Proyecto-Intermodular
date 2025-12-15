package data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class LineOrder {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int orderId;
    public int productId;
    public int units;
    public int tableNumber;
    public boolean done = false;
    public double linePrice; // price * units

    public LineOrder() {}

    @Ignore // Para que room utilice el otro room
    public LineOrder(int id, int orderId, int productId, int units, int tableNumber, boolean done, double linePrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.units = units;
        this.linePrice = linePrice;
        this.tableNumber = tableNumber;
        this.done = done;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public boolean getDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public double getLinePrice() { return linePrice; }
    public void setLinePrice(double linePrice) { this.linePrice = linePrice; }
}