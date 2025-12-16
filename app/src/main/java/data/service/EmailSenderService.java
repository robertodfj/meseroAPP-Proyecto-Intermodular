package data.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import data.dao.BarDAO;
import data.dao.LineOrderDAO;
import data.dao.OrderDAO;
import data.dao.ProductDAO;
import data.entity.LineOrder;
import data.entity.Order;
import data.entity.Product;

public class EmailSenderService {

    private final OrderDAO orderDao;
    private final ProductDAO productDao;
    private final LineOrderDAO lineDao;
    private final BarDAO barDAO;

    private static final String EMAIL_USERNAME = "meseroapp@gmail.com";
    private static final String EMAIL_PASSWORD = "********";

    public EmailSenderService(
            OrderDAO orderDao,
            ProductDAO productDao,
            LineOrderDAO lineDao,
            BarDAO barDAO
    ) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.lineDao = lineDao;
        this.barDAO = barDAO;
    }

    public boolean sendOrderTicket(int orderId, String recipient) {

        Order order = orderDao.getById(orderId);
        if (order == null) return false;

        String barName = barDAO.getById(order.getBarId()).getBarName();
        List<LineOrder> lines = lineDao.getLinesByOrder(orderId);

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif;'>");
        html.append("<h2>Factura - ").append(barName).append("</h2>");
        html.append("<table border='1' cellpadding='8' cellspacing='0' width='100%'>");
        html.append("<tr><th>Producto</th><th>Cantidad</th><th>Precio</th><th>Subtotal</th></tr>");

        for (LineOrder line : lines) {
            Product product = productDao.getById(line.getProductId());

            html.append("<tr>")
                    .append("<td>").append(product.getProductName()).append("</td>")
                    .append("<td>").append(line.getUnits()).append("</td>")
                    .append("<td>").append(product.getPrice()).append("€</td>")
                    .append("<td>").append(line.getLinePrice()).append("€</td>")
                    .append("</tr>");
        }

        html.append("<tr>")
                .append("<td colspan='3'><strong>Total</strong></td>")
                .append("<td><strong>").append(order.getTotalPrice()).append("€</strong></td>")
                .append("</tr>");

        html.append("</table>");
        html.append("<p>Gracias por tu visita</p>");
        html.append("</body></html>");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
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
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "MeseroApp"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Factura - " + barName);
            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendUserVerifyEmail(String barEmail, int token, String fullName) {

        String subject = "Verificación de nuevo usuario en MeseroApp";
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif;'>");
        html.append("<h2>Solicitud de registro de usuario</h2>");
        html.append("<p>Hola,</p>");
        html.append("<p>El usuario <strong>").append(fullName).append("</strong> quiere registrarse en tu bar.</p>");
        html.append("<p>Su token de verificación es: <strong>").append(token).append("</strong></p>");
        html.append("<p>Introduce este token en la app para completar el registro.</p>");
        html.append("<br><p>MeseroApp</p>");
        html.append("</body></html>");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
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
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "MeseroApp"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(barEmail));
            message.setSubject(subject);
            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendBarVerifyEmail(String email, int token) {

        String subject = "Verificación de nuevo usuario en MeseroApp";
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif;'>");
        html.append("<h2>Solicitud de registro de usuario</h2>");
        html.append("<p>Hola,</p>");
        html.append("<p>¿Estas intentando crear un bar en meseroAPP?</p>");
        html.append("<p>Su token de verificación es: <strong>").append(token).append("</strong></p>");
        html.append("<p>Introduce este token en la app para completar el registro del bar.</p>");
        html.append("<br><p>MeseroApp</p>");
        html.append("</body></html>");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
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
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "MeseroApp"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}