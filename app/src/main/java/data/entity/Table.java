package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Table {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int tableNumber;

    public int space;

    public Table() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
