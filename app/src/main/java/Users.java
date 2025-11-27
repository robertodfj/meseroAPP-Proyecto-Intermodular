import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class Users {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public Date startDate;
    public Date endDate = null;
    public String email;
    public String password;

    public Users() {}

    public Users(int id, String name, Date startDate, Date endDate, String email, String password) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
