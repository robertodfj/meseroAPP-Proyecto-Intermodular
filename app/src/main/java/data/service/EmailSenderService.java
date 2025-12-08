package data.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
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

    // Correo de mesero APP
    private static final String EMAIL_USERNAME = "meseroapp@gmail.com";
    private static final String EMAIL_PASSWORD = "********"; // remplazar por contraseña real


    public EmailSenderService(OrderDAO orderDao, ProductDAO productDao, LineOrderDAO lineDao, BarDAO barDAO) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.lineDao = lineDao;
        this.barDAO = barDAO;
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
            ProductDAO productDao = null;
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

    public Boolean sendBarVerifyEmail(String recipient, int tokenVerify) {

        // Generar HTML del correo
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif; color:#333;'>");

        html.append("<h2 style='color:#2E86C1;'>Verificación de Bar - MeseroApp</h2>");

        html.append("<p>Hola,</p>");
        html.append("<p>Estás registrando un bar en <strong>MeseroApp</strong>. Para completar el proceso, introduce el siguiente código de verificación en la aplicación:</p>");

        html.append("<div style='margin: 20px 0; padding: 15px; background:#f0f4ff; border-left: 4px solid #2E86C1; display:inline-block;'>")
                .append("<h1 style='margin:0; font-size:32px; letter-spacing:4px; color:#2E86C1;'>")
                .append(tokenVerify)
                .append("</h1>")
                .append("</div>");

        html.append("<p>Este es tu <strong>token de verificación</strong> generado por MeseroApp.</p>");
        html.append("<p>Si tú no solicitaste este registro, puedes ignorar este correo.</p>");

        html.append("<br><p>– El equipo de MeseroApp</p>");
        html.append("</body></html>");

        // Configuración SMTP
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
            message.setSubject("Código de verificación de MeseroApp");
            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Correo de verificación enviado a " + recipient);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean sendUserVerifyEmail(String recipient, int tokenVerify, String userName) {

        // Generar HTML del correo
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif; color:#333;'>");

        html.append("<h2 style='color:#2E86C1;'>Nuevo empleado quiere registrarse - MeseroApp</h2>");

        html.append("<p>Hola,</p>");
        html.append("<p>El empleado <strong>")
                .append(userName)
                .append("</strong> quiere unirse a tu bar en <strong>MeseroApp</strong>.</p>");

        html.append("<p>Para aprobar su registro, introduce el siguiente código de verificación en la aplicación:</p>");

        html.append("<div style='margin: 20px 0; padding: 15px; background:#f0f4ff; border-left: 4px solid #2E86C1; display:inline-block;'>")
                .append("<h1 style='margin:0; font-size:32px; letter-spacing:4px; color:#2E86C1;'>")
                .append(tokenVerify)
                .append("</h1>")
                .append("</div>");

        html.append("<p>Este es el <strong>token de verificación</strong> generado por MeseroApp.</p>");
        html.append("<p>Si tú no solicitaste este registro, puedes ignorar este correo.</p>");

        html.append("<br><p>– El equipo de MeseroApp</p>");
        html.append("</body></html>");

        // Configuración SMTP
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
            message.setSubject("Verificación de registro de nuevo empleado - MeseroApp");
            message.setContent(html.toString(), "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Correo de verificación enviado a " + recipient);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
