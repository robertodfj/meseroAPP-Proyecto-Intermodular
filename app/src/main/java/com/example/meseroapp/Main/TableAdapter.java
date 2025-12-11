package com.example.meseroapp.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import data.entity.Table;
import com.example.meseroapp.R;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<Table> tables = new ArrayList<>();
    private OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEdit(Table table);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.listener = listener;
    }

    public void setTables(List<Table> newTables) {
        this.tables = newTables;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table table = tables.get(position);

        holder.tvTableNumber.setText(table.getTableNumber());
        switch (table.getStatus()) {
            case "disponible":
                holder.imgStatus.setImageResource(R.drawable.ok);
                break;
            case "ocupada":
                holder.imgStatus.setImageResource(R.drawable.ocupado);
                break;
            case "reservada":
                holder.imgStatus.setImageResource(R.drawable.warning);
                break;
            default:
                holder.imgStatus.setImageResource(R.drawable.ok);
                break;
        }

        holder.btnComanda.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(table);  // Pasa el table específico al listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTableNumber;
        ImageView imgStatus;
        Button btnComanda;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            btnComanda = itemView.findViewById(R.id.btnComanda);
        }
    }
}