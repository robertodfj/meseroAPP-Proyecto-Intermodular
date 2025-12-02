package data.service;

import data.dao.ProductDAO;
import data.entity.Product;

public class ProductService {

    private final ProductDAO productDao;

    public ProductService(ProductDAO productDao) {
        this.productDao = productDao; // inyección de DAO
    }

    public boolean createProduct(Product product) {
        Product existProduct = productDao.getByName(product.getProductName());
        if (existProduct == null) {
            productDao.insert(product);
            System.out.println("Producto '" + product.getProductName() + "' añadido correctamente");
            return true;
        }
        System.out.println("Producto '" + product.getProductName() + "' ya existe");
        return false;
    }

    public boolean deleteProduct(int id) {
        Product existProduct = productDao.getById(id);
        if (existProduct != null) {
            productDao.delete(existProduct);
            System.out.println("Producto '" + existProduct.getProductName() + "' eliminado correctamente");
            return true;
        }
        System.out.println("El producto no existe");
        return false;
    }

    public boolean editProduct(int id, String newName, Double newPrice, Integer newStock) {
        Product product = productDao.getById(id);
        if (product != null) {
            if (newName != null) productDao.updateProductName(id, newName);
            if (newPrice != null) productDao.updateProductPrice(id, newPrice);
            if (newStock != null) productDao.updateProductStock(id, newStock);
            System.out.println("Producto '" + productDao.getById(id).getProductName() + "' editado correctamente");
            return true;
        }
        System.out.println("El producto no existe");
        return false;
    }
}