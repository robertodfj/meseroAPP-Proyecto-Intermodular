package com.example.meseroapp.Main.Boss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.Main.CamareroFragment;
import com.example.meseroapp.Main.CocinaFragment;
import com.example.meseroapp.R;

public class BossFragment extends Fragment {

    public BossFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gerente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnVerAlmacen = view.findViewById(R.id.btnVerAlmacen);
        Button btnVerEmpleados = view.findViewById(R.id.btnVerEmpleados);
        Button btnVerCocina = view.findViewById(R.id.btnVerCocina);
        Button btnVerMesas = view.findViewById(R.id.btnVerMesas);

        // TODO:
        btnVerAlmacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductFragment productFragment = new ProductFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_containerMain, productFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });

        // TODO:
        btnVerEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersFragment usersFragment = new UsersFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_containerMain, usersFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });

        btnVerCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CocinaFragment cocinaFragment = new CocinaFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_containerMain, cocinaFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });

        btnVerMesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CamareroFragment camareroFragment = new CamareroFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_containerMain, camareroFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });



    }
}