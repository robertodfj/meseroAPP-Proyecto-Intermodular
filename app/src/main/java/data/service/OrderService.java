package data.service;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import data.dao.BarDAO;
import data.dao.LineOrderDAO;
import data.dao.OrderDAO;
import data.dao.ProductDAO;
import data.entity.LineOrder;
import data.entity.Order;
import data.entity.Product;

public class OrderService {
    private final OrderDAO orderDao;
    private final ProductDAO productDao;
    private final LineOrderDAO lineDao;

    public OrderService(OrderDAO orderDao, ProductDAO productDao, LineOrderDAO lineDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.lineDao = lineDao;
    }

    public boolean addProductToOrder(int orderId, int productId, int units) {
        Order order = orderDao.getById(orderId);
        if (order == null || order.getClosed()) return false;

        Product product = productDao.getById(productId);
        if (product == null || product.getStock() < units) return false;

        productDao.updateProductStock(productId, product.getStock() - units);

        LineOrder line = new LineOrder();
        line.setOrderId(orderId);
        line.setProductId(productId);
        line.setUnits(units);
        line.setLinePrice(product.getPrice() * units);

        order.setTotalPrice(order.getTotalPrice() + line.getLinePrice());
        orderDao.update(order);

        lineDao.insert(line);
        return true;
    }

    public boolean openOrder(int barId, int tableId, String date) {
        Order existOrder = orderDao.getLastTableOrder(tableId);
        if (existOrder != null && !existOrder.getClosed()) return false;

        Order order = new Order();
        order.setBarId(barId);
        order.setTableId(tableId);
        order.setDate(date);
        order.setClosed(false);
        order.setTotalPrice(0);

        orderDao.insert(order);
        return true;
    }

    public boolean closeOrder(int orderId) {
        Order order = orderDao.getById(orderId);
        if (order == null || order.getClosed()) return false;

        order.setClosed(true);
        orderDao.update(order);
        return true;
    }
}