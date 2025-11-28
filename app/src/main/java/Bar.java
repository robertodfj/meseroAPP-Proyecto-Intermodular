import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bar {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String barName;

    public String email;

}
