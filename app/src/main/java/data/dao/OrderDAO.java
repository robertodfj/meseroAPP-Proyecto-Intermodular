package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import data.entity.Order;
import data.entity.OrderComplete;

@Dao
public interface OrderDAO {

    @Insert
    long insert(Order order);

    @Query("SELECT * FROM `Order` WHERE id = :id")
    Order getById(int id);

    // Relación completa: pedido + líneas + productos
    @Query("SELECT * FROM `Order` WHERE id = :id")
    OrderComplete getCompleteOrder(int id);

    // Pedidos por mesa
    @Query("SELECT * FROM `Order` WHERE tableId = :tableId")
    List<Order> getOrdersByTable(int tableId);

    // Pedidos de un bar
    @Query("SELECT * FROM `Order` WHERE barId = :barId")
    List<Order> getOrdersByBar(int barId);

    // Pedidos por día
    @Query("SELECT * FROM `Order` WHERE date = :date")
    List<Order> getOrdersByDate(String date);
}