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

    @Query("UPDATE `Product` SET ProductName = :newProductName WHERE id = :id")
    void updateProductName(int id, String newProductName);

    @Query("UPDATE `Product` SET price = :newPrice WHERE id = :id")
    void updateProductPrice(int id, double newPrice);

    @Query("UPDATE `Product` SET stock = :stock WHERE id = :id")
    void updateProductStock(int id, int stock);

    @Delete
    void delete(Product product);

    public default boolean createProduct(Product product) {
        Product existProduct = getByName(product.getProductName());
        if (existProduct == null) {
            insert(product);
            System.out.println("Producto '" + product.getProductName() + "' añadido correctamente");
            return true;
        }
        System.out.println("Producto '" + product.getProductName() + "' ya existe");
        return false;
    }

    public default boolean deleteProduct(int id) {
        Product existProduct = getById(id);
        if (existProduct != null) {
            delete(existProduct);
            System.out.println("Producto '" + existProduct.getProductName() + "' eliminado correctamente");
            return true;
        }
        System.out.println("El producto no existe");
        return false;
    }

    public default boolean editProduct(int id, String newName, Double newPrice, Integer newStock) {
        Product product = getById(id);
        if (product != null) {
            if (newName != null) updateProductName(id, newName);
            if (newPrice != null) updateProductPrice(id, newPrice);
            if (newStock != null) updateProductStock(id, newStock);
            System.out.println("Producto '" + getById(id).getProductName() + "' editado correctamente");
            return true;
        }
        System.out.println("El producto no existe");
        return false;
    }

    public default boolean editStock(int id, int units) {
        Product product = getById(id);

        if (product == null) {
            System.out.println("El producto no existe");
            return false;
        }

        int currentStock = product.getStock();
        int newStock = currentStock - units;

        if (newStock < 0) {
            System.out.println("No hay stock suficiente para realizarlo. Stock actual: " +currentStock);
            return false;
        }

        updateProductStock(id, newStock);
        System.out.println("Stock actualizado. Nuevo stock: " + newStock);
        return true;
    }
}