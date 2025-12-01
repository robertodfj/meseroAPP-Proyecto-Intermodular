package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Order {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int barId;
    public int tableId;
    public double totalPrice;
    public String date;

    public Order() {}

    public Order(int id, int barId, int tableId, double totalPrice, String date) {
        this.id = id;
        this.barId = barId;
        this.tableId = tableId;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBarId() { return barId; }
    public void setBarId(int barId) { this.barId = barId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}