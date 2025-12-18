package data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class LineOrder {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int orderId;
    public int barId;
    public int productId;
    public int units;
    public int tableNumber;
    public boolean cocinaDone = false;
    public boolean camareroDone;
    public double linePrice; // price * units

    public LineOrder() {}

    @Ignore // Para que room utilice el otro
    public LineOrder(int id, int orderId, int barId, int productId, int units, int tableNumber,
                     boolean cocinaDone, boolean camareroDone, double linePrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.units = units;
        this.linePrice = linePrice;
        this.tableNumber = tableNumber;
        this.camareroDone = camareroDone;
        this.cocinaDone = cocinaDone;
        this.barId = barId;
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

    public boolean getCocinaDone() { return cocinaDone; }
    public void setCocinaDone(boolean cocinaDone) { this.cocinaDone = cocinaDone; }

    public boolean getCamareroDone() { return camareroDone; }
    public void setCamareroDone(boolean camareroDone) { this.camareroDone = camareroDone; }

    public double getLinePrice() { return linePrice; }
    public void setLinePrice(double linePrice) { this.linePrice = linePrice; }

    public int getBarId() { return barId; }
    public void setBarId(int barId) { this.barId = barId; }
}