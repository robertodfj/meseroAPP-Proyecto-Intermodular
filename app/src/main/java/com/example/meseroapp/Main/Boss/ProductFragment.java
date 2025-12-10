package com.example.meseroapp.Main.Boss;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import data.database.AppDatabase;

public class ProductFragment extends Fragment {

    public ProductFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.rvProduct);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ProductAdapter adapter = new ProductAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();

        AppDatabase db = AppDatabase.getInstance(getContext());

        db.productDao().getProductsByBarId(barId).observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);
        });
    }
}