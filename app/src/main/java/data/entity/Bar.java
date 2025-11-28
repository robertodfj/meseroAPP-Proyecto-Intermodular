package data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bar {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String barName;

    public String email;

    public Bar() {}

    public Bar(int id, String barName, String email) {
        this.id = id;
        this.barName = barName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
