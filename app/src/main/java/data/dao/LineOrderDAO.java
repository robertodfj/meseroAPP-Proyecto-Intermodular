package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.LineOrder;

@Dao
public interface LineOrderDAO {
    @Insert
    void insert(LineOrder lineOrder);

    @Query("SELECT * FROM `LineOrder` WHERE id = :id")
    LineOrder getById(int id);
}
