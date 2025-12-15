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

import data.dao.OrderDAO;
import data.database.AppDatabase;
import data.entity.LineOrder;
import data.entity.Order;
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
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        TableAdapter adapter = new TableAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();
        AppDatabase db = AppDatabase.getInstance(getContext());

        db.tableDao()
                .getByBarId(barId)
                .observe(getViewLifecycleOwner(), adapter::setTables);

        db.productDao()
                .getProductsByBarId(barId)
                .observe(getViewLifecycleOwner(), products -> {
                    cachedProducts.clear();
                    cachedProducts.addAll(products);
                });

        adapter.setOnEditClickListener(table -> {

            switch (table.getStatus()) {

                case "disponible":
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Mesa: " + table.getTableNumber());
                    builder.setMessage("¿Quieres abir una nueva orden para la mesa " + table.getTableNumber() + "?");
                    builder.setPositiveButton("Sí", (dialog, which) -> openAddOrderDialog(db, table));
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    builder.show();
                    break;

                case "ocupada":
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
                    builder2.setTitle("Mesa: " + table.getTableNumber());

                    LinearLayout layout2 = new LinearLayout(requireContext());
                    layout2.setOrientation(LinearLayout.VERTICAL);
                    layout2.setPadding(50, 40, 50, 10);

                    Button btnViewOrder = new Button(requireContext());
                    btnViewOrder.setText("Ver comanda");
                    Button btnSendTicket = new Button(requireContext());
                    btnSendTicket.setText("Enviar Factura a correo");
                    Button btnAddOrder = new Button(requireContext());
                    btnAddOrder.setText("Añadir productos a la orden");

                    layout2.addView(btnViewOrder);
                    layout2.addView(btnSendTicket);
                    layout2.addView(btnAddOrder);

                    builder2.setView(layout2);
                    builder2.setNegativeButton("Volver", (dialog2, which) -> dialog2.dismiss());

                    AlertDialog dialog2 = builder2.create();

                    btnAddOrder.setOnClickListener(v -> openAddOrderDialog(db, table));

                    dialog2.show();
                    break;

                case "reservada":
                    break;

                default:
                    break;
            }
        });
    }

    private void openAddOrderDialog(AppDatabase db, Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Mesa: " + table.getTableNumber());

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
        spinnerAdapter.addAll(cachedProducts);

        EditText etQuantity = new EditText(requireContext());
        etQuantity.setHint("Cantidad");
        etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(etQuantity);

        builder.setView(layout);
        builder.setNegativeButton("Volver", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        new Thread(() -> {
            OrderDAO orderDao = db.orderDao();
            Order order = orderDao.getLastOrderByTable(table.getId());

            if (order == null) {
                order = new Order();
                order.setTableId(table.getId());
                order.setBarId(SessionManager.getInstance(getContext()).getBarId());
                order.setTotalPrice(0);
                order.setClosed(false);

                long newOrderId = orderDao.insert(order);
                order.setId((int) newOrderId);
            }

            int orderId = order.getId();

            requireActivity().runOnUiThread(() -> {
                dialog.setOnShowListener(d -> {
                    Button btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button btnMas = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                    btnGuardar.setOnClickListener(v ->
                            saveLineOrder(db, table, spinnerProducts, etQuantity, dialog, true, orderId)
                    );

                    btnMas.setOnClickListener(v ->
                            saveLineOrder(db, table, spinnerProducts, etQuantity, dialog, false, orderId)
                    );
                });

                dialog.show();
            });
        }).start();
    }

    private void saveLineOrder(AppDatabase db, Table table, Spinner spinnerProducts,
                               EditText etQuantity, AlertDialog dialog, boolean closeDialog, int orderId) {

        if (spinnerProducts.getSelectedItem() == null) {
            Toast.makeText(requireContext(), "No hay productos", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = etQuantity.getText().toString();
        if (quantityStr.isEmpty()) {
            Toast.makeText(requireContext(), "Introduce una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int units;
        try {
            units = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (units <= 0) {
            Toast.makeText(requireContext(), "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = (Product) spinnerProducts.getSelectedItem();
        if (product.getStock() < units) {
            Toast.makeText(requireContext(), "Stock insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        double linePrice = product.getPrice() * units;

        LineOrder lineOrder = new LineOrder();
        lineOrder.setProductId(product.getId());
        lineOrder.setOrderId(orderId);
        lineOrder.setUnits(units);
        lineOrder.setLinePrice(linePrice);
        lineOrder.setTableNumber(table.getTableNumber());
        lineOrder.setDone(false);

        new Thread(() -> {
            db.lineOrderDao().insert(lineOrder);

            product.setStock(product.getStock() - units);
            db.productDao().update(product);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Producto añadido", Toast.LENGTH_SHORT).show();
                if (closeDialog) {
                    dialog.dismiss();
                } else {
                    etQuantity.setText("");
                }
            });
        }).start();
    }
}