package data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Table;

@Dao
public interface TableDAO {
    @Insert
    void insert(Table table);

    @Query("SELECT * FROM `Table` WHERE id = :id")
    Table getById(int id);

    @Delete
    void delete(Table table);
}
