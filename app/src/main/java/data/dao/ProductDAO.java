package data.dao;

import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Product;

public interface ProductDAO {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM Product WHERE id = :id")
    Product getById(int id);
}
