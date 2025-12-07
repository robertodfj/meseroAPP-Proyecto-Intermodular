package data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "email", unique = true)})
public class Bar {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String barName;
    public String email;
    public int token = 0;

    public Bar() {}

    public Bar(int id, String barName, String email, int token) {
        this.id = id;
        this.barName = barName;
        this.email = email;
        this.token = token;
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
    public int getToken() {return token;}
    public void setToken(int token) {this.token = token;}
}
