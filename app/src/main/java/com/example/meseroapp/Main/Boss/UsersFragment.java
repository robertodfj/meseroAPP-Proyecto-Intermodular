package com.example.meseroapp.Main.Boss;

import android.os.Bundle;
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

import data.database.AppDatabase;

public class UsersFragment extends Fragment {

    public UsersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.rvEmpleados);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        UserAdapter adapter = new UserAdapter();
        recycler.setAdapter(adapter);

        int barId = SessionManager.getInstance(getContext()).getBarId();

        AppDatabase db = AppDatabase.getInstance(getContext());

        db.userDao().getEmpleados(barId).observe(getViewLifecycleOwner(), users -> {
            adapter.setUsers(users);
        });
    }
}