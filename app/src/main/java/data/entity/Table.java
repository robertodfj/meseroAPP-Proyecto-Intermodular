package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int barId;
    public int tableNumber;
    public int space;

    public Table() {}

    public Table(int id, int barId, int tableNumber, int space) {
        this.id = id;
        this.barId = barId;
        this.tableNumber = tableNumber;
        this.space = space;
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
}
