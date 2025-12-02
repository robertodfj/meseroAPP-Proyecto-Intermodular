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
    private final BarDAO barDAO;

    // Correo de mesero APP
    private static final String EMAIL_USERNAME = "meseroapp@gmail.com";
    private static final String EMAIL_PASSWORD = "********"; // remplazar por contraseña real

    public OrderService(OrderDAO orderDao, ProductDAO productDao, LineOrderDAO lineDao, BarDAO barDAO) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.lineDao = lineDao;
        this.barDAO = barDAO;
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

    public boolean sendOrderTicket(int orderId, String recipient) {
        Order order = orderDao.getById(orderId);
        if (order == null) return false;

        int barId = order.getBarId();
        String barName = barDAO.getById(barId).getBarName();

        // Obtener líneas del pedido
        List<LineOrder> lines = lineDao.getLinesByOrder(orderId);

        // Generar HTML del ticket
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif; color:#333;'>");
        html.append("<h2 style='color:#2E86C1;'>Factura de tu pedido en ").append(barName).append("</h2>");
        html.append("<table style='width:100%; border-collapse: collapse;'>");
        html.append("<tr style='background-color:#f2f2f2;'>")
                .append("<th style='border:1px solid #ddd;padding:8px;'>Producto</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;'>Cantidad</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;'>Precio Unitario</th>")
                .append("<th style='border:1px solid #ddd;padding:8px;'>Subtotal</th>")
                .append("</tr>");

        for (LineOrder line : lines) {
            Product product = productDao.getById(line.getProductId());
            html.append("<tr>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(product.getProductName()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(line.getUnits()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>€").append(product.getPrice()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>€").append(line.getLinePrice()).append("</td>")
                    .append("</tr>");
        }

        html.append("<tr style='font-weight:bold;'>")
                .append("<td colspan='3' style='border:1px solid #ddd;padding:8px;'>Total</td>")
                .append("<td style='border:1px solid #ddd;padding:8px;'>€").append(order.getTotalPrice()).append("</td>")
                .append("</tr>");
        html.append("</table>");
        html.append("<p>Gracias por tu compra!</p>");
        html.append("<p>Correo enviado desde MESERO APP</p>");
        html.append("</body></html>");

        // Configuración SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Factura de tu pedido en " + barName);
            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Correo HTML enviado correctamente a " + recipient);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}