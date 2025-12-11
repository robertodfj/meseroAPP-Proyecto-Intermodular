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
import android.widget.Button;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import data.database.AppDatabase;
import data.entity.Table;

public class CamareroFragment extends Fragment {
    public CamareroFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camarero, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false); // 2 columnas, vertical y no invertido
        RecyclerView recycler = view.findViewById(R.id.rvMesas);
        recycler.setLayoutManager(layoutManager);

        TableAdapter adapter = new TableAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        // Observamos las mesas del bar y actualizamos el RecyclerView
        db.tableDao().getByBarId(barId).observe(getViewLifecycleOwner(), adapter::setTables);

        adapter.setOnEditClickListener(new TableAdapter.OnEditClickListener() {
            @Override
            public void onEdit(Table table) {
                
            }
        });
    }
}