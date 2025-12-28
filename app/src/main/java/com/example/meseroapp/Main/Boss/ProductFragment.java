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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import data.database.AppDatabase;
import data.entity.Product;

public class ProductFragment extends Fragment {

    private AppDatabase db;
    private int barId;

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

        FloatingActionButton addProduct = view.findViewById(R.id.addProduct);

        barId = SessionManager.getInstance(getContext()).getBarId();
        db = AppDatabase.getInstance(getContext());

        db.productDao()
                .getProductsByBarId(barId)
                .observe(getViewLifecycleOwner(), adapter::setProducts);

        addProduct.setOnClickListener(v -> addProductBar());
        adapter.setOnEditClickListener(product -> showEditDialog(product));
    }

    //Añadir un nuevo producto
    private void addProductBar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Añadir producto");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextInputEditText etName = new TextInputEditText(requireContext());
        etName.setHint("Nombre del producto");

        TextInputEditText etPrice = new TextInputEditText(requireContext());
        etPrice.setHint("Precio");
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        TextInputEditText etStock = new TextInputEditText(requireContext());
        etStock.setHint("Stock");
        etStock.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(etName);
        layout.addView(etPrice);
        layout.addView(etStock);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", null);
        builder.setNegativeButton("Cancelar", null);

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
                    // Todo añadir un producto nuevo con el id del bar del usuario

                    db.productDao().insert(product);

                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(),
                                    "Producto añadido",
                                    Toast.LENGTH_SHORT).show()
                    );
                }).start();

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    // Editar o borrar producto
    private void showEditDialog(Product product) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar producto");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextInputEditText etName = new TextInputEditText(requireContext());
        etName.setText(product.getProductName());

        TextInputEditText etPrice = new TextInputEditText(requireContext());
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPrice.setText(String.valueOf(product.getPrice()));

        TextInputEditText etStock = new TextInputEditText(requireContext());
        etStock.setInputType(InputType.TYPE_CLASS_NUMBER);
        etStock.setText(String.valueOf(product.getStock()));

        layout.addView(etName);
        layout.addView(etPrice);
        layout.addView(etStock);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", null);
        builder.setNeutralButton("Borrar", null);
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                new Thread(() -> {
                    db.productDao().updateProductName(product.getId(), etName.getText().toString());
                    db.productDao().updateProductPrice(product.getId(),
                            Double.parseDouble(etPrice.getText().toString()));
                    db.productDao().updateProductStock(product.getId(),
                            Integer.parseInt(etStock.getText().toString()));

                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(),
                                    "Producto actualizado",
                                    Toast.LENGTH_SHORT).show()
                    );
                }).start();
                dialog.dismiss();
            });

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                new Thread(() -> db.productDao().delete(product)).start();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}