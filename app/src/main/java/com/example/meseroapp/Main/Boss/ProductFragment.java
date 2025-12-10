package com.example.meseroapp.Main.Boss;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import data.entity.Product;

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

        // Observamos los productos del bar y actualizamos el RecyclerView
        db.productDao().getProductsByBarId(barId).observe(getViewLifecycleOwner(), adapter::setProducts);

        adapter.setOnEditClickListener(product -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Editar producto");

            LinearLayout layout = new LinearLayout(requireContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            EditText etName = new EditText(requireContext());
            etName.setHint("Nombre del producto");
            etName.setText(product.getProductName());
            layout.addView(etName);

            EditText etPrice = new EditText(requireContext());
            etPrice.setHint("Precio");
            etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etPrice.setText(String.valueOf(product.getPrice()));
            layout.addView(etPrice);

            EditText etStock = new EditText(requireContext());
            etStock.setHint("Stock");
            etStock.setInputType(InputType.TYPE_CLASS_NUMBER);
            etStock.setText(String.valueOf(product.getStock()));
            layout.addView(etStock);

            builder.setView(layout);

            // Botón Guardar edición
            builder.setPositiveButton("Guardar", null);
            // Botón Volver atrás
            builder.setNegativeButton("Volver", (dialog, which) -> dialog.dismiss());
            // Botón Borrar producto
            builder.setNeutralButton("Borrar", null);

            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(d -> {
                Button btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnBorrar = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                // Confirmar edición
                btnGuardar.setOnClickListener(v -> {
                    String newName = etName.getText().toString().trim();
                    String newPriceStr = etPrice.getText().toString().trim();
                    String newStockStr = etStock.getText().toString().trim();

                    if (newName.isEmpty() || newPriceStr.isEmpty() || newStockStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double newPrice = Double.parseDouble(newPriceStr);
                    int newStock = Integer.parseInt(newStockStr);

                    new Thread(() -> {
                        db.productDao().updateProductName(product.getId(), newName);
                        db.productDao().updateProductPrice(product.getId(), newPrice);
                        db.productDao().updateProductStock(product.getId(), newStock);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }).start();
                });

                // Borrar producto
                btnBorrar.setOnClickListener(v -> {
                    new Thread(() -> {
                        db.productDao().delete(product);
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Producto borrado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }).start();
                });
            });

            dialog.show();
        });
    }
}