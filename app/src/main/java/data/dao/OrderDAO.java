package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Order;
@Dao
public interface OrderDAO {
    @Insert
    void insert(Order order);

    @Query("SELECT * FROM `Order` WHERE id = :id")
    Order getById(int id);
}
