package com.example.meseroapp.Main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import data.database.AppDatabase;
import data.entity.LineOrder;
import data.entity.User;
import data.service.NotificationHelper;


public class PendingOrderCamarero extends Fragment {

    public PendingOrderCamarero(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending_order_camarero, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inflamos la vista con el adaptador personalizado
        RecyclerView recicler = view.findViewById(R.id.rvPendingOrdersCamarero);
        recicler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        LineOrderAdapter lineOrderAdapter = new LineOrderAdapter();
        recicler.setAdapter(lineOrderAdapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        db.lineOrderDao()
                .getPendingLinesByCamarero(barId)
                .observe(getViewLifecycleOwner(), lineOrderAdapter::setOrder);

        lineOrderAdapter.setOnEditClickListener(lineOrder -> markLineAsDone(lineOrder) );

        // Si no hay pedidos pendientes, mostramos un mensaje
        TextView tvNoPendingOrders = view.findViewById(R.id.tvNoPendingOrdersCamarero);
        db.lineOrderDao()
                .getPendingLinesByCamarero(barId)
                .observe(getViewLifecycleOwner(), lineOrders -> {
                    if (lineOrders == null || lineOrders.isEmpty()) {
                        tvNoPendingOrders.setVisibility(View.VISIBLE);
                        recicler.setVisibility(View.GONE);
                    } else {
                        tvNoPendingOrders.setVisibility(View.GONE);
                        recicler.setVisibility(View.VISIBLE);
                    }
                });

        FloatingActionButton fabNotifyKitchen = view.findViewById(R.id.fabNotifyKitchenCamarero);
        fabNotifyKitchen.setOnClickListener(v -> );
    }

    private void markLineAsDone(LineOrder lineOrder) {
        AppDatabase db = AppDatabase.getInstance(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Marcar como listo");
        builder.setMessage("¿Estás seguro de que deseas marcar este pedido como listo?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            lineOrder.setCocinaDone(true);
            lineOrder.setCamareroDone(true);
            new Thread(() -> db.lineOrderDao().update(lineOrder)).start();

            Toast.makeText(getContext(), "Pedido marcado como listo", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void notifyKitchen() {
        SessionManager sessionManager = SessionManager.getInstance(getContext());
        int userId = sessionManager.();


        Toast.makeText(getContext(), "Estas activo, ya te llegan las notificaciones", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Estas inactivo, ya no te llegan las notificaciones", Toast.LENGTH_SHORT).show();
    }
}