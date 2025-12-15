package com.example.meseroapp.Main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import data.entity.LineOrder;
import data.entity.Product;
import data.entity.Table;

public class CamareroFragment extends Fragment {

    private final List<Product> cachedProducts = new ArrayList<>();

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

        RecyclerView recycler = view.findViewById(R.id.rvMesas);
        recycler.setLayoutManager(
                new GridLayoutManager(getContext(), 2)
        );

        TableAdapter adapter = new TableAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        // Observar mesas
        db.tableDao()
                .getByBarId(barId)
                .observe(getViewLifecycleOwner(), adapter::setTables);

        // Observar productos UNA VEZ
        db.productDao()
                .getProductsByBarId(barId)
                .observe(getViewLifecycleOwner(), products -> {
                    cachedProducts.clear();
                    cachedProducts.addAll(products);
                });

        adapter.setOnEditClickListener(table -> {

            switch (table.getStatus()) {

                case "disponible":

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(requireContext());
                    builder.setTitle("Añadir comanda");

                    LinearLayout layout =
                            new LinearLayout(requireContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 40, 50, 10);

                    Spinner spinnerProducts =
                            new Spinner(requireContext());
                    layout.addView(spinnerProducts);

                    ArrayAdapter<Product> spinnerAdapter =
                            new ArrayAdapter<>(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    new ArrayList<>()
                            );
                    spinnerAdapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item);

                    spinnerProducts.setAdapter(spinnerAdapter);
                    spinnerAdapter.addAll(cachedProducts);

                    EditText etQuantity =
                            new EditText(requireContext());
                    etQuantity.setHint("Cantidad");
                    etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(etQuantity);

                    builder.setView(layout);

                    builder.setPositiveButton("Guardar", null);
                    builder.setNeutralButton("Añadir más", null);
                    builder.setNegativeButton("Volver",
                            (dialog, which) -> dialog.dismiss());

                    AlertDialog dialog = builder.create();

                    dialog.setOnShowListener(d -> {

                        Button btnGuardar =
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button btnMas =
                                dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                        btnGuardar.setOnClickListener(v -> {
                            saveLineOrder(
                                    db,
                                    table,
                                    spinnerProducts,
                                    etQuantity,
                                    dialog,
                                    true
                            );
                        });

                        btnMas.setOnClickListener(v -> {
                            saveLineOrder(
                                    db,
                                    table,
                                    spinnerProducts,
                                    etQuantity,
                                    dialog,
                                    false
                            );
                        });
                    });

                    dialog.show();
                    break;

                case "ocupada":
                    break;

                case "reservada":
                    break;

                default:
                    break;
            }
        });
    }

    /**
     * Guarda una línea de comanda.
     * @param closeDialog true -> cierra diálogo | false -> mantiene abierto
     */
    private void saveLineOrder(
            AppDatabase db,
            Table table,
            Spinner spinnerProducts,
            EditText etQuantity,
            AlertDialog dialog,
            boolean closeDialog
    ) {

        if (spinnerProducts.getSelectedItem() == null) {
            Toast.makeText(requireContext(),
                    "No hay productos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = etQuantity.getText().toString();

        if (quantityStr.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Introduce una cantidad",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int units;
        try {
            units = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(),
                    "Cantidad inválida",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (units <= 0) {
            Toast.makeText(requireContext(),
                    "Cantidad inválida",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Product product =
                (Product) spinnerProducts.getSelectedItem();

        if (product.getStock() < units) {
            Toast.makeText(requireContext(),
                    "Stock insuficiente",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double linePrice = product.getPrice() * units;

        LineOrder lineOrder = new LineOrder();
        lineOrder.setProductId(product.getId());
        lineOrder.setUnits(units);
        lineOrder.setLinePrice(linePrice);
        lineOrder.setTableNumber(table.getTableNumber());
        lineOrder.setDone(false);

        new Thread(() -> {

            db.lineOrderDao().insert(lineOrder);

            product.setStock(product.getStock() - units);
            db.productDao().update(product);

            requireActivity().runOnUiThread(() -> {

                Toast.makeText(requireContext(),
                        "Producto añadido",
                        Toast.LENGTH_SHORT).show();

                if (closeDialog) {
                    dialog.dismiss();
                } else {
                    etQuantity.setText("");
                }
            });

        }).start();
    }
}