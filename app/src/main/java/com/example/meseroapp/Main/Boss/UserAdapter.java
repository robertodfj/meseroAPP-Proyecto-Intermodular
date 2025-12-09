package com.example.meseroapp.Main.Boss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import data.entity.User;
import com.example.meseroapp.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users = new ArrayList<>();

    public void setUsers(List<User> newUsers) {
        this.users = newUsers;
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
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFullname, tvEmail, tvRol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullname = itemView.findViewById(R.id.tvFullname);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRol = itemView.findViewById(R.id.tvRol);
        }
    }
}
