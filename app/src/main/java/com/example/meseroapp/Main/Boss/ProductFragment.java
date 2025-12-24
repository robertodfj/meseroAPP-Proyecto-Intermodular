package com.example.meseroapp.Main.Boss;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import data.database.AppDatabase;
import data.entity.Product;

public class ProductFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recycler = view.findViewById(R.id.rvProduct);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ProductAdapter adapter = new ProductAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        db.productDao()
                .getProductsByBarId(barId)
                .observe(getViewLifecycleOwner(), adapter::setProducts);

        adapter.setOnEditClickListener(product -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Editar producto");

            LinearLayout layout = new LinearLayout(requireContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            TextInputLayout tilName = new TextInputLayout(requireContext());
            TextInputEditText etName = new TextInputEditText(requireContext());
            tilName.setHint("Nombre del producto");
            etName.setText(product.getProductName());
            tilName.addView(etName);

            TextInputLayout tilPrice = new TextInputLayout(requireContext());
            TextInputEditText etPrice = new TextInputEditText(requireContext());
            tilPrice.setHint("Precio");
            etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etPrice.setText(String.valueOf(product.getPrice()));
            tilPrice.addView(etPrice);

            TextInputLayout tilStock = new TextInputLayout(requireContext());
            TextInputEditText etStock = new TextInputEditText(requireContext());
            tilStock.setHint("Stock");
            etStock.setInputType(InputType.TYPE_CLASS_NUMBER);
            etStock.setText(String.valueOf(product.getStock()));
            tilStock.addView(etStock);

            layout.addView(tilName);
            layout.addView(tilPrice);
            layout.addView(tilStock);

            builder.setView(layout);

            builder.setPositiveButton("Guardar", null);
            builder.setNegativeButton("Volver", (d, w) -> d.dismiss());
            builder.setNeutralButton("Borrar", null);

            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(d -> {

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    if (etName.getText().toString().isEmpty()
                            || etPrice.getText().toString().isEmpty()
                            || etStock.getText().toString().isEmpty()) {

                        Toast.makeText(getContext(),
                                "Todos los campos son obligatorios",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(() -> {
                        db.productDao().updateProductName(product.getId(), etName.getText().toString());
                        db.productDao().updateProductPrice(product.getId(),
                                Double.parseDouble(etPrice.getText().toString()));
                        db.productDao().updateProductStock(product.getId(),
                                Integer.parseInt(etStock.getText().toString()));

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }).start();
                });

                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                    new Thread(() -> {
                        db.productDao().delete(product);
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Producto borrado", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }).start();
                });
            });

            dialog.show();
        });
    }
}