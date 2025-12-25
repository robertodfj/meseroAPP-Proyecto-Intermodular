package data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import data.dao.*;
import data.entity.*;


import android.content.Context;

import androidx.room.Room;
@Database(
        entities = {
                Product.class,
                LineOrder.class,
                Order.class,
                User.class,
                Bar.class,
                Table.class
        },
        version = 2,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ProductDAO productDao();
    public abstract LineOrderDAO lineOrderDao();
    public abstract OrderDAO orderDao();
    public abstract UserDAO userDao();
    public abstract BarDAO barDao();
    public abstract TableDAO tableDao();

    // Singleton para obtener la instancia de la base de datos
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "mesero_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}