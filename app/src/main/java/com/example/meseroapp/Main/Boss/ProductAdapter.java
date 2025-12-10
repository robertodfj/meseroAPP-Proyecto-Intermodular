package com.example.meseroapp.Main.Boss;

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

import data.entity.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEdit(Product product);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> list) {
        this.productList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false); // tu layout
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getProductName());
        holder.tvStock.setText("Stock: " + product.getStock());
        holder.tvPrice.setText("Precio: " + product.getPrice() + "€");

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvStock, tvPrice;
        Button btnEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvProductName);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEditar);
        }
    }
}