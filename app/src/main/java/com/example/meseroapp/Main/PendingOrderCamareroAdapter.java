package com.example.meseroapp.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meseroapp.R;

import java.util.ArrayList;
import java.util.List;

import data.dao.ProductDAO;
import data.database.AppDatabase;
import data.entity.LineOrder;
import data.entity.Product;


public class PendingOrderCamareroAdapter extends RecyclerView.Adapter<PendingOrderCamareroAdapter.ViewHolder> {

    private List<LineOrder> orders = new ArrayList<>();
    private OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEdit(LineOrder lineOrder);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.listener = listener;
    }

    public void setOrder(List<LineOrder> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_order_camarero_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LineOrder lineOrder = orders.get(position);

        ProductDAO productDao = AppDatabase.getInstance(holder.itemView.getContext()).productDao();

        // Hilo para consultar el nombre del producto
        new Thread(() -> {
            Product product = productDao.getById(lineOrder.getProductId());
            String productName = (product != null) ? product.getProductName() : "Producto desconocido";

            // Actualizar el TextView en el hilo principal
            holder.itemView.post(() -> holder.tvProduct.setText(productName));
        }).start();

        holder.tvTitle.setText("Mesa:" + lineOrder.getTableNumber());
        holder.tvQuantity.setText("Cantidad: " + lineOrder.getUnits());

        holder.btnCloseOrder.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(lineOrder);  // Pasa el order específico al listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvProduct, tvQuantity;
        Button btnCloseOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvPendingOrderTitle);
            tvProduct = itemView.findViewById(R.id.tvPendingProductName);
            tvQuantity = itemView.findViewById(R.id.tvPendingProductUnits);
            btnCloseOrder = itemView.findViewById(R.id.btnPendingCloseOrder);
        }
    }
}

