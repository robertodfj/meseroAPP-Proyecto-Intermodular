package com.example.meseroapp.Main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import java.util.List;

import data.database.AppDatabase;
import data.entity.LineOrder;
import data.entity.User;
import data.service.NotificationHelper;

public class CocinaFragment extends Fragment {

    public CocinaFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cocina, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inflamos la vista con el adaptador personalizado
        RecyclerView recicler = view.findViewById(R.id.rvOrderCard);
        recicler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        LineOrderAdapter lineOrderAdapter = new LineOrderAdapter();
        recicler.setAdapter(lineOrderAdapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        db.lineOrderDao()
                .getPendingLinesByBar(barId)
                .observe(getViewLifecycleOwner(), lineOrderAdapter::setOrder);

        lineOrderAdapter.setOnEditClickListener(lineOrder -> markLineAsDone(lineOrder) );
    }

    private void markLineAsDone(LineOrder lineOrder) {
        AppDatabase db = AppDatabase.getInstance(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Marcar como listo");
        builder.setMessage("¿Estás seguro de que deseas marcar este pedido como listo?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            lineOrder.setCocinaDone(true);
            lineOrder.setCamareroDone(false);
            new Thread(() -> db.lineOrderDao().update(lineOrder)).start();

            //Enviar notificación al camarero activo del bar
            int barId = SessionManager.getInstance(getContext()).getBarId();
            List<User> users = db.userDao().getActiveCamarero(barId);
            if (users != null && !users.isEmpty()) {
                for (User camarero : users) {
                    NotificationHelper.sendNotification(
                            requireContext(),
                            camarero.getId(),
                            "Pedido listo",
                            "Un pedido ha sido marcado como listo en la cocina."
                    );
                }
            }

        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}