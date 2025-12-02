package data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import data.entity.Order;
import data.entity.OrderComplete;

@Dao
public interface OrderDAO {

    @Insert
    long insert(Order order);

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

    public default boolean openOrder(int barId, int tableId, String date) {

        Order existOrder = getLastTableOrder(tableId);

        // Si existe y está abierta → NO permitir crear una nueva
        if (existOrder != null && !existOrder.getClosed()) {
            return false;
        }

        // Crear nueva orden
        Order order = new Order();
        order.setBarId(barId);
        order.setTableId(tableId);
        order.setDate(date);
        order.setClosed(false);
        order.setTotalPrice(0);

        insert(order);

        return true;
    }

    public default boolean closeOrder(int orderId) {

        Order order = getById(orderId);

        if (order == null || order.getClosed()) {
            return false;
        }

        order.setClosed(true);
        order.setTotalPrice();// TODO: Añadir calculo de suma de líneas aquí

        // Actualizar la orden en la base de datos
        insert(order);

        return true;
    }
}