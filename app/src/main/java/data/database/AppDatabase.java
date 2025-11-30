package data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import data.dao.*;
import data.entity.*;

@Database(
        entities = {
                Bar.class,
                Table.class,
                User.class,
                Order.class,
                LineOrder.class,
                Product.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BarDAO barDao();
    public abstract TableDAO tableDao();
    public abstract UserDAO userDao();
    public abstract ProductDAO productDao();
    public abstract OrderDAO orderDao();
    public abstract LineOrderDAO lineOrderDao();
}
