package com.example.meseroapp.Main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meseroapp.R;
import com.example.meseroapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import data.dao.OrderDAO;
import data.dao.TableDAO;
import data.database.AppDatabase;
import data.entity.LineOrder;
import data.entity.Order;
import data.entity.Product;
import data.entity.Table;
import data.service.EmailSenderService;

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

        Button btnAddTable = view.findViewById(R.id.addTable);

        adapter.setOnEditClickListener(table -> {

            switch (table.getStatus()) {
                case "reservada":
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Mesa: " + table.getTableNumber())
                            .setMessage("¿Qué quieres hacer?")
                            .setPositiveButton("Nueva orden", (d, w) -> openAddOrderDialog(db, table))
                            .setNegativeButton("Liberar mesa", (d, w) -> {
                                new Thread(() -> {
                                    try {
                                        table.setStatus("disponible");
                                        db.tableDao().update(table);
                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(getContext(),
                                                        "Mesa " + table.getTableNumber() + " liberada",
                                                        Toast.LENGTH_SHORT).show()
                                        );
                                    }catch (Exception e){
                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(getContext(),
                                                        "Error al liberar la mesa",
                                                        Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                }).start();
                            })
                            .show();
                    break;

                case "ocupada":
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Mesa: " + table.getTableNumber());

                    LinearLayout layout = new LinearLayout(requireContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 40, 50, 10);

                    Button btnViewOrder = new Button(requireContext());
                    btnViewOrder.setText("Ver comanda");

                    Button btnSendTicket = new Button(requireContext());
                    btnSendTicket.setText("Enviar factura");

                    Button btnAddOrder = new Button(requireContext());
                    btnAddOrder.setText("Añadir productos");

                    layout.addView(btnViewOrder);
                    layout.addView(btnSendTicket);
                    layout.addView(btnAddOrder);

                    builder.setView(layout);
                    builder.setNegativeButton("Volver", null);

                    AlertDialog dialog = builder.create();

                    btnAddOrder.setOnClickListener(v -> openAddOrderDialog(db, table));

                    btnSendTicket.setOnClickListener(v -> showEmailDialog(db, table));

                    btnViewOrder.setOnClickListener(v -> showOrder(db, table));

                    dialog.show();
                    break;
                default:
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Mesa: " + table.getTableNumber())
                            .setMessage("¿Qué quieres hacer?")
                            .setPositiveButton("Nueva orden", (d, w) -> openAddOrderDialog(db, table))
                            .setNegativeButton("Reservar mesa", (d, w) -> reserveTable(db, table))
                            .show();
                    break;

            }
        });
        btnAddTable.setOnClickListener(v -> addNewTable(db));
    }

    // AÑADIR LINEA A ORDEN
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
                cachedProducts
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(spinnerAdapter);

        EditText etQuantity = new EditText(requireContext());
        etQuantity.setHint("Cantidad");
        etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(etQuantity);

        builder.setView(layout);
        builder.setPositiveButton("Guardar", null);
        builder.setNeutralButton("Añadir más", null);
        builder.setNegativeButton("Volver", null);

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
                table.setStatus("ocupada");
                db.tableDao().update(table);

                long newId = db.orderDao().insert(order);
                order.setId((int)newId);
            }

            int orderId = order.getId();

            requireActivity().runOnUiThread(() -> {
                dialog.setOnShowListener(d -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setOnClickListener(v ->
                                    saveLineOrder(db, table, spinnerProducts, etQuantity, dialog, true, orderId)
                            );

                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                            .setOnClickListener(v ->
                                    saveLineOrder(db, table, spinnerProducts, etQuantity, dialog, false, orderId)
                            );
                });
                dialog.show();
            });
        }).start();
    }

    private void saveLineOrder(AppDatabase db, Table table,
                               Spinner spinnerProducts, EditText etQuantity,
                               AlertDialog dialog, boolean closeDialog, int orderId) {

        if (spinnerProducts.getSelectedItem() == null) return;

        int units;
        try {
            units = Integer.parseInt(etQuantity.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getContext(), "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = (Product) spinnerProducts.getSelectedItem();
        if (product.getStock() < units) {
            Toast.makeText(getContext(), "Stock insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (units <= 0) {
            Toast.makeText(getContext(), "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        LineOrder line = new LineOrder();
        line.setOrderId(orderId);
        line.setProductId(product.getId());
        line.setBarId(product.barId);
        line.setUnits(units);
        line.setLinePrice(product.getPrice() * units);
        line.setTableNumber(table.getTableNumber());
        line.setDone(false);

        new Thread(() -> {
            db.lineOrderDao().insert(line);

            product.setStock(product.getStock() - units);
            db.productDao().update(product);

            // ACTUALIZAR PRECIO TOTAL DE LA ORDEN
            db.orderDao().updateTotalPrice(orderId);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Producto añadido", Toast.LENGTH_SHORT).show();
                if (closeDialog) dialog.dismiss();
                else etQuantity.setText("");
            });
        }).start();
    }

    // EMAIL FACTURA

    private void showEmailDialog(AppDatabase db, Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enviar factura");

        EditText etEmail = new EditText(requireContext());
        etEmail.setHint("Correo electrónico");
        builder.setView(etEmail);

        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Enviar", (d, w) -> {
            String email = etEmail.getText().toString();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Email inválido", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            new Thread(() -> {
                Order order = db.orderDao().getLastOrderByTable(table.getId());
                if (order == null) return;

                db.orderDao().updateTotalPrice(order.getId());
                order.setClosed(true);
                table.setStatus("disponible");
                db.tableDao().update(table);
                db.orderDao().update(order);

                EmailSenderService service =
                        new EmailSenderService(db.orderDao(), db.productDao(), db.lineOrderDao(), db.barDao());

                boolean ok = service.sendOrderTicket(order.getId(), email);

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                ok ? "Factura enviada" : "Error al enviar",
                                Toast.LENGTH_SHORT).show()
                );
            }).start();
        });

        builder.show();
    }

    // RESERVAR MESA
    private void reserveTable(AppDatabase db, Table table) {
        new Thread(() -> {
            try {
                table.setStatus("reservada");
                db.tableDao().update(table);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                "Mesa " + table.getTableNumber() + " reservada",
                                Toast.LENGTH_SHORT).show()
                );
            }catch (Exception e){
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                "Error al reservar la mesa",
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void showOrder(AppDatabase db, Table table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Orden mesa " + table.getTableNumber());

        TextView textView = new TextView(requireContext());
        textView.setPadding(40, 30, 40, 30);
        builder.setView(textView);

        new Thread(() -> {
            Order order = db.orderDao().getLastOrderByTable(table.getId());

            if (order == null) {
                requireActivity().runOnUiThread(() ->
                        textView.setText("No hay ninguna orden activa")
                );
                return;
            }

            // Asegurar total correcto
            db.orderDao().updateTotalPrice(order.getId());
            order = db.orderDao().getById(order.getId());

            List<LineOrder> lines =
                    db.lineOrderDao().getLinesByOrder(order.getId());

            StringBuilder sb = new StringBuilder();
            double total = 0;

            for (LineOrder line : lines) {
                Product product = db.productDao().getById(line.getProductId());
                sb.append(product.getProductName())
                        .append(" x")
                        .append(line.getUnits())
                        .append(" → ")
                        .append(line.getLinePrice())
                        .append(" €\n");

                total += line.getLinePrice();
            }

            sb.append("\nTOTAL: ").append(total).append(" €");

            requireActivity().runOnUiThread(() ->
                    textView.setText(sb.toString())
            );

        }).start();

        builder.setNegativeButton("Volver", null);

        builder.setNeutralButton("Enviar factura", (d, w) ->
                showEmailDialog(db, table)
        );

        builder.setPositiveButton("Cerrar orden", (d, w) -> {
            new Thread(() -> {
                Order order = db.orderDao().getLastOrderByTable(table.getId());
                if (order != null) {
                    db.orderDao().updateTotalPrice(order.getId());
                    order.setClosed(true);
                    table.setStatus("disponible");
                    db.tableDao().update(table);
                    db.orderDao().update(order);
                }
            }).start();
        });

        builder.show();
    }

    private void addNewTable(AppDatabase db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Añadir nueva mesa");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText etTableNumber = new EditText(requireContext());
        etTableNumber.setHint("Número de mesa");
        etTableNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(etTableNumber);

        EditText etSpace = new EditText(requireContext());
        etSpace.setHint("Capacidad");
        etSpace.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(etSpace);

        builder.setView(layout);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String tableText = etTableNumber.getText().toString().trim();
            String spaceText = etSpace.getText().toString().trim();

            if (tableText.isEmpty() || spaceText.isEmpty()) {
                Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int tableNumber = Integer.parseInt(tableText);
            int space = Integer.parseInt(spaceText);
            int barId = SessionManager.getInstance(requireContext()).getBarId();

            new Thread(() -> {
                Table existingTable = db.tableDao().getByTableNumber(tableNumber, barId);

                if (existingTable != null) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Ya existe una mesa con ese número", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                Table table = new Table();
                table.setTableNumber(tableNumber);
                table.setSpace(space);
                table.setBarId(barId);
                table.setStatus("disponible");

                db.tableDao().insert(table);

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Mesa añadida", Toast.LENGTH_SHORT).show()
                );
            }).start();
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }
}