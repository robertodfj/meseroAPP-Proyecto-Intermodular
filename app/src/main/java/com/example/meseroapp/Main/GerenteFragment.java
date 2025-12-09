package com.example.meseroapp.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meseroapp.Login.BossFragment;
import com.example.meseroapp.R;

public class GerenteFragment extends Fragment {

    public GerenteFragment() {}

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
                AlmacenFragment almacenFragment = new AlmacenFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, almacenFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });

        // TODO:
        btnVerEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpleadosFragment empleadosFragment = new EmpleadosFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, empleadosFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });

        btnVerCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CocinaFragment cocinaFragment = new CocinaFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cocinaFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });

        btnVerMesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CamareroFragment camareroFragment = new CamareroFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, camareroFragment) // frameLayout del Activity
                        .addToBackStack(null) // permite volver con el botón atrás
                        .commit();
            }
        });



    }
}