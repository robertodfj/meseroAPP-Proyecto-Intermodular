package data.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "mesero_app_db"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
