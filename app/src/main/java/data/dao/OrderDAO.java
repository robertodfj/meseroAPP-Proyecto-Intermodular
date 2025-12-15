package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import data.entity.LineOrder;
import data.entity.Order;
import data.entity.OrderComplete;
import data.entity.Product;

@Dao
public interface OrderDAO {

    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("UPDATE `Order` SET totalPrice = :id")
    void updateTotalPrice(int id);

    @Query("SELECT * FROM `Order` WHERE id = :id")
    Order getById(int id);

    @Transaction
    @Query("SELECT * FROM `Order` WHERE id = :id")
    OrderComplete getCompleteOrder(int id);

    @Query("SELECT * FROM `Order` WHERE tableId = :tableId")
    List<Order> getOrdersByTable(int tableId);

    @Query("SELECT * FROM `Order` WHERE tableId = :tableId ORDER BY id DESC LIMIT 1")
    Order getLastTableOrder(int tableId);

    @Query("SELECT * FROM `Order` WHERE barId = :barId")
    List<Order> getOrdersByBar(int barId);

    @Query("SELECT * FROM `Order` WHERE date = :date")
    List<Order> getOrdersByDate(String date);

    @Query("SELECT * FROM `Order` WHERE tableId = :tableId AND closed = 0 ORDER BY id DESC LIMIT 1")
    Order getLastOrderByTable(int tableId);
}