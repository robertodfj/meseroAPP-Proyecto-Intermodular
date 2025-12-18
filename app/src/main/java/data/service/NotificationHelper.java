package data.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.meseroapp.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "orders_channel";

    /**
     * Envía una notificación simple.
     *
     * @param context Contexto de la app
     * @param userId  Id del usuario (opcional para distinguir notificaciones)
     * @param title   Título de la notificación
     * @param message Texto de la notificación
     */
    public static void sendNotification(Context context, int userId, String title, String message) {

        // Crear canal si es Android 8+
        createChannel(context);

        // Construir la notificación
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        // Verificar permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                    android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // El permiso no está concedido, no enviamos la notificación
                return;
            }
        }

        // Enviar notificación
        NotificationManagerCompat.from(context)
                .notify((int) System.currentTimeMillis(), builder.build());
    }

    /**
     * Crea el canal de notificación para Android 8+.
     */
    private static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Pedidos",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de pedidos listos en la cocina");

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}