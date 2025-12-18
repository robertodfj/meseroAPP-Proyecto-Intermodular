package data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        indices = {@Index(value = "email", unique = true)}
)
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int barId;
    public String rol;
    public boolean isActive = false;
    public String email;
    public String password;

    public User() {}

    @Ignore // Para que room utilice el otro room
    public User(int id, String name, String email, String password, int barId, String rol, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.barId = barId;
        this.rol = rol;
        this.isActive = isActive;
    }

    public int getId() { return id;}

    public void setId(int id) {this.id = id;}

    public int getBarId() {return barId;}

    public void setBarId(int barId) {this.barId = barId;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getRol() {return rol;}

    public void setRol(String rol) {this.rol = rol;}

    public boolean isActive() {return isActive;}
    public void setActive(boolean active) {isActive = active;}
}