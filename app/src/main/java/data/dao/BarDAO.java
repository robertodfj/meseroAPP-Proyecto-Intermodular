package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Bar;
@Dao
public interface BarDAO {
    @Insert
    void insert(Bar bar);

    @Query("SELECT * FROM Bar WHERE barName = :name")
    Bar getByName(String name);
}
