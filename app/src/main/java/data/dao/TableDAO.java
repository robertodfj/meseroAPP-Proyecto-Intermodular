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

    @Query("SELECT * FROM `Table` WHERE tableNumber = :tableNumber")
    Table getByTableNumber(int tableNumber);

    @Delete
    void delete(Table table);

    @Query("UPDATE `Table` SET tableNumber = :newTableNumber WHERE tableNumber = :tableNumber")
    void updateTableNumber(int tableNumber, int newTableNumber);
}