package com.example.meseroapp.Main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import data.database.AppDatabase;
import data.entity.Product;
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
                switch (table.getStatus()) {
                    case "disponible":
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Añadir comanda");

                        LinearLayout layout = new LinearLayout(requireContext());
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setPadding(50, 40, 50, 10);

                        Spinner spinnerProducts = new Spinner(requireContext());
                        layout.addView(spinnerProducts);

                        ArrayAdapter<Product> spinnerAdapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                new ArrayList<>()
                        );

                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerProducts.setAdapter(spinnerAdapter);

                        // Observar productos
                        db.productDao()
                                .getProductsByBarId(barId)
                                .observe(getViewLifecycleOwner(), products -> {
                                    spinnerAdapter.clear();
                                    spinnerAdapter.addAll(products);
                                    spinnerAdapter.notifyDataSetChanged();
                                });

                        EditText etQuantity = new EditText(requireContext());
                        etQuantity.setHint("Cantidad");
                        etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                        layout.addView(etQuantity);

                        // Botón Guardar comanda
                        builder.setPositiveButton("Guardar", null);
                        // Botón Volver atrás
                        builder.setNegativeButton("Volver", (dialog, which) -> dialog.dismiss());
                        // Botón Borrar producto
                        builder.setNeutralButton("Añadir más", null);

                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(d -> {
                            Button btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button btnMas = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                            // Confirmar edición
                            btnGuardar.setOnClickListener(v -> {
                                // Aquí guardarías la comanda en la base de datos
                                Toast.makeText(requireContext(), "Comanda guardada", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                            btnMas.setOnClickListener(v -> {
                                // Aquí añadirías más productos a la comanda
                                Toast.makeText(requireContext(), "Producto añadido", Toast.LENGTH_SHORT).show();
                                etQuantity.setText("");
                            });
                        });

                        dialog.show();
                });
                        break;
                    case "ocupada":

                        break;
                    case "reservada":

                        break;
                    default:

                        break;
                }

            }
        });
    }
}