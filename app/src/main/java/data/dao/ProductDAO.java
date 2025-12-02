package data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import data.entity.Product;

@Dao
public interface ProductDAO {

    @Insert
    void insert(Product product);

    @Query("SELECT * FROM Product WHERE id = :id")
    Product getById(int id);

    @Query("SELECT * FROM Product WHERE productName = :productName")
    Product getByName(String productName);

    @Query("UPDATE Product SET productName = :newProductName WHERE id = :id")
    void updateProductName(int id, String newProductName);

    @Query("UPDATE Product SET price = :newPrice WHERE id = :id")
    void updateProductPrice(int id, double newPrice);

    @Query("UPDATE Product SET stock = :stock WHERE id = :id")
    void updateProductStock(int id, int stock);

    @Delete
    void delete(Product product);
}