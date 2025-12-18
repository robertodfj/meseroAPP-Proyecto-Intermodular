package com.example.meseroapp.Main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import data.database.AppDatabase;

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
    }
}