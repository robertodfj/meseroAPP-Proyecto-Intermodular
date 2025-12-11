package data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int barId;
    public int tableNumber;
    public int space;
    public String status = null;

    public Table() {}

    @Ignore // Para que room utilice el otro room
    public Table(int id, int barId, int tableNumber, int space, String status) {
        this.id = id;
        this.barId = barId;
        this.tableNumber = tableNumber;
        this.space = space;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}
