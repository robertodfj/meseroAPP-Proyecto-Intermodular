package com.example.meseroapp.Main.Boss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import data.entity.User;
import com.example.meseroapp.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users = new ArrayList<>();
    private List<User> usersFull = new ArrayList<>(); // copia completa para filtrado
    private OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEdit(User user);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<User> newUsers) {
        this.users = new ArrayList<>(newUsers);
        this.usersFull = new ArrayList<>(newUsers); // guardamos copia completa
        notifyDataSetChanged();
    }

    public void filter(String text) {
        List<User> filteredList = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            filteredList.addAll(usersFull);
        } else {
            String query = text.toLowerCase();
            for (User u : usersFull) {
                if (u.getName().toLowerCase().contains(query) ||
                        u.getEmail().toLowerCase().contains(query)) {
                    filteredList.add(u);
                }
            }
        }
        users.clear();
        users.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvFullname.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvRol.setText(user.getRol());

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullname, tvEmail, tvRol;
        Button btnEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullname = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRol = itemView.findViewById(R.id.tvRol);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}