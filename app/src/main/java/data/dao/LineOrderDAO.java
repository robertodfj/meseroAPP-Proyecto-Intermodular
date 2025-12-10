package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import data.entity.LineOrder;
import data.entity.LineWithProduct;

@Dao
public interface LineOrderDAO {

    @Insert
    void insert(LineOrder lineOrder);

    @Query("SELECT * FROM LineOrder WHERE orderId = :orderId")
    List<LineOrder> getLinesByOrder(int orderId);
    @Transaction
    @Query("SELECT * FROM LineOrder WHERE orderId = :orderId")
    List<LineWithProduct> getLinesWithProducts(int orderId);
}